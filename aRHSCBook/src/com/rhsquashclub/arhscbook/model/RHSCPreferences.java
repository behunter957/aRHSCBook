package com.rhsquashclub.arhscbook.model;

import com.rhsquashclub.arhscbook.RHSCMain;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;

public class RHSCPreferences {
	
	private static RHSCPreferences data = null;
	
	private String serverName;
	private String userid;
	private String password;
	private boolean includeBookings;
	private RHSCCourtSelection courtSelection;
	
	private RHSCPreferences() {
		serverName = "www.bhsquashclub.com";
		userid = "userid";
		password = "password";
		includeBookings = true;
		courtSelection = RHSCCourtSelection.All;
	}
	
	public static RHSCPreferences get() {
		if (data == null) {
			data = new RHSCPreferences();
		}
		return data;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isIncludeBookings() {
		return includeBookings;
	}

	public void setIncludeBookings(boolean includeBookings) {
		this.includeBookings = includeBookings;
	}

	public RHSCCourtSelection getCourtSelection() {
		return courtSelection;
	}

	public void setCourtSelection(RHSCCourtSelection courtSelection) {
		this.courtSelection = courtSelection;
	}

}
