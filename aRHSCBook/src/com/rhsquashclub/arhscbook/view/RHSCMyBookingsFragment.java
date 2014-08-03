package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.*;

import android.content.Intent;
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
import android.widget.ListView;

public class RHSCMyBookingsFragment extends Fragment {

	private RHSCMyBookings bookings;
	
	private RHSCMyBookingsAdapter listAdapter;

	public RHSCMyBookingsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
//		Log.i("RHSCSelectedCourtTimesFragment:onCreate","entry");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
//		Log.i("RHSCSelectedCourtTimesFragment:onCreateView","entry");
		View view = inflater.inflate(R.layout.fragment_mybookingslist, container, false);

		getActivity().setTitle(R.string.bookings_title); 
		
		bookings = RHSCMyBookings.get(getActivity());
		
		listAdapter = 
				new RHSCMyBookingsAdapter(getActivity(), R.layout.bookings_list_item_row,bookings);
		ListView lv = (ListView) view.findViewById(R.id.BookingsFragment);
		lv.setAdapter( listAdapter);	
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("MyBookings",
						String.format("item %d clicked", position));
				// Intent intent = new Intent(context, SendMessage.class);
				// String message = "abc";
				// intent.putExtra(EXTRA_MESSAGE, message);
				// startActivity(intent);
			}
		});		
		
		String[] parms = { "bhunter" };
		bookings.loadFromServer(listAdapter,parms);
		// use view.findViewById(id) to set values in the view
		
		return view;
	}

	public RHSCMyBookingsAdapter getListAdapter() {
		return listAdapter;
	}

}
