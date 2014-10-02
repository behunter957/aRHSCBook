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
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rhsquashclub.arhscbook.BookDoublesActivity;
import com.rhsquashclub.arhscbook.BookSinglesActivity;
import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.RHSCMain;
import com.rhsquashclub.arhscbook.model.RHSCCourtSelection;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;
import com.rhsquashclub.arhscbook.model.RHSCPreferences;
import com.rhsquashclub.arhscbook.model.RHSCSelectedCourtTimes;
import com.rhsquashclub.arhscbook.model.RHSCServer;
import com.rhsquashclub.arhscbook.model.RHSCUser;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class RHSCSelectedCourtTimesFragment extends Fragment {

	private RHSCSelectedCourtTimes courts = null;
	private Calendar selectedDate = Calendar.getInstance();
	private Calendar currentDate;
	private RHSCCourtTime selectedCourtTime;
	
	private RHSCCourtTimeAdapter listAdapter;
	private Button dateSel = null;

	private RHSCDatePickerDialog dialog = null;
	
	static final int BOOK_SINGLES_COURT = 1;
	static final int BOOK_DOUBLES_COURT = 2;

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
		currentDate = Calendar.getInstance();
		if (selectedDate == null) {
			selectedDate = Calendar.getInstance();
		} 
		if (selectedDate.getTimeInMillis() < currentDate.getTimeInMillis()) {
			selectedDate = currentDate;
		}

		Spinner courtSel = (Spinner) view.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.court_selection_array, R.layout.spinner_item);
		// Specify the layout to use when the list of choices appears
//		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		courtSel.setAdapter(spinnerAdapter);
		courtSel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				// your code here
				RHSCPreferences.get().setCourtSelection(
						RHSCCourtSelection.find(position));
				// notify listview to refresh
				Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
				if (RHSCUser.get().isLoggedOn()) {
					String[] parms = {
							String.format("%d-%02d=%02d",
									sd.get(Calendar.YEAR),
									sd.get(Calendar.MONTH) + 1,
									sd.get(Calendar.DAY_OF_MONTH)),
							RHSCPreferences.get().getCourtSelection().getText(),
							RHSCPreferences.get().isIncludeBookings() ? "YES"
									: "NO", RHSCUser.get().getName() };
					RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
					bgTask.execute(parms);
				}
			}

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		courtSel.setSelection(RHSCPreferences.get().getCourtSelection().getPos());
		
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
								if (RHSCUser.get().isLoggedOn()) {
									String[] parms = {
											String.format("%d-%02d=%02d", year,
													monthOfYear + 1, dayOfMonth),
											RHSCPreferences.get()
													.getCourtSelection()
													.getText(),
											RHSCPreferences.get()
													.isIncludeBookings() ? "YES"
													: "NO",
											RHSCUser.get().getName() };
									RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
									bgTask.execute(parms);
									String buttonText = new SimpleDateFormat(
											"EEE, MMM d", Locale.ENGLISH)
											.format(RHSCSelectedCourtTimesFragment.this.selectedDate
													.getTime());
									RHSCSelectedCourtTimesFragment.this.dateSel
											.setText(buttonText);
								}
							}
						},
						RHSCSelectedCourtTimesFragment.this.selectedDate, cDate, tDate);
				dialog.show();
			}
			
		});
		
		Switch includeSel = (Switch) view.findViewById(R.id.switch1);
		includeSel
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// do something, the isChecked will be
						// true if the switch is in the On position
						RHSCPreferences.get().setIncludeBookings(isChecked);
						Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
						if (RHSCUser.get().isLoggedOn()) {
							String[] parms = {
									String.format("%d-%02d=%02d",
											sd.get(Calendar.YEAR),
											sd.get(Calendar.MONTH) + 1,
											sd.get(Calendar.DAY_OF_MONTH)),
									RHSCPreferences.get().getCourtSelection()
											.getText(),
									RHSCPreferences.get().isIncludeBookings() ? "YES"
											: "NO", RHSCUser.get().getName() };
							RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
							bgTask.execute(parms);
						}
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
				selectedCourtTime = courts.get(position); 
				if (courts.get(position).getStatus().equals("Available")) {
					Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
					Log.i("GSON Court Time",gson.toJson(courts.get(position)));

					if (courts.get(position).getCourt().equals("Court 5")) {
						Intent intent = new Intent(getActivity(),
								BookDoublesActivity.class);
						intent.putExtra("court", gson.toJson(courts.get(position)));
						startActivityForResult(intent,BOOK_DOUBLES_COURT);
					} else {
						Intent intent = new Intent(getActivity(),
								BookSinglesActivity.class);
						intent.putExtra("court", gson.toJson(courts.get(position)));
						startActivityForResult(intent,BOOK_SINGLES_COURT);
					}
				} else {
					// not available - cancel?
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							getActivity());

					// set title
					String ctdText = String.format("%s on %s", courts.get(position).getCourt(),new SimpleDateFormat(
							"EEEE, MMMM d 'at' h:mm", Locale.ENGLISH)
							.format(courts.get(position).getCourtTime()));
					alertDialogBuilder.setTitle(ctdText);

					// set dialog message
					alertDialogBuilder
							.setMessage("Do you wish to cancel this booking?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											// MainActivity.this.finish();
											String[] parms = { selectedCourtTime.getBookingId(), RHSCUser.get().getName(), 
													selectedCourtTime.getPlayer_id()[1], selectedCourtTime.getPlayer_id()[2], selectedCourtTime.getPlayer_id()[3],selectedCourtTime.getEvent() };
											RHSCCancelCourtTimeTask bgTask = new RHSCCancelCourtTimeTask();
											bgTask.execute(parms);
											dialog.cancel();
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, just
											// close
											// the dialog box and do nothing
											dialog.cancel();
										}
									});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
				}
			}
		});
		
    	Calendar sd = selectedDate;
    	// TODO check if logged on before allowing retrieval of court times
		if (RHSCUser.get().isLoggedOn()) {
			String[] parms = {
					String.format("%d-%02d=%02d", sd.get(Calendar.YEAR),
							sd.get(Calendar.MONTH) + 1,
							sd.get(Calendar.DAY_OF_MONTH)),
					RHSCPreferences.get().getCourtSelection().getText(),
					RHSCPreferences.get().isIncludeBookings() ? "YES" : "NO",
					RHSCUser.get().getName()};
			RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
			bgTask.execute(parms);
		} else {
			Log.i("RHSCSelectedCourtTimesFragment","not logged on");
		}
		// use view.findViewById(id) to set values in the view
		
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == BOOK_SINGLES_COURT) {
	        // Make sure the request was successful
	        if (resultCode == android.app.Activity.RESULT_OK) {
	        	Log.i("return from book singles","court booked");
		    	Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
				String[] parms = { String.format("%d-%02d=%02d",sd.get(Calendar.YEAR) ,sd.get(Calendar.MONTH)+1,sd.get(Calendar.DAY_OF_MONTH)), 
						RHSCPreferences.get().getCourtSelection().getText(), 
						RHSCPreferences.get().isIncludeBookings()?"YES":"NO", 
						RHSCUser.get().getName() };
				RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
				bgTask.execute(parms);
	        }
	        if (resultCode == android.app.Activity.RESULT_CANCELED) {
	        	Log.i("return from book singles","court not booked");
	        }
	    }
	    if (requestCode == BOOK_DOUBLES_COURT) {
	        // Make sure the request was successful
	        if (resultCode == android.app.Activity.RESULT_OK) {
	        	Log.i("return from book doubles","court booked");
		    	Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
				String[] parms = { String.format("%d-%02d=%02d",sd.get(Calendar.YEAR) ,sd.get(Calendar.MONTH)+1,sd.get(Calendar.DAY_OF_MONTH)), 
						RHSCPreferences.get().getCourtSelection().getText(), 
						RHSCPreferences.get().isIncludeBookings()?"YES":"NO", 
						RHSCUser.get().getName() };
				RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
				bgTask.execute(parms);
	        }
	        if (resultCode == android.app.Activity.RESULT_CANCELED) {
	        	Log.i("return from book doubles","court not booked");
	        }
	    }
	}
	
	public void reload() {
		Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
		if (RHSCUser.get().isLoggedOn()) {
			String[] parms = {
					String.format("%d-%02d=%02d",
							sd.get(Calendar.YEAR),
							sd.get(Calendar.MONTH) + 1,
							sd.get(Calendar.DAY_OF_MONTH)),
					RHSCPreferences.get().getCourtSelection().getText(),
					RHSCPreferences.get().isIncludeBookings() ? "YES"
							: "NO", RHSCUser.get().getName() };
			RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
			bgTask.execute(parms);
		}
		if (listAdapter != null) {
			listAdapter.notifyDataSetChanged();
		}
	}

	public RHSCCourtTimeAdapter getListAdapter() {
		return listAdapter;
	}
	
	
	private class RHSCSelectCourtTimesTask extends AsyncTask<String, Void, String> {
		
		public URI getRequestURI(String scheddate,String courttype,String include,String uid) {
			String myURL = String.format("http://%s/Reserve/IOSTimesJSON.php?scheddate=%s&courttype=%s&include=%s&uid=%s",
					RHSCServer.get().getURL(), scheddate, courttype, include, uid);
			Log.i("SelectCourtTime",myURL);
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
	                        Log.i("SelectCourtTime", builder.toString()); //response data
	                        return builder.toString();
	                } else {
	                        Log.e("SelectCourtTime", "Failed to download file");
	                }
	        } catch (ClientProtocolException e) {
	        		Log.e("SelectCourtTime", "ClientProtocolException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	        } catch (IOException e) {
	            	Log.e("SelectCourtTime", "IOException on ".concat(targetURI.toString()));
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

	private class RHSCCancelCourtTimeTask extends AsyncTask<String, Void, String> {
		
		public URI getRequestURI(String[] parms) {
			String myURL = String.format("http://%s/Reserve/IOSCancelBookingJSON.php?b_id=%s&player1=%s&player2=%s&player3=%s&player4=%s&uid=%s&channel=%s",
						RHSCServer.get().getURL(), parms[0], parms[1], parms[2], parms[3], parms[4], RHSCUser.get().getName(),"aRHSCBook", parms[5]);
			Log.i("CancelCourtTime",myURL);
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
	    	URI targetURI = getRequestURI(parms);
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(targetURI);
	        try {
	                HttpResponse response = client.execute(httpGet);
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
	                        Log.i("CancelCourtTime", builder.toString()); //response data
	            			try {
		            			JSONObject jObj = new JSONObject(builder.toString());
		            			Log.i("cancel court result",jObj.has("success")?jObj.getString("success"):jObj.getString("error"));
		            			return jObj.has("success")?"success":"error";
	            			} catch (JSONException je) {
	            				return "error";
	            			}
	                } else {
	                        Log.e("Getter", "Failed to download file");
	                        return "error";
	                }
	        } catch (ClientProtocolException e) {
	        		Log.e("Getter", "ClientProtocolException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	                return "error";
	        } catch (IOException e) {
	            	Log.e("Getter", "IOException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	                return "error";
	        }
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
	    	if (result != null) {
	    		if (result.equals("success")) {
	    			// show message that booking was successful
	    			CharSequence text = "Court cancelled";
	    			int duration = Toast.LENGTH_LONG;
	    			Toast toast = Toast.makeText(getActivity(), text, duration);
	    			toast.show();
			    	Calendar sd = RHSCSelectedCourtTimesFragment.this.selectedDate;
					String[] parms = { String.format("%d-%02d=%02d",sd.get(Calendar.YEAR) ,sd.get(Calendar.MONTH)+1,sd.get(Calendar.DAY_OF_MONTH)), 
							RHSCPreferences.get().getCourtSelection().getText(), 
							RHSCPreferences.get().isIncludeBookings()?"YES":"NO", 
							RHSCUser.get().getName() };
					RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask();
					bgTask.execute(parms);
	    		} else {
	    			// show message that booking was not successful
	    			CharSequence text = "Court not cancelled - server error";
	    			int duration = Toast.LENGTH_LONG;
	    			Toast toast = Toast.makeText(getActivity(), text, duration);
	    			toast.show();
	    		}
    		} else {
    			// show message that booking was not successful
    			CharSequence text = "Court not cancelled - network error";
    			int duration = Toast.LENGTH_LONG;
    			Toast toast = Toast.makeText(getActivity(), text, duration);
    			toast.show();
	    	}
	    }

	}
	


}
