package com.rhsquashclub.arhscbook.model;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rhsquashclub.arhscbook.RHSCMain;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class RHSCUser extends RHSCMember {
	
	private static RHSCUser user;
	private RHSCMain main = null;
	private boolean callMain = false;
	
	private boolean loggedOn;
	
	private RHSCUser() {
		super();
		loggedOn = false;
	}
	
	public boolean isLoggedOn() {
		return loggedOn;
	}

	public static RHSCUser get() {
		if (user == null) {
			user = new RHSCUser();
		}
		return user;
	}

	public void authenticate() {
		callMain = false;
		RHSCLogonTask bgTask = new RHSCLogonTask();
		bgTask.execute();
	}
	
	public void authenticate(RHSCMain main) {
		this.main = main;
		callMain = true;
		RHSCLogonTask bgTask = new RHSCLogonTask();
		bgTask.execute();
	}
	
	private class RHSCLogonTask extends AsyncTask<Void, Void, String> {
		
		public URI getRequestURI() {
			String myURL = String.format("http://%s/Reserve20/IOSUserLogonJSON.php?uid=%s&pwd=%s",
					RHSCServer.get().getURL(),RHSCPreferences.get().getUserid(),
					RHSCPreferences.get().getPassword());
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
				try {
					Log.i("RHSCUser", result);
					JSONObject jObj = new JSONObject(result);
					if (jObj.has("user")) {
						JSONArray jArr = jObj.getJSONArray("user");
						JSONObject jUser = jArr.getJSONObject(0);
						Log.i("RHSCUser", "authenticated");
						loadFromJSON(jUser);
						loggedOn = true;
						if (callMain) {
							main.reload();
						}
					} else {
						Log.i("RHSCUser", "NOT authenticated");
						loggedOn = false;
					}
					// TODO deal with errors and populate the user member
				} catch (JSONException je) {
					Log.i("RHSCUser", "JSON error");
				}
			}
		}

	}

}
