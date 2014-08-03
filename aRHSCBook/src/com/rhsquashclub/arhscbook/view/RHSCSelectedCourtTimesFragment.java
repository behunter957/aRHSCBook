package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.RHSCCourtTime;
import com.rhsquashclub.arhscbook.model.RHSCSelectedCourtTimes;
import com.rhsquashclub.arhscbook.model.RHSCServer;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

public class RHSCSelectedCourtTimesFragment extends Fragment {

	private RHSCSelectedCourtTimes courts;
	
	private RHSCCourtTimeAdapter listAdapter;

	public RHSCSelectedCourtTimesFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
//		Log.i("RHSCSelectedCourtTimesFragment:onCreate","entry");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
//		Log.i("RHSCSelectedCourtTimesFragment:onCreateView","entry");
		View view = inflater.inflate(R.layout.fragment_selectedcourtlist, container, false);

		getActivity().setTitle(R.string.courts_title); 
		
		courts = RHSCSelectedCourtTimes.get(getActivity());

		Spinner courtSel = (Spinner) view.findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.court_selection_array, R.layout.spinner_item);
		// Specify the layout to use when the list of choices appears
//		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		courtSel.setAdapter(spinnerAdapter);
		
		DatePicker dateSel = (DatePicker) view.findViewById(R.id.datePicker1);
		
		Switch includeSel = (Switch) view.findViewById(R.id.switch1);

		listAdapter = 
				new RHSCCourtTimeAdapter(getActivity(), R.layout.court_times_list_item_row,courts);
		ListView lv = (ListView) view.findViewById(R.id.CourtListFragment);
		lv.setAdapter( listAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("SelectedCourtTimes",
						String.format("item %d clicked", position));
				// Intent intent = new Intent(context, SendMessage.class);
				// String message = "abc";
				// intent.putExtra(EXTRA_MESSAGE, message);
				// startActivity(intent);
			}
		});		
		
		String[] parms = { "2014-08-06", "All", "YES", "bhunter" };
		courts.loadFromServer(listAdapter,parms);
		// use view.findViewById(id) to set values in the view
		
		return view;
	}

	public RHSCCourtTimeAdapter getListAdapter() {
		return listAdapter;
	}

}
