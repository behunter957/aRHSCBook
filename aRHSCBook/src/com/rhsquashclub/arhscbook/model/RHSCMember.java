package com.rhsquashclub.arhscbook.model;

import org.json.JSONObject;
import org.json.JSONException;

import com.google.gson.annotations.Expose;

public class RHSCMember {
	
	@Expose
	protected String name;
	@Expose
	protected String lastName;
	@Expose
	protected String firstName;
	@Expose
	protected String status;
	@Expose
	protected String type;
	@Expose
	protected String email;
	@Expose
	protected String phone1;
	@Expose
	protected String phone2;

	public void setName(String name) {
		this.name = name;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getDisplayName() {
		return firstName.concat(" ").concat(lastName);
	}

	public String getSortName() {
		return lastName.concat(", ").concat(firstName);
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
	
	public void loadFromJSON(JSONObject jObj) {
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
	
	public String toString() {
		return this.firstName.concat(" ").concat(this.lastName);
	}
}
