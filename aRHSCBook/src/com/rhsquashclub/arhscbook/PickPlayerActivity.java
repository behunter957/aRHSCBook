package com.rhsquashclub.arhscbook;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rhsquashclub.arhscbook.model.RHSCMemberList;
import com.rhsquashclub.arhscbook.view.RHSCCourtTimeAdapter;
import com.rhsquashclub.arhscbook.view.RHSCMemberAdapter;
import com.rhsquashclub.arhscbook.view.RHSCMemberListFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class PickPlayerActivity extends Activity {

	private RHSCMemberAdapter listAdapter;
	private RHSCMemberList members;
	private EditText inputSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_player);
		members = RHSCMemberList.get(getApplicationContext());
		listAdapter = 
				new RHSCMemberAdapter(this, R.layout.member_list_item_row,members);
		ListView lv = (ListView) findViewById(R.id.player_list_view);
		lv.setAdapter( listAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("PickPlayer", String.format("item %d clicked", position));
				Intent returnIntent = new Intent();
				Gson gson = new Gson();
				returnIntent.putExtra("player", gson.toJson(members.get(position)));
				setResult(RESULT_OK,returnIntent);
				finish();
			}
		});
		
		inputSearch = (EditText) findViewById(R.id.inputSearch2);
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	PickPlayerActivity.this.listAdapter.getFilter().filter(cs);  
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                         
            }
        });		
		

		Button cancelButton = (Button) findViewById(R.id.cancelPickMember);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("reason","cancelled by user"); // if http update failed then return "service failure"
				setResult(RESULT_CANCELED,returnIntent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pick_player, menu);
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
