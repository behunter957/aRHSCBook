package com.rhsquashclub.arhscbook;

import com.google.gson.Gson;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;
import com.rhsquashclub.arhscbook.model.RHSCMember;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class BookSinglesActivity extends Activity {
	
	private RHSCCourtTime testCourt;
	
	static final int SELECT_PLAYER2 = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_singles);
		
		Intent intent = getIntent();
		String jsonCourt = intent.getStringExtra("court");
		Gson gson = new Gson();
		testCourt = gson.fromJson(jsonCourt, RHSCCourtTime.class);
//		testCourt.setEvent("Psych!");
		Log.i("BookSinglesActivity received",jsonCourt);
		Button bookButton = (Button) findViewById(R.id.bookButton);
		bookButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// first update the booking - http call
				// then return the updated court time? or just return to signal a refresh from server
				Intent returnIntent = new Intent();
//				Gson gson = new Gson();
//				returnIntent.putExtra("court",gson.toJson(testCourt));
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		});

		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("reason","cancelled by user"); // if http update failed then return "service failure"
				setResult(RESULT_CANCELED,returnIntent);
				finish();
			}
		});
		
		// intercept radio button select for player2
		RadioGroup rg = (RadioGroup) findViewById(R.id.player2);
		
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                case R.id.tbd2:
                    // TODO Something
                    break;
                case R.id.guest2:
                    // TODO Something
                    break;
                case R.id.member2:
                    // TODO Something
					Intent intent = new Intent(getApplicationContext(),
							BookSinglesActivity.class);
					startActivityForResult(intent,SELECT_PLAYER2);
                    break;
                }
            }
        });

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == SELECT_PLAYER2) {
	        // Make sure the request was successful
	        if (resultCode == android.app.Activity.RESULT_OK) {
	        	Gson gson = new Gson();
	        	RHSCMember player = gson.fromJson(data.getExtras().getString("player"), RHSCMember.class);
	        	Log.i("return from select player2",player.getName());
	        }
	        if (resultCode == android.app.Activity.RESULT_CANCELED) {
	        	Log.i("return from select player2",data.getExtras().getString("reason"));
	        }
	    }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_singles, menu);
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
