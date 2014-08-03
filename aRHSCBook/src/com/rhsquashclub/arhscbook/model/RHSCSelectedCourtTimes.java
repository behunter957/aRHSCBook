package com.rhsquashclub.arhscbook.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rhsquashclub.arhscbook.view.RHSCCourtTimeAdapter;
import com.rhsquashclub.arhscbook.view.RHSCSelectCourtTimesTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RHSCSelectedCourtTimes extends RHSCCourtTimeList {

	private static RHSCSelectedCourtTimes sSelectedCourtTimes;
	private Context mAppContext;
	private static final long serialVersionUID = 1L;
	
	private Date selectedDate;
	private boolean includeBookings;
	private boolean isSelected = false;

	private RHSCSelectedCourtTimes(Context appContext) {
		super();
		mAppContext = appContext;
	}
	
	public static RHSCSelectedCourtTimes get( Context c) { 
		if (sSelectedCourtTimes == null) { 
			sSelectedCourtTimes = new RHSCSelectedCourtTimes( c.getApplicationContext()); 
		} 
		return sSelectedCourtTimes; 
	}
	
	public void select(Date selectedDate,boolean includeBookings) {
		this.setSelectedDate(selectedDate);
		this.setIncludeBookings(includeBookings);
		this.clear();
	}

	public void reload() {
		if (!this.isSelected) return;
		this.clear();
	}

	public boolean isIncludeBookings() {
		return includeBookings;
	}

	public void setIncludeBookings(boolean includeBookings) {
		this.includeBookings = includeBookings;
	}

	public Date getSelectedDate() {
		return selectedDate;
	}

	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	public void loadFromServer(RHSCCourtTimeAdapter adapter, String[] parms) {
		// now start the background task
		Log.i("RHSCSelectedCourtTimes","loadFromserver");
		
		RHSCSelectCourtTimesTask bgTask = new RHSCSelectCourtTimesTask(this,adapter);
//		String[] parms = { "2014-08-02", "All", "YES", "bhunter" };
		bgTask.execute(parms);
	}
	
	public RHSCSelectedCourtTimes testSampleSelected() {
		this.clear();
		String[] courts = {"Court 1","Court 2"};
		int j = 0;
		for (String crt : courts) {
			try {
				Date courtTime = new SimpleDateFormat("MMMM d, yyyy h:mm a",
						Locale.ENGLISH).parse("July 22, 2014 1:30 pm");
				for (int i = 0; i < 10; i++) {
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
				Log.e("load sample", e.getLocalizedMessage());
			}
			j += 1;
		}
		return this;
	}
}
