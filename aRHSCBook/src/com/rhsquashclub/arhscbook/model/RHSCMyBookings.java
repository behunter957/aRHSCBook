package com.rhsquashclub.arhscbook.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
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

import android.content.Context;
import android.util.Log;

public class RHSCMyBookings extends RHSCCourtTimeList {
	
	private static RHSCMyBookings sMyBookings;
	private Context mAppContext;
	private static final long serialVersionUID = 1L;
	
	private RHSCMyBookings(Context appContext) {
		super();
		mAppContext = appContext;
	}
	
	public static RHSCMyBookings get( Context c) { 
		if (sMyBookings == null) { 
			sMyBookings = new RHSCMyBookings( c.getApplicationContext()); 
		} 
		return sMyBookings; 
	}
	
	public void reload() {
		
	}

	public String getMyBookings() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(RHSCServer.get().getURL()
				.concat("/Reserve/IOSMyBookingsJSON.php?uid=")
				.concat(RHSCUser.get().getName()));
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
			} else {
				Log.e(RHSCMyBookings.class.toString(),
						"Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	public RHSCMyBookings testSampleSelected() {
		this.clear();
		String[] courts = {"Court 1","Court 2"};
		int j = 0;
		for (String crt : courts) {
			try {
				Date courtTime = new SimpleDateFormat("MMMM d, yyyy h:mm a",
						Locale.ENGLISH).parse("July 22, 2014 1:30 pm");
				for (int i = 0; i < 2; i++) {
					String datePart = new SimpleDateFormat(
							"MMMM d, yyyy", Locale.ENGLISH)
							.format(courtTime);
					String timePart = new SimpleDateFormat(
							"h:mm a", Locale.ENGLISH)
							.format(courtTime);
					String bookId = String.format("%4d", (i * courts.length) + j);
					Log.i("adding court time", bookId.concat(":").concat(crt));
					this.add(new RHSCCourtTime(bookId,crt,datePart,timePart));
					Calendar cal = Calendar.getInstance();
					cal.setTime(courtTime);
					cal.add(Calendar.MINUTE, 40);
					courtTime = cal.getTime();
				}
			} catch (Exception e) {
				Log.e("load sample bookings", e.getLocalizedMessage());
			}
			j += 1;
		}
		return this;
	}
	
}
