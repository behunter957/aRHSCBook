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

import com.rhsquashclub.arhscbook.view.RHSCCourtTimeAdapter;
import com.rhsquashclub.arhscbook.view.RHSCMyBookingsAdapter;
import com.rhsquashclub.arhscbook.view.RHSCMyBookingsTask;
import com.rhsquashclub.arhscbook.view.RHSCSelectCourtTimesTask;

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

	public void loadFromServer(RHSCMyBookingsAdapter adapter, String[] parms) {
		// now start the background task
//		Log.i("RHSCMyBookings","loadFromserver");
		RHSCMyBookingsTask bgTask = new RHSCMyBookingsTask(this,adapter);
		bgTask.execute(parms);
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
