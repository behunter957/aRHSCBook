package com.rhsquashclub.arhscbook.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.*;

import android.content.Intent;
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
import android.widget.ListView;

public class RHSCMyBookingsFragment extends Fragment {

	private RHSCMyBookings bookings;
	
	private RHSCMyBookingsAdapter listAdapter;

	public RHSCMyBookingsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
//		Log.i("RHSCSelectedCourtTimesFragment:onCreate","entry");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
//		Log.i("RHSCSelectedCourtTimesFragment:onCreateView","entry");
		View view = inflater.inflate(R.layout.fragment_mybookingslist, container, false);

		getActivity().setTitle(R.string.bookings_title); 
		
		bookings = RHSCMyBookings.get(getActivity());
		
		listAdapter = 
				new RHSCMyBookingsAdapter(getActivity(), R.layout.bookings_list_item_row,bookings);
		ListView lv = (ListView) view.findViewById(R.id.BookingsFragment);
		lv.setAdapter( listAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("MyBookings",
						String.format("item %d clicked", position));
				// Intent intent = new Intent(context, SendMessage.class);
				// String message = "abc";
				// intent.putExtra(EXTRA_MESSAGE, message);
				// startActivity(intent);
			}
		});		
		
		String[] parms = { "bhunter" };
		RHSCMyBookingsTask bgTask = new RHSCMyBookingsTask();
		bgTask.execute(parms);
		
		return view;
	}

	public RHSCMyBookingsAdapter getListAdapter() {
		return listAdapter;
	}

	private class RHSCMyBookingsTask extends AsyncTask<String, Void, String> {
		
		public URI getRequestURI(String uid) {
			String myURL = String.format("http://%s/Reserve/IOSMyBookingsJSON.php?uid=%s",
					RHSCServer.get().getURL(), uid);
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
	    	URI targetURI = getRequestURI(parms[0]);
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
//			Log.i("RHSCMyBookingsTask:postExecute",result);
	    	if (result != null) {
	            bookings.loadFromJSON(result,"bookings");
	            listAdapter.notifyDataSetChanged();
	    	}
	    }

	}
}
