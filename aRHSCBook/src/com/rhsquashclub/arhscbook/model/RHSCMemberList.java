package com.rhsquashclub.arhscbook.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rhsquashclub.arhscbook.RHSCMain;
import com.rhsquashclub.arhscbook.view.RHSCCourtTimeAdapter;
import com.rhsquashclub.arhscbook.view.RHSCMemberAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RHSCMemberList extends ArrayList<RHSCMember> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static RHSCMemberList memList;
	private static RHSCMember TBD;
	private static RHSCMember GUEST;
	
	private RHSCMemberAdapter adapter;
	private boolean notifyFragment = false;
	
	public RHSCMemberList() {
		super();
	}
	
	public static RHSCMemberList get(Context c) {
		if (memList == null) {
			memList = new RHSCMemberList();
		}
		return memList;
	}
	
	public void reload(RHSCMemberAdapter adapter) {
		this.adapter = adapter;
		notifyFragment = true;
		if (RHSCUser.get().isLoggedOn()) {
			RHSCGetMemberListTask bgTask = memList.new RHSCGetMemberListTask();
			Void[] parms = {};
			bgTask.execute(parms);
			RHSCMain.retryMemberLoad = false;
		} else {
			RHSCMain.retryMemberLoad = true;
			Log.i("RHSCmemberList","not logged on");
		}
	}
	
	public void load() {
		notifyFragment = false;
		if (RHSCUser.get().isLoggedOn()) {
			RHSCGetMemberListTask bgTask = memList.new RHSCGetMemberListTask();
			Void[] parms = {};
			bgTask.execute(parms);
			RHSCMain.retryMemberLoad = false;
		} else {
			RHSCMain.retryMemberLoad = true;
			Log.i("RHSCmemberList","not logged on");
		}
	}
	
	public static RHSCMember tbd() {
		if (TBD == null) {
			TBD = new RHSCMember("TBD","TBD");
		}
		return TBD;
	}

	public static RHSCMember guest() {
		if (GUEST == null) {
			GUEST = new RHSCMember("Guest","Guest");
		}
		return GUEST;
	}

	public RHSCMemberList loadFromJSON(String jsonString) {
		this.clear();
		try {
			JSONObject jObj = new JSONObject(jsonString);
			JSONArray jArr = jObj.getJSONArray("members");
			for (int i = 0; i < jArr.length(); i++) {
				this.add(new RHSCMember(jArr.getJSONObject(i)));
			}
		} catch (JSONException je) {
		}
		return this;
	}

	public RHSCMemberList testSampleSelected() {
		this.clear();
		String[] mems = {"Bruce Hunter","Tony Harris","Danny Paolucci"};
		for (String crt : mems) {
			this.add(new RHSCMember(crt,"Active"));
		}
		return this;
	}
	
	private class RHSCGetMemberListTask extends AsyncTask<Void, Void, String> {
		
		public URI getRequestURI() {
			String myURL = String.format("http://%s/Reserve20/IOSMemberListJSON.php",
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
	           memList.loadFromJSON(result);
	           if (notifyFragment) {
	        	   adapter.notifyDataSetChanged();
	           }
	    	}
	    }

	}

}
