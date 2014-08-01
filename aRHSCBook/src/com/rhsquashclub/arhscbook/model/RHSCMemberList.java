package com.rhsquashclub.arhscbook.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RHSCMemberList extends ArrayList<RHSCMember> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static RHSCMemberList memList;
	private static RHSCMember TBD;
	private static RHSCMember GUEST;
	
	private RHSCMemberList() {
		super();
	}
	
	public static RHSCMemberList get(Context c) {
		if (memList == null) {
			memList = new RHSCMemberList();
		}
		return memList;
	}
	
	public static RHSCMember tbd() {
		if (TBD == null) {
			TBD = new RHSCMember("TBD","TBD");
		}
		return TBD;
	}

	public static RHSCMember guest() {
		if (GUEST == null) {
			GUEST = new RHSCMember("Guest","Guest");
		}
		return GUEST;
	}

	public RHSCMemberList loadFromJSON(String jsonString) {
		try {
			JSONObject jObj = new JSONObject(jsonString);
			JSONArray jArr = jObj.getJSONArray("members");
			for (int i = 0; i < jArr.length(); i++) {
				this.add(new RHSCMember(jArr.getJSONObject(i)));
			}
		} catch (JSONException je) {
		}
		return this;
	}

	public RHSCMemberList loadFromServer(RHSCServer srvr) {
		return this;
	}
	
	public RHSCMemberList testSampleSelected() {
		this.clear();
		String[] mems = {"Bruce Hunter","Tony Harris","Danny Paolucci"};
		for (String crt : mems) {
			this.add(new RHSCMember(crt,"Active"));
		}
		return this;
	}

}
