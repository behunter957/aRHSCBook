package com.rhsquashclub.arhscbook.model;

import java.util.Date;

import android.content.Context;

public class RHSCServer {
	
	public String getURL() {
		return URL;
	}

	private String URL;
	
	public void setURL(String uRL) {
		URL = uRL;
	}

	private static RHSCServer server;
	
	private RHSCServer(String URL) {
		this.URL = URL;
	}

	public static RHSCServer get() {
		if (server == null) {
			server = new RHSCServer(RHSCPreferences.get().getServerName());
		}
		return server;
	}
	
	public RHSCCourtTimeList getCourtTimes(Date selectedDate, boolean includeBookings) {
		return null;
	}
}
