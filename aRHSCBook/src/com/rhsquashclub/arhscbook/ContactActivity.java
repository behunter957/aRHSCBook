package com.rhsquashclub.arhscbook;

import com.google.gson.Gson;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;
import com.rhsquashclub.arhscbook.model.RHSCMember;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

public class ContactActivity extends Activity {
	
	private RHSCMember member;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		Intent intent = getIntent();
		String jsonCourt = intent.getStringExtra("member");
		Gson gson = new Gson();
		member = gson.fromJson(jsonCourt, RHSCMember.class);
		TextView contactName = (TextView) findViewById(R.id.contact_name);
		contactName.setText(member.getDisplayName());
		TextView contactEmail = (TextView) findViewById(R.id.contact_email);
		if (member.getEmail().equalsIgnoreCase("NULL")) {
			contactEmail.setText("");
			ImageButton emailButton = (ImageButton) findViewById(R.id.email_button);
			emailButton.setEnabled(false);
			emailButton.setVisibility(android.view.View.INVISIBLE);
		} else {
			contactEmail.setText(member.getEmail());
		}

		TextView contactPhone1 = (TextView) findViewById(R.id.contact_phone1);
		if (member.getPhone1().equalsIgnoreCase("NULL")) {
			contactPhone1.setText("");
			ImageButton phone1Button = (ImageButton) findViewById(R.id.phone1_button);
			phone1Button.setEnabled(false);
			phone1Button.setVisibility(android.view.View.INVISIBLE);
			ImageButton sms1Button = (ImageButton) findViewById(R.id.sms1_button);
			sms1Button.setEnabled(false);
			sms1Button.setVisibility(android.view.View.INVISIBLE);
		} else {
			contactPhone1.setText(member.getPhone1());
		}

		TextView contactPhone2 = (TextView) findViewById(R.id.contact_phone2);
		if (member.getPhone2().equalsIgnoreCase("NULL")) {
			contactPhone2.setText("");
			ImageButton phone2Button = (ImageButton) findViewById(R.id.phone2_button);
			phone2Button.setEnabled(false);
			phone2Button.setVisibility(android.view.View.INVISIBLE);
			ImageButton sms2Button = (ImageButton) findViewById(R.id.sms2_button);
			sms2Button.setEnabled(false);
			sms2Button.setVisibility(android.view.View.INVISIBLE);
		} else {
			contactPhone1.setText(member.getPhone1());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
