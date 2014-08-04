package com.rhsquashclub.arhscbook.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RHSCCourtTime {
	
	private String bookingId;
	private String court;
	private Date courtTime;
	private String status;
	private String event;
	private String[] player_id;
	private String[] player_lname;
	
	public String getBookingId() {
		return bookingId;
	}

	public String getCourt() {
		return court;
	}

	public Date getCourtTime() {
		return courtTime;
	}

	public String getStatus() {
		return status;
	}

	public String getEvent() {
		return event;
	}

	public String[] getPlayer_id() {
		return player_id;
	}

	public String[] getPlayer_lname() {
		return player_lname;
	}
	
	public String getCourtAndTime() {
		String datepart = new SimpleDateFormat("h:mm a", 
				Locale.ENGLISH).format(courtTime);
		return court.concat(" - ").concat(datepart);
	}
	
	public String getCourtAndDateTime() {
		String datepart = new SimpleDateFormat("EEE, MMM d 'at' h:mm a", 
				Locale.ENGLISH).format(courtTime);
		return court.concat(" - ").concat(datepart);
	}
	
	public String getEventAndPlayers() {
		String res = event.concat(" - ").concat(player_lname[0]).concat(",").concat(player_lname[1]);
		if (court.equals("Court 5")) {
			res = res.concat(",").concat(player_lname[2]).concat(",").concat(player_lname[3]);
		}
		return res;
	}

	public RHSCCourtTime(JSONObject jObj) {
		try {
			this.bookingId = jObj.has("booking_id")?jObj.getString("booking_id"):"";
			this.court = jObj.has("court")?jObj.getString("court"):"";
			String datePart = jObj.has("courtdate")?jObj.getString("courtdate"):"";
			String timePart = jObj.has("courttime")?jObj.getString("courttime"):"";
			try {
				this.courtTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", 
					Locale.ENGLISH).parse(datePart.concat(" ").concat(timePart));
			} catch (ParseException pe) {
				Log.e("ParseException on date",pe.toString());
				this.courtTime = new Date();
			}
			this.status = jObj.has("courtStatus")?jObj.getString("courtStatus"):"";
			this.event = jObj.has("courtEvent")?jObj.getString("courtEvent"):"";
			this.player_id = new String[4];
			this.player_id[0] = jObj.has("player1_id")?jObj.getString("player1_id"):"";
			this.player_id[1] = jObj.has("player2_id")?jObj.getString("player2_id"):"";
			this.player_id[2] = jObj.has("player3_id")?jObj.getString("player3_id"):"";
			this.player_id[3] = jObj.has("player4_id")?jObj.getString("player4_id"):"";
			this.player_lname = new String[4];
			this.player_lname[0] = jObj.has("player1_lname")?jObj.getString("player1_lname"):"";
			this.player_lname[1] = jObj.has("player2_lname")?jObj.getString("player2_lname"):"";
			this.player_lname[2] = jObj.has("player3_lname")?jObj.getString("player3_lname"):"";
			this.player_lname[3] = jObj.has("player4_lname")?jObj.getString("player4_lname"):"";
		} catch (JSONException je) {
		}
	}

	public String toString() {
		String res = this.court.concat(" - ").concat(new SimpleDateFormat("MMMM d, yyyy h:mm a", 
				Locale.ENGLISH).format(this.courtTime));
		return res;
	}
	
	public RHSCCourtTime(String bookingId,String court,String dateMMMMdyyyy,String timehmma) {
		this.bookingId = bookingId;
		this.court = court;
		try {
			this.courtTime = new SimpleDateFormat("MMMM d, yyyy h:mm a", 
				Locale.ENGLISH).parse(dateMMMMdyyyy.concat(" ").concat(timehmma));
		} catch (ParseException pe) {
			this.courtTime = new Date();
		}
		this.status = "Available";
		this.event = "Friendly";
		this.player_id = new String[4];
		this.player_id[0] = "";
		this.player_id[1] = "";
		this.player_id[2] = "";
		this.player_id[3] = "";
		this.player_lname = new String[4];
		this.player_lname[0] = "";
		this.player_lname[1] = "";
		this.player_lname[2] = "";
		this.player_lname[3] = "";		
	}
}
