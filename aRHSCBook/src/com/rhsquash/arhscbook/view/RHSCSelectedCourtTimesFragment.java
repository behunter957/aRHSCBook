package com.rhsquash.arhscbook.view;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.*;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class RHSCSelectedCourtTimesFragment extends ListFragment {
	
	private RHSCSelectedCourtTimes courts;

	public RHSCSelectedCourtTimesFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
		getActivity().setTitle(R.string.courts_title); 
		courts = RHSCSelectedCourtTimes.get(getActivity());
		
		ArrayAdapter<RHSCCourtTime> adapter = 
				new ArrayAdapter<RHSCCourtTime>(getActivity(), android.R.layout.simple_list_item_1, courts); 
		setListAdapter( adapter);

	}

}
