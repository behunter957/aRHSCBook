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
		this.selectedDate = selectedDate;
		this.includeBookings = includeBookings;
		this.clear();
	}

	public void reload() {
		if (!this.isSelected) return;
		this.clear();
	}
}
