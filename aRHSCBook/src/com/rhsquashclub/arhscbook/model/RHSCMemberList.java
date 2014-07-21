package com.rhsquashclub.arhscbook.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	public static RHSCMemberList get() {
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

	public void loadFromJSON(String jsonString) {
		try {
			JSONObject jObj = new JSONObject(jsonString);
			JSONArray jArr = jObj.getJSONArray("members");
			for (int i = 0; i < jArr.length(); i++) {
				this.add(new RHSCMember(jArr.getJSONObject(i)));
			}
		} catch (JSONException je) {
		}
	}

}
