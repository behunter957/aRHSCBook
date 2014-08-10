package com.rhsquashclub.arhscbook;

import com.rhsquashclub.arhscbook.model.*;
import com.rhsquashclub.arhscbook.view.*;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RHSCMain extends ActionBarActivity {

	public static RHSCSelectedCourtTimesFragment courtListFragment = new RHSCSelectedCourtTimesFragment(); 
	public static RHSCMyBookingsFragment myBookingsFragment = new RHSCMyBookingsFragment(); 
	public static RHSCMemberListFragment memberListFragment = new RHSCMemberListFragment(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rhscmain);

		RHSCSelectedCourtTimes.get(this.getApplicationContext());
		RHSCMyBookings.get(this.getApplicationContext());
		RHSCMemberList.get(this.getApplicationContext());

		// show courts view by default
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction()
		.replace(R.id.TabViewFragment,
				RHSCMain.courtListFragment).commit();

	    ConnectivityManager connMgr = (ConnectivityManager) 
	            getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	        if (networkInfo != null && networkInfo.isConnected()) {
	            // fetch data
	        } else {
	            // display error
	        }

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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
