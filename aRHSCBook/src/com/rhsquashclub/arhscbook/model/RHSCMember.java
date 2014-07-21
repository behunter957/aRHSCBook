package com.rhsquashclub.arhscbook.model;

import org.json.JSONObject;
import org.json.JSONException;

public class RHSCMember {
	
	protected String name;
	protected String lastName;
	protected String firstName;
	protected String status;
	protected String type;
	protected String email;
	protected String phone1;
	protected String phone2;

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getStatus() {
		return status;
	}

	public String getType() {
		return type;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone1() {
		return phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public RHSCMember() {
		
	}

	public RHSCMember(String name, String type) {
		this.name = name;
		this.lastName = name;
		this.firstName = name;
		this.status = "Active";
		this.type = type;
		this.email = "";
		this.phone1 = "";
		this.phone2 = "";
	}
	
	public RHSCMember(JSONObject jObj) {
		try {
			this.name = jObj.has("id")?jObj.getString("id"):"";
			this.lastName = jObj.has("lname")?jObj.getString("lname"):"";
			this.firstName = jObj.has("fname")?jObj.getString("fname"):"";
			this.status = jObj.has("status")?jObj.getString("status"):"";
			this.type = jObj.has("member_type")?jObj.getString("member_type"):"";
			this.email = jObj.has("email")?jObj.getString("email"):"";
			this.phone1 = jObj.has("primary_phone")?jObj.getString("primary_phone"):"";
			this.phone2 = jObj.has("home_phone")?jObj.getString("home_phone"):"";
		} catch (JSONException je) {
			//
		}
	}
}
