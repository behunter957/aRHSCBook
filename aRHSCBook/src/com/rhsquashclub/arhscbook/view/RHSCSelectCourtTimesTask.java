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

import com.rhsquashclub.arhscbook.model.RHSCSelectedCourtTimes;
import com.rhsquashclub.arhscbook.model.RHSCServer;

import android.os.AsyncTask;
import android.util.Log;

public class RHSCSelectCourtTimesTask extends AsyncTask<String, Void, String> {
	
	private RHSCCourtTimeAdapter adapter;
	private RHSCSelectedCourtTimes courts;
	
	public RHSCSelectCourtTimesTask(RHSCSelectedCourtTimes courts, RHSCCourtTimeAdapter adapter) {
		this.adapter = adapter;
		this.courts = courts;
	}

	public URI getRequestURI(String scheddate,String courttype,String include,String uid) {
		String myURL = String.format("http://%s/Reserve/IOSTimesJSON.php?scheddate=%s&courttype=%s&include=%s&uid=%s",
				RHSCServer.get().getURL(), scheddate, courttype, include, uid);
		try {
			URI targetURI = new URI(myURL);
			return targetURI;
		} catch (URISyntaxException e) {
			Log.e("UIR Syntax Exception",e.toString());
			return null;
		}
	}
	
    @Override
    protected String doInBackground(String... parms) {
    	// parm 1 is scheddate
    	// parm 2 is courttype
    	// parm 3 is include (YES/NO)
    	// parm 4 is uid
    	URI targetURI = getRequestURI(parms[0],parms[1],parms[2],parms[3]);
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
//                        Log.v("Getter", "Your data: " + builder.toString()); //response data
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
            this.courts.loadFromJSON(result);
            this.adapter.notifyDataSetChanged();
    	}
    }

}
