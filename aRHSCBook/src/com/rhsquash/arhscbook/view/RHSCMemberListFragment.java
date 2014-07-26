package com.rhsquash.arhscbook.view;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.*;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class RHSCMemberListFragment extends ListFragment {
	
	private RHSCMemberList members;

	public RHSCMemberListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
		getActivity().setTitle(R.string.members_title); 
		members = RHSCMemberList.get(getActivity());
		
		ArrayAdapter<RHSCMember> adapter = 
				new ArrayAdapter<RHSCMember>(getActivity(), android.R.layout.simple_list_item_1, members); 
		setListAdapter( adapter);

	}

}
