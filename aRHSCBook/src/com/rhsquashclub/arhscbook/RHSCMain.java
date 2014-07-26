package com.rhsquashclub.arhscbook;

import com.rhsquashclub.arhscbook.model.RHSCMemberList;
import com.rhsquashclub.arhscbook.model.RHSCSelectedCourtTimes;
import com.rhsquashclub.arhscbook.view.RHSCMemberListFragment;
import com.rhsquashclub.arhscbook.view.RHSCSelectedCourtTimesFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RHSCMain extends ActionBarActivity {

	public static Fragment courtListFragment = new RHSCSelectedCourtTimesFragment(); 
	public static Fragment memberListFragment = new RHSCMemberListFragment(); 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RHSCSelectedCourtTimes.get(this.getApplicationContext()).testSampleSelected();
		RHSCMemberList.get(this.getApplicationContext()).testSampleSelected();
		setContentView(R.layout.activity_rhscmain);
		RadioGroup rg = (RadioGroup) findViewById(R.id.tabbar);
		rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			   public void onCheckedChanged(RadioGroup group,
                       int checkedId) {
				   Context context = getApplicationContext();
				   FragmentManager fm = getSupportFragmentManager(); 
                   Toast toast;
                   switch (checkedId) { //set the Model to hold the answer the user picked
                   case R.id.rad_courts:
                	   toast = Toast.makeText(context, "Courts selected", Toast.LENGTH_SHORT);
                	   toast.show();
               			fm.beginTransaction().replace(R.id.TabViewFragment, RHSCMain.courtListFragment).commit(); 
                       break;
                   case R.id.rad_bookings:
                		toast = Toast.makeText(context, "Bookings selected", Toast.LENGTH_SHORT);
                		toast.show();
               			fm.beginTransaction().replace(R.id.TabViewFragment, RHSCMain.courtListFragment).commit(); 
                       break;
                   case R.id.rad_members:
                	   toast = Toast.makeText(context, "Members selected", Toast.LENGTH_SHORT);
                	   toast.show();
              			fm.beginTransaction().replace(R.id.TabViewFragment, RHSCMain.memberListFragment).commit(); 
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
