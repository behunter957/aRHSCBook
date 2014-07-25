package com.rhsquashclub.arhscbook.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

abstract class RHSCCourtTimeList extends ArrayList<RHSCCourtTime> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RHSCCourtTimeList() {
		super();
	}
	
	protected void loadFromJSON(String jsonString) {
		try {
			JSONObject jObj = new JSONObject(jsonString);
			JSONArray jArr = jObj.getJSONArray("courtTimes");
			for (int i = 0; i < jArr.length(); i++) {
				this.add(new RHSCCourtTime(jArr.getJSONObject(i)));
			}
		} catch (JSONException je) {
		}
	}
}
