package com.rhsquashclub.arhscbook;

import com.google.gson.Gson;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;

import android.app.Activity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_singles);
		
		Intent intent = getIntent();
		String jsonCourt = intent.getStringExtra("court");
		Gson gson = new Gson();
		testCourt = gson.fromJson(jsonCourt, RHSCCourtTime.class);
		testCourt.setEvent("Psych!");
		Log.i("BookSinglesActivity received",jsonCourt);
		Button bookButton = (Button) findViewById(R.id.bookButton);
		bookButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				Gson gson = new Gson();
				returnIntent.putExtra("court",gson.toJson(testCourt));
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		});

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
