package com.rhsquashclub.arhscbook.model;

import java.util.Date;

public class RHSCSelectedCourtTimes extends RHSCCourtTimeList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date selectedDate;
	private boolean includeBookings;
	private boolean isSelected = false;

	public RHSCSelectedCourtTimes() {
		// TODO Auto-generated constructor stub
		super();
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
}
