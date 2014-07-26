package com.rhsquashclub.arhscbook.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
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
