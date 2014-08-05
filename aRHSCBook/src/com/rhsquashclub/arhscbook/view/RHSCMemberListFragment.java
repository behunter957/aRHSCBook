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
import com.rhsquashclub.arhscbook.RHSCMain;
import com.rhsquashclub.arhscbook.model.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RHSCMemberListFragment extends Fragment {
	
	private RHSCMemberList members;
	private RHSCMemberAdapter adapter;
	
	public RHSCMemberAdapter getAdapter() {
		return adapter;
	}

	EditText inputSearch;

	public RHSCMemberListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
//		Log.i("RHSCMemberListFragment:onCreateView","entry");
		View view = inflater.inflate(R.layout.fragment_memberlist, container, false);

		getActivity().setTitle(R.string.members_title); 
		
		members = RHSCMemberList.get(getActivity());
		
		adapter = 
				new RHSCMemberAdapter(getActivity(), R.layout.member_list_item_row, members); 
		ListView lv = (ListView) view.findViewById(R.id.member_list_view);
		lv.setAdapter( adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Member List",
						String.format("item %d clicked", position));
				// Intent intent = new Intent(context, SendMessage.class);
				// String message = "abc";
				// intent.putExtra(EXTRA_MESSAGE, message);
				// startActivity(intent);
			}
		});		
		
		RHSCGetMemberListTask bgTask = new RHSCGetMemberListTask();
		Void[] parms = {};
		bgTask.execute(parms);

		inputSearch = (EditText) view.findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	RHSCMemberListFragment.this.getAdapter().getFilter().filter(cs);  
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                         
            }
        });		
		
		return view;
	}

	private class RHSCGetMemberListTask extends AsyncTask<Void, Void, String> {
		
		public URI getRequestURI() {
			String myURL = String.format("http://%s/Reserve/IOSMemberListJSON.php",
					RHSCServer.get().getURL());
			try {
				URI targetURI = new URI(myURL);
				return targetURI;
			} catch (URISyntaxException e) {
				Log.e("UIR Syntax Exception",e.toString());
				return null;
			}
		}
		
	    @Override
	    protected String doInBackground(Void... parms) {
	    	// no parms
	    	URI targetURI = getRequestURI();
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
	    	if (result != null) {
	           members.loadFromJSON(result);
	           adapter.notifyDataSetChanged();
	    	}
	    }

	}

}
