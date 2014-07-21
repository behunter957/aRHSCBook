package com.rhsquashclub.arhscbook.model;

import java.util.Date;

public class RHSCServer {
	
	public String getURL() {
		return URL;
	}

	private String URL;
	
	private static RHSCServer server;
	
	private RHSCServer(String URL) {
		this.URL = URL;
	}

	public static RHSCServer get() {
		if (server == null) {
			server = new RHSCServer("www.bhsquashclub.com");
		}
		return server;
	}
	
	public RHSCCourtTimeList getCourtTimes(Date selectedDate, boolean includeBookings) {
		return null;
	}
}
