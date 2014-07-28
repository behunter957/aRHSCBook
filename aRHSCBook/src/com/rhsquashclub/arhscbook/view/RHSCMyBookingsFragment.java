package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.*;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class RHSCMyBookingsFragment extends ListFragment {
	
	private RHSCMyBookings bookings;

	public RHSCMyBookingsFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
		getActivity().setTitle(R.string.members_title); 
		bookings = RHSCMyBookings.get(getActivity());
		
		ArrayAdapter<RHSCCourtTime> adapter = 
				new ArrayAdapter<RHSCCourtTime>(getActivity(), android.R.layout.simple_list_item_1, bookings); 
		setListAdapter( adapter);

	}

}
