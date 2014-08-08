package com.rhsquashclub.arhscbook.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rhsquashclub.arhscbook.BookSinglesActivity;
import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.RHSCCourtSelection;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;
import com.rhsquashclub.arhscbook.model.RHSCPreferences;
import com.rhsquashclub.arhscbook.model.RHSCSelectedCourtTimes;
import com.rhsquashclub.arhscbook.model.RHSCServer;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

public class RHSCSelectedCourtTimesFragment extends Fragment {

	private RHSCSelectedCourtTimes courts;
	private Calendar selectedDate;
	
	private RHSCCourtTimeAdapter listAdapter;
	private Button dateSel = null;

	private RHSCDatePickerDialog dialog = null;
	
	static final int BOOK_SINGLES_COURT = 1;

	public RHSCSelectedCourtTimesFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
//		Log.i("RHSCSelectedCourtTimesFragment:onCreate","entry");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
//		Log.i("RHSCSelectedCourtTimesFragment:onCreateView","entry");
		View view = inflater.inflate(R.layout.fragment_selectedcourtlist, container, false);

		getActivity().setTitle(R.string.courts_title); 
		
		courts = RHSCSelectedCourtTimes.get(getActivity());
		selectedDate = Calendar.getInstance();

		Spinner courtSel = (Spinner) view.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.court_selection_array, R.layout.spinner_item);
		// Specify the layout to use when the list of choices appears
//		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		courtSel.setAdapter(spinnerAdapter);
		courtSel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        // your code here
		    	RHSCPreferences.get().setCourtSelection(RHSCCourtSelection.find(position));
		    	// notify listview to refresh
		    	Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
				String[] parms = { String.format("%d-%02d=%02d",sd.get(Calendar.YEAR) ,sd.get(Calendar.MONTH)+1,sd.get(Calendar.DAY_OF_MONTH)), 
						RHSCPreferences.get().getCourtSelection().getText(), 
						RHSCPreferences.get().isIncludeBookings()?"YES":"NO", 
						RHSCPreferences.get().getUserid() };
				RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
				bgTask.execute(parms);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		dateSel = (Button) view.findViewById(R.id.dateButton1);
		String buttonText = new SimpleDateFormat(
				"EEE, MMM d", Locale.ENGLISH)
				.format(selectedDate.getTime());
		dateSel.setText(buttonText);
		dateSel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar cDate = Calendar.getInstance();
			    cDate.set(Calendar.MINUTE, 0);
			    cDate.set(Calendar.SECOND, 0);
			    cDate.set(Calendar.MILLISECOND, 0);
				Calendar tDate = Calendar.getInstance();
				tDate.add(Calendar.DATE, 30);
				dialog = new RHSCDatePickerDialog(getActivity(), 
						new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								RHSCSelectedCourtTimesFragment.this.selectedDate = new GregorianCalendar(year,monthOfYear,dayOfMonth,0,0,0);
								String[] parms = { String.format("%d-%02d=%02d",year,monthOfYear+1,dayOfMonth), 
										RHSCPreferences.get().getCourtSelection().getText(), 
										RHSCPreferences.get().isIncludeBookings()?"YES":"NO", 
										RHSCPreferences.get().getUserid() };
								RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
								bgTask.execute(parms);
								String buttonText = new SimpleDateFormat(
										"EEE, MMM d", Locale.ENGLISH)
										.format(RHSCSelectedCourtTimesFragment.this.selectedDate
												.getTime());
								RHSCSelectedCourtTimesFragment.this.dateSel
										.setText(buttonText);
							}
						},
						RHSCSelectedCourtTimesFragment.this.selectedDate, cDate, tDate);
				dialog.show();
			}
			
		});
		
		Switch includeSel = (Switch) view.findViewById(R.id.switch1);
		includeSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        // do something, the isChecked will be
		        // true if the switch is in the On position
		    	RHSCPreferences.get().setIncludeBookings(isChecked);
		    	Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
				String[] parms = { String.format("%d-%02d=%02d",sd.get(Calendar.YEAR) ,sd.get(Calendar.MONTH)+1,sd.get(Calendar.DAY_OF_MONTH)), 
						RHSCPreferences.get().getCourtSelection().getText(), 
						RHSCPreferences.get().isIncludeBookings()?"YES":"NO", 
						RHSCPreferences.get().getUserid() };
				RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
				bgTask.execute(parms);
		    }
		});
		includeSel.setChecked(RHSCPreferences.get().isIncludeBookings());
		
		listAdapter = 
				new RHSCCourtTimeAdapter(getActivity(), R.layout.court_times_list_item_row,courts);
		ListView lv = (ListView) view.findViewById(R.id.CourtListFragment);
		lv.setAdapter( listAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("SelectedCourtTimes",
						String.format("item %d clicked", position));
				if (courts.get(position).getStatus().equals("Available")) {
					if (courts.get(position).getCourt().equals("Court 5")) {

					} else {
						Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
						Log.i("GSON Court Time",gson.toJson(courts.get(position)));

						Intent intent = new Intent(getActivity(),
								BookSinglesActivity.class);
						intent.putExtra("court", gson.toJson(courts.get(position)));
						startActivityForResult(intent,BOOK_SINGLES_COURT);
					}
				} else {
					// not available - cancel?
				}
			}
		});		
		
    	Calendar sd = selectedDate;
		String[] parms = { String.format("%d-%02d=%02d",sd.get(Calendar.YEAR) ,sd.get(Calendar.MONTH)+1,sd.get(Calendar.DAY_OF_MONTH)), 
				RHSCPreferences.get().getCourtSelection().getText(), 
				RHSCPreferences.get().isIncludeBookings()?"YES":"NO", 
				RHSCPreferences.get().getUserid() };
		RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
		bgTask.execute(parms);
		// use view.findViewById(id) to set values in the view
		
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == BOOK_SINGLES_COURT) {
	        // Make sure the request was successful
	        if (resultCode == android.app.Activity.RESULT_OK) {
//	        	Gson gson = new Gson();
//	        	RHSCCourtTime res = gson.fromJson(data.getExtras().getString("court"), RHSCCourtTime.class);
	        	Log.i("return from book singles","Booked");
	        }
	        if (resultCode == android.app.Activity.RESULT_CANCELED) {
	        	Log.i("return from book singles",data.getExtras().getString("reason"));
	        }
	    }
	}

	public RHSCCourtTimeAdapter getListAdapter() {
		return listAdapter;
	}
	
	
	private class RHSCSelectCourtTimesTask extends AsyncTask<String, Void, String> {
		
		public URI getRequestURI(String scheddate,String courttype,String include,String uid) {
			String myURL = String.format("http://%s/Reserve/IOSTimesJSON.php?scheddate=%s&courttype=%s&include=%s&uid=%s",
					RHSCServer.get().getURL(), scheddate, courttype, include, uid);
			try {
				URI targetURI = new URI(myURL);
				return targetURI;
			} catch (URISyntaxException e) {
				Log.e("URI Syntax Exception",e.toString());
				return null;
			}
		}
		
	    @Override
	    protected String doInBackground(String... parms) {
	    	// parm 1 is scheddate
	    	// parm 2 is courttype
	    	// parm 3 is include (YES/NO)
	    	// parm 4 is uid
//			Log.i("RHSCSelectedCourtTimesTask","doInBackground");
	    	URI targetURI = getRequestURI(parms[0],parms[1],parms[2],parms[3]);
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(targetURI);
	        try {
	                HttpResponse response = client.execute(httpGet);
//	        		Log.i("RHSCSelectedCourtTimesTask","returned from execute");
	                StatusLine statusLine = response.getStatusLine();
	                int statusCode = statusLine.getStatusCode();
	                if (statusCode == 200) {
	                        HttpEntity entity = response.getEntity();
	                        InputStream content = entity.getContent();
	                        BufferedReader reader = new BufferedReader(
	                                        new InputStreamReader(content));
	                        String line;
	                        while ((line = reader.readLine()) != null) {
	                                builder.append(line);
	                        }
//	                        Log.v("Getter", "Your data: " + builder.toString()); //response data
	                        return builder.toString();
	                } else {
	                        Log.e("Getter", "Failed to download file");
	                }
	        } catch (ClientProtocolException e) {
	        		Log.e("Getter", "ClientProtocolException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	        } catch (IOException e) {
	            	Log.e("Getter", "IOException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	        }
	        
	    	return null;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
//			Log.i("RHSCSelectedCourtTimesTask:postExecute",result);
	    	if (result != null) {
	    		courts.loadFromJSON(result,"courtTimes");
	            listAdapter.notifyDataSetChanged();
	    	}
	    }

	}


}
