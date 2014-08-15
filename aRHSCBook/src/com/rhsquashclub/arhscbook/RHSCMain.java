package com.rhsquashclub.arhscbook;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.rhsquashclub.arhscbook.model.*;
import com.rhsquashclub.arhscbook.view.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RHSCMain extends ActionBarActivity {

	public static RHSCSelectedCourtTimesFragment courtListFragment = new RHSCSelectedCourtTimesFragment();
	public static RHSCMyBookingsFragment myBookingsFragment = new RHSCMyBookingsFragment();
	public static RHSCMemberListFragment memberListFragment = new RHSCMemberListFragment();
	public static boolean isLoggedOn = true; // TODO set to false once logon code is in place

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rhscmain);
		PreferenceManager.setDefaultValues(this, R.xml.settings, false);
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		RHSCPreferences.get().setServerName(sharedPref.getString("serverHostname", ""));		
		RHSCPreferences.get().setUserid(sharedPref.getString("userid", ""));		
		RHSCPreferences.get().setPassword(sharedPref.getString("password", ""));
		Log.i("selectCourts",sharedPref.getString("selectCourts", ""));
		RHSCPreferences.get().setCourtSelection(RHSCCourtSelection.valueOf(sharedPref.getString("selectCourts", "All")));		
		RHSCPreferences.get().setIncludeBookings(sharedPref.getBoolean("includeBookings", false));		

		RHSCSelectedCourtTimes.get(this.getApplicationContext());
		RHSCMyBookings.get(this.getApplicationContext());
		RHSCMemberList.get(this.getApplicationContext());

		// show courts view by default
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction()
				.replace(R.id.TabViewFragment, RHSCMain.courtListFragment)
				.commit();

		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			RadioGroup rg = (RadioGroup) findViewById(R.id.tabbar);
			rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// Context context = getApplicationContext();
					// Toast toast;
					FragmentManager fm = getSupportFragmentManager();
					switch (checkedId) { // set the Model to hold the answer the
											// user picked
					case R.id.rad_courts:
						// toast = Toast.makeText(context, "Courts selected",
						// Toast.LENGTH_SHORT);
						// toast.show();
						fm.beginTransaction()
								.replace(R.id.TabViewFragment,
										RHSCMain.courtListFragment).commit();
						break;
					case R.id.rad_bookings:
						// toast = Toast.makeText(context, "Bookings selected",
						// Toast.LENGTH_SHORT);
						// toast.show();
						fm.beginTransaction()
								.replace(R.id.TabViewFragment,
										RHSCMain.myBookingsFragment).commit();
						break;
					case R.id.rad_members:
						// toast = Toast.makeText(context, "Members selected",
						// Toast.LENGTH_SHORT);
						// toast.show();
						fm.beginTransaction()
								.replace(R.id.TabViewFragment,
										RHSCMain.memberListFragment).commit();
						break;
					default:
					}

				}
			});

		} else {
			// display error
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder.setTitle("Network not available");

			// set dialog message
			alertDialogBuilder
					.setMessage("Network is not available. Try when connected.")
					.setCancelable(false)
					.setPositiveButton("Dismiss",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, close
									// current activity
									RHSCMain.this.finish();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rhscmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this,
					SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
