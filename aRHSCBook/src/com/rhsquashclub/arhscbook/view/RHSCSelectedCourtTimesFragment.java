package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RHSCSelectedCourtTimesFragment extends Fragment {
	
	public RHSCSelectedCourtTimesFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
		Log.i("RHSCSelectedCourtTimesFragment:onCreate","entry");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
		Log.i("RHSCSelectedCourtTimesFragment:onCreateView","entry");
		View view = inflater.inflate(R.layout.fragment_selectedcourtlist, container, false);
		FragmentManager fm = getFragmentManager();
		  if (fm.findFragmentById(R.id.CourtListFragment) == null) {  
			   RHSCSelectedCourtTimesListFragment list = new RHSCSelectedCourtTimesListFragment();  
			   fm.beginTransaction().add(R.id.CourtListFragment, list).commit();  
			  }  
		
		
		// use view.findViewById(id) to set values in the view
		
		return view;
	}

}
