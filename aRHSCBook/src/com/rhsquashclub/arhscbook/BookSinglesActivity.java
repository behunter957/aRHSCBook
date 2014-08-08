package com.rhsquashclub.arhscbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.rhsquashclub.arhscbook.model.RHSCCourtSelection;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;
import com.rhsquashclub.arhscbook.model.RHSCMember;
import com.rhsquashclub.arhscbook.model.RHSCPreferences;
import com.rhsquashclub.arhscbook.model.RHSCServer;
import com.rhsquashclub.arhscbook.model.RHSCUser;
import com.rhsquashclub.arhscbook.view.RHSCSelectedCourtTimesFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class BookSinglesActivity extends Activity {
	
	private RHSCCourtTime targetCourt;
	private ArrayAdapter<CharSequence> spinnerAdapter;
	
	static final int SELECT_PLAYER2 = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_singles);
		
		Intent intent = getIntent();
		String jsonCourt = intent.getStringExtra("court");
		Gson gson = new Gson();
		targetCourt = gson.fromJson(jsonCourt, RHSCCourtTime.class);

		// TODO first lock the court
//		if (!lockCourt(targetCourt.getBookingId(),RHSCUser.get().getName())) {
//			// return afterdisplaying error messahe
//		}
		// then populate the view
		String ctdText = String.format("%s on %s", targetCourt.getCourt(),new SimpleDateFormat(
				"EEEE, MMMM d", Locale.ENGLISH)
				.format(targetCourt.getCourtTime()));
		TextView courtDesc = (TextView) findViewById(R.id.courtTimeDesc);
		courtDesc.setText(ctdText);
		
		// TODO populate the spinner for event type
		Spinner eventSpinner = (Spinner) findViewById(R.id.eventSpinner1);
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.singles_event_array, R.layout.spinner_item);
		eventSpinner.setAdapter(spinnerAdapter);
		eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        // your code here
		    	Log.i("spinner selected",spinnerAdapter.getItem(position).toString());
		    	targetCourt.setEvent(spinnerAdapter.getItem(position).toString());
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		
		Log.i("BookSinglesActivity received",jsonCourt);
		Button bookButton = (Button) findViewById(R.id.bookButton);
		bookButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// first update the booking - http call
				// then return signal a refresh from server
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
                	Log.i("BookSinglesActivity","button for member clicked");
					Intent intent = new Intent(getApplicationContext(),
							PickPlayerActivity.class);
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
	        	RadioButton playerButton = (RadioButton) findViewById(R.id.member2);
	        	playerButton.setText(player.getDisplayName());
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

	private boolean lockCourt(String booking,String uid) {
		String myURL = String.format("http://%s/Reserve/IOSLockBookingJSON.php?booking_id=%s&uid=%s",
				RHSCServer.get().getURL(), booking, uid);
		try {
			URI targetURI = new URI(myURL);
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(targetURI);
	        try {
	                HttpResponse response = client.execute(httpGet);
	                StatusLine statusLine = response.getStatusLine();
	                int statusCode = statusLine.getStatusCode();
	                if (statusCode == 200) {
	                        HttpEntity entity = response.getEntity();
	                        InputStream content = entity.getContent();
	                        BufferedReader reader = new BufferedReader(
	                                        new InputStreamReader(content));
	                        String line;
	                        while ((line = reader.readLine()) != null) {
	                                builder.append(line);
	                        }
	                        // test for success
	                        // return builder.toString();
	                		try {
	                			JSONObject jObj = new JSONObject(builder.toString());
	                			return jObj.has("result");
	                		} catch (JSONException je) {
	                		}
	                        return true;
	                } else {
	                        Log.e("Getter", "Failed to download file");
	                }
	        } catch (ClientProtocolException e) {
	        		Log.e("Getter", "ClientProtocolException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	        } catch (IOException e) {
	            	Log.e("Getter", "IOException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	        }
		} catch (URISyntaxException e) {
			Log.e("URI Syntax Exception",e.toString());
			return false;
		}
		return false;
	}

	private void unlockCourt(String booking) {
		String myURL = String.format("http://%s/Reserve/IOSUnlockBookingJSON.php?booking_id=%s",
				RHSCServer.get().getURL(), booking);
		try {
			URI targetURI = new URI(myURL);
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpGet httpGet = new HttpGet(targetURI);
	        try {
	                HttpResponse response = client.execute(httpGet);
	                StatusLine statusLine = response.getStatusLine();
	                int statusCode = statusLine.getStatusCode();
	                if (statusCode == 200) {
	                        HttpEntity entity = response.getEntity();
	                        InputStream content = entity.getContent();
	                        BufferedReader reader = new BufferedReader(
	                                        new InputStreamReader(content));
	                        String line;
	                        while ((line = reader.readLine()) != null) {
	                                builder.append(line);
	                        }
	                        // test for success
	                        // return builder.toString();
	                        return;
	                } else {
	                        Log.e("Getter", "Failed to download file");
	                }
	        } catch (ClientProtocolException e) {
	        		Log.e("Getter", "ClientProtocolException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	        } catch (IOException e) {
	            	Log.e("Getter", "IOException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	        }
		} catch (URISyntaxException e) {
			Log.e("URI Syntax Exception",e.toString());
		}
		return;
	}

}
