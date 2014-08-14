package com.rhsquashclub.arhscbook;

import com.google.gson.Gson;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;
import com.rhsquashclub.arhscbook.model.RHSCMember;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
		ImageButton emailButton = (ImageButton) findViewById(R.id.email_button);
		if (member.getEmail().equalsIgnoreCase("NULL")) {
			contactEmail.setText("");
			emailButton.setEnabled(false);
			emailButton.setVisibility(android.view.View.INVISIBLE);
		} else {
			contactEmail.setText(member.getEmail());
			emailButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// placeholder
					Log.i("ContactActivity","email button clicked");
					Intent i = new Intent(Intent.ACTION_SEND);
					i.setType("message/rfc822");
					i.putExtra(Intent.EXTRA_EMAIL  , new String[]{ member.getEmail() });
					i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
					i.putExtra(Intent.EXTRA_TEXT   , "body of email");
					try {
					    startActivity(Intent.createChooser(i, "Send mail..."));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(ContactActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
					}
//					Intent returnIntent = new Intent();
//					setResult(RESULT_CANCELED, returnIntent);
//					finish();
				}
			});
		}

		TextView contactPhone1 = (TextView) findViewById(R.id.contact_phone1);
		ImageButton phone1Button = (ImageButton) findViewById(R.id.phone1_button);
		ImageButton sms1Button = (ImageButton) findViewById(R.id.sms1_button);
		if (member.getPhone1().equalsIgnoreCase("NULL")) {
			contactPhone1.setText("");
			phone1Button.setEnabled(false);
			phone1Button.setVisibility(android.view.View.INVISIBLE);
			sms1Button.setEnabled(false);
			sms1Button.setVisibility(android.view.View.INVISIBLE);
		} else {
			contactPhone1.setText(member.getPhone1());
			phone1Button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// placeholder
					Log.i("ContactActivity","phone1 button clicked");
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"
		                        + member.getPhone2())));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(ContactActivity.this, "Telephony not available.", Toast.LENGTH_SHORT).show();
					}
//					Intent returnIntent = new Intent();
//					setResult(RESULT_CANCELED, returnIntent);
//					finish();
				}
			});
			sms1Button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// placeholder
					Log.i("ContactActivity","sms1 button clicked");
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
		                        + member.getPhone1())));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(ContactActivity.this, "Telephony not available.", Toast.LENGTH_SHORT).show();
					}
//					Intent returnIntent = new Intent();
//					setResult(RESULT_CANCELED, returnIntent);
//					finish();
				}
			});
		}

		TextView contactPhone2 = (TextView) findViewById(R.id.contact_phone2);
		ImageButton phone2Button = (ImageButton) findViewById(R.id.phone2_button);
		ImageButton sms2Button = (ImageButton) findViewById(R.id.sms2_button);
		if (member.getPhone2().equalsIgnoreCase("NULL")) {
			contactPhone2.setText("");
			phone2Button.setEnabled(false);
			phone2Button.setVisibility(android.view.View.INVISIBLE);
			sms2Button.setEnabled(false);
			sms2Button.setVisibility(android.view.View.INVISIBLE);
		} else {
			contactPhone2.setText(member.getPhone1());
			phone2Button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// placeholder
					Log.i("ContactActivity","phone2 button clicked");
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"
		                        + member.getPhone2())));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(ContactActivity.this, "Telephony not available.", Toast.LENGTH_SHORT).show();
					}
//					Intent returnIntent = new Intent();
//					setResult(RESULT_CANCELED, returnIntent);
//					finish();
				}
			});
			sms2Button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// placeholder
					Log.i("ContactActivity","sms2 button clicked");
					try {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
		                        + member.getPhone2())));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(ContactActivity.this, "Telephony not available.", Toast.LENGTH_SHORT).show();
					}
//					Intent returnIntent = new Intent();
//					setResult(RESULT_CANCELED, returnIntent);
//					finish();
				}
			});
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
