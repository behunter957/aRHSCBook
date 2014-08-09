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
import android.widget.Toast;

public class BookDoublesActivity extends Activity {
	
	private RHSCCourtTime targetCourt;
	private ArrayAdapter<CharSequence> spinnerAdapter;
	private boolean can_book = false;
	private int returnValue;
	
	static final int SELECT_PLAYER2 = 2;
	static final int SELECT_PLAYER3 = 3;
	static final int SELECT_PLAYER4 = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_doubles);
		
    	Log.i("BookDoublesActivity","onCreate");
		Intent intent = getIntent();
		String jsonCourt = intent.getStringExtra("court");
		Gson gson = new Gson();
		targetCourt = gson.fromJson(jsonCourt, RHSCCourtTime.class);

		// first lock the court (will update can_book if successful)
		String[] parms = { targetCourt.getBookingId(), 
				RHSCPreferences.get().getUserid() };
		RHSCLockCourtTimeTask bgTask = new RHSCLockCourtTimeTask();
		bgTask.execute(parms);
		// then populate the view
		String ctdText = String.format("%s on %s", targetCourt.getCourt(),new SimpleDateFormat(
				"EEEE, MMMM d 'at' h:mm", Locale.ENGLISH)
				.format(targetCourt.getCourtTime()));
		TextView courtDesc = (TextView) findViewById(R.id.courtTimeDesc);
		courtDesc.setText(ctdText);
		
		// populate the spinner for event type
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
				if (can_book) {
					// first update the booking - http call
					String[] parms = { targetCourt.getBookingId(), RHSCPreferences.get().getUserid(), 
							targetCourt.getPlayer_id()[1], targetCourt.getPlayer_id()[2], targetCourt.getPlayer_id()[3],targetCourt.getEvent() };
					RHSCBookCourtTimeTask bgTask = new RHSCBookCourtTimeTask();
					bgTask.execute(parms);
				}
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
		RadioGroup rg2 = (RadioGroup) findViewById(R.id.player2);
		
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
	        	RadioButton playerButton = (RadioButton) findViewById(R.id.member2);
                switch(checkedId)
                {
                case R.id.tbd2:
                    // TODO Something
                	targetCourt.getPlayer_id()[1] = "TBD";
                	targetCourt.getPlayer_lname()[1] = "TBD";
    	        	playerButton.setText("Select Member");
                    break;
                case R.id.guest2:
                    // TODO Something
                	targetCourt.getPlayer_id()[1] = "Guest";
                	targetCourt.getPlayer_lname()[1] = "Guest";
    	        	playerButton.setText("Select Member");
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

		// intercept radio button select for player2
		RadioGroup rg3 = (RadioGroup) findViewById(R.id.player3);
		
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
	        	RadioButton playerButton = (RadioButton) findViewById(R.id.member3);
                switch(checkedId)
                {
                case R.id.tbd3:
                	targetCourt.getPlayer_id()[2] = "TBD";
                	targetCourt.getPlayer_lname()[2] = "TBD";
    	        	playerButton.setText("Select Member");
                    break;
                case R.id.guest3:
                	targetCourt.getPlayer_id()[2] = "Guest";
                	targetCourt.getPlayer_lname()[2] = "Guest";
    	        	playerButton.setText("Select Member");
                    break;
                case R.id.member3:
                	Log.i("BookSinglesActivity","button for member clicked");
					Intent intent = new Intent(getApplicationContext(),
							PickPlayerActivity.class);
					startActivityForResult(intent,SELECT_PLAYER3);
                    break;
                }
            }
        });

		// intercept radio button select for player4
		RadioGroup rg4 = (RadioGroup) findViewById(R.id.player4);
		
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
	        	RadioButton playerButton = (RadioButton) findViewById(R.id.member4);
                switch(checkedId)
                {
                case R.id.tbd4:
                    // TODO Something
                	targetCourt.getPlayer_id()[3] = "TBD";
                	targetCourt.getPlayer_lname()[3] = "TBD";
    	        	playerButton.setText("Select Member");
                    break;
                case R.id.guest4:
                    // TODO Something
                	targetCourt.getPlayer_id()[3] = "Guest";
                	targetCourt.getPlayer_lname()[3] = "Guest";
    	        	playerButton.setText("Select Member");
                    break;
                case R.id.member4:
                    // TODO Something
                	Log.i("BookSinglesActivity","button for member clicked");
					Intent intent = new Intent(getApplicationContext(),
							PickPlayerActivity.class);
					startActivityForResult(intent,SELECT_PLAYER4);
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
            	targetCourt.getPlayer_id()[1] = player.getName();
            	targetCourt.getPlayer_lname()[1] = player.getLastName();
	        	Log.i("return from select player2",player.getName());
	        }
	        if (resultCode == android.app.Activity.RESULT_CANCELED) {
	        	Log.i("return from select player2",data.getExtras().getString("reason"));
	        }
	    }
	    if (requestCode == SELECT_PLAYER3) {
	        // Make sure the request was successful
	        if (resultCode == android.app.Activity.RESULT_OK) {
	        	Gson gson = new Gson();
	        	RHSCMember player = gson.fromJson(data.getExtras().getString("player"), RHSCMember.class);
	        	RadioButton playerButton = (RadioButton) findViewById(R.id.member3);
	        	playerButton.setText(player.getDisplayName());
            	targetCourt.getPlayer_id()[2] = player.getName();
            	targetCourt.getPlayer_lname()[2] = player.getLastName();
	        	Log.i("return from select player3",player.getName());
	        }
	        if (resultCode == android.app.Activity.RESULT_CANCELED) {
	        	Log.i("return from select player3",data.getExtras().getString("reason"));
	        }
	    }
	    if (requestCode == SELECT_PLAYER4) {
	        // Make sure the request was successful
	        if (resultCode == android.app.Activity.RESULT_OK) {
	        	Gson gson = new Gson();
	        	RHSCMember player = gson.fromJson(data.getExtras().getString("player"), RHSCMember.class);
	        	RadioButton playerButton = (RadioButton) findViewById(R.id.member4);
	        	playerButton.setText(player.getDisplayName());
            	targetCourt.getPlayer_id()[3] = player.getName();
            	targetCourt.getPlayer_lname()[3] = player.getLastName();
	        	Log.i("return from select player4",player.getName());
	        }
	        if (resultCode == android.app.Activity.RESULT_CANCELED) {
	        	Log.i("return from select player4",data.getExtras().getString("reason"));
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

	private class RHSCLockCourtTimeTask extends AsyncTask<String, Void, String> {
		
		public URI getRequestURI(String booking,String uid) {
			String myURL = String.format("http://%s/Reserve/IOSLockBookingJSON.php?bookingId=%s&uid=%s",
						RHSCServer.get().getURL(), booking, uid);
			Log.i("LockCourtTimes",myURL);
			try {
				URI targetURI = new URI(myURL);
				return targetURI;
			} catch (URISyntaxException e) {
				Log.e("URI Syntax Exception",e.toString());
				return null;
			}
		}
		
	    @Override
	    protected String doInBackground(String... parms) {
	    	// parm 1 is booking
	    	// parm 2 is uid
			Log.i("lock court","doInBackground");
	    	URI targetURI = getRequestURI(parms[0],parms[1]);
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
//	                        Log.v("Getter", "Your data: " + builder.toString()); //response data
	            			Log.i("lock court",builder.toString());
	            			try {
		            			JSONObject jObj = new JSONObject(builder.toString());
		            			return jObj.has("result")?"success":"error";
	            			} catch (JSONException je) {
	            				return "error";
	            			}
	                } else {
	                        Log.e("Getter", "Failed to download file");
	                        return "error";
	                }
	        } catch (ClientProtocolException e) {
	        		Log.e("Getter", "ClientProtocolException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	                return "error";
	        } catch (IOException e) {
	            	Log.e("Getter", "IOException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	                return "error";
	        }
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
	    	if (result != null) {
	    		if (result.equals("success")) {
	    			// allow book button
	    			can_book = true;
	    			return;
	    		}
	    	}
	    	// show message cant lock and return to parent
			Context context = getApplicationContext();
			CharSequence text = "Court could not be locked";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			Intent returnIntent = new Intent();
			returnIntent.putExtra("reason","could not lock the court"); 
			setResult(RESULT_CANCELED,returnIntent);
			finish();
	    }

	}

	private class RHSCUnlockCourtTimeTask extends AsyncTask<String, Void, Void> {
		
		
		public URI getRequestURI(String booking) {
			String myURL = String.format("http://%s/Reserve/IOSLockBookingJSON.php?booking_id=%s",
						RHSCServer.get().getURL(), booking);
			try {
				URI targetURI = new URI(myURL);
				return targetURI;
			} catch (URISyntaxException e) {
				Log.e("URI Syntax Exception",e.toString());
				return null;
			}
		}
		
	    @Override
	    protected Void doInBackground(String... parms) {
	    	// parm 1 is booking
	    	URI targetURI = getRequestURI(parms[0]);
	    	returnValue = (parms[1].equals("success")?RESULT_OK:RESULT_CANCELED);
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
	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) {
	    	// show message cant lock and return to parent
			Intent returnIntent = new Intent();
			setResult(returnValue,returnIntent);
			finish();
	    }

	}

	private class RHSCBookCourtTimeTask extends AsyncTask<String, Void, String> {
		
		public URI getRequestURI(String[] parms) {
			String myURL = String.format("http://%s/Reserve/IOSUpdateBookingJSON.php?b_id=%s&player1=%s&player2=%s&player3=%s&player4=%s&uid=%s&channel=%s&courtEvent=%s",
						RHSCServer.get().getURL(), parms[0], parms[1], parms[2], parms[3], parms[4], RHSCPreferences.get().getUserid(),"aRHSCBook", parms[5]);
			Log.i("BookCourtTime",myURL);
			try {
				URI targetURI = new URI(myURL);
				return targetURI;
			} catch (URISyntaxException e) {
				Log.e("URI Syntax Exception",e.toString());
				return null;
			}
		}
		
	    @Override
	    protected String doInBackground(String... parms) {
	    	URI targetURI = getRequestURI(parms);
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
	                        Log.i("BookCourtTime", builder.toString()); //response data
	            			try {
		            			JSONObject jObj = new JSONObject(builder.toString());
		            			Log.i("book doubles result",jObj.has("result")?jObj.getString("result"):jObj.getString("error"));
		            			return jObj.has("result")?"success":"error";
	            			} catch (JSONException je) {
	            				return "error";
	            			}
	                } else {
	                        Log.e("Getter", "Failed to download file");
	                        return "error";
	                }
	        } catch (ClientProtocolException e) {
	        		Log.e("Getter", "ClientProtocolException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	                return "error";
	        } catch (IOException e) {
	            	Log.e("Getter", "IOException on ".concat(targetURI.toString()));
	                e.printStackTrace();
	                return "error";
	        }
	    }
	    
	    @Override
	    protected void onPostExecute(String result) {
	    	if (result != null) {
	    		if (result.equals("success")) {
	    			// show message that booking was successful
	    			Context context = getApplicationContext();
	    			CharSequence text = "Court booked";
	    			int duration = Toast.LENGTH_LONG;
	    			Toast toast = Toast.makeText(context, text, duration);
	    			toast.show();
	    		} else {
	    			// show message that booking was not successful
	    			Context context = getApplicationContext();
	    			CharSequence text = "Court not booked - server error";
	    			int duration = Toast.LENGTH_LONG;
	    			Toast toast = Toast.makeText(context, text, duration);
	    			toast.show();
	    		}
    		} else {
    			// show message that booking was not successful
    			Context context = getApplicationContext();
    			CharSequence text = "Court not booked - network error";
    			int duration = Toast.LENGTH_LONG;
    			Toast toast = Toast.makeText(context, text, duration);
    			toast.show();
	    	}
			// unlock the court 
			String[] parms = { targetCourt.getBookingId(), result };
			RHSCUnlockCourtTimeTask bgTask = new RHSCUnlockCourtTimeTask();
			bgTask.execute(parms);
	    }

	}

}
