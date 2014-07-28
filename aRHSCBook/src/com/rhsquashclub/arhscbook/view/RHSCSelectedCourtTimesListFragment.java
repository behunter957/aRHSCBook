package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.model.*;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RHSCSelectedCourtTimesListFragment extends ListFragment {
	
	private RHSCSelectedCourtTimes courts;

	public RHSCSelectedCourtTimesListFragment() {
		// TODO Auto-generated constructor stub
	}

	  @Override  
	  public void onListItemClick(ListView l, View v, int position, long id) {  
	  }  
	  
	  @Override  
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	    Bundle savedInstanceState) {  
		getActivity().setTitle(R.string.courts_title); 
		courts = RHSCSelectedCourtTimes.get(getActivity());
		Log.i("in onCreate","after getting courts");
		
		ArrayAdapter<RHSCCourtTime> adapter = 
				new ArrayAdapter<RHSCCourtTime>(getActivity(), android.R.layout.simple_list_item_1, courts); 
		setListAdapter( adapter);
		   return super.onCreateView(inflater, container, savedInstanceState);  

	}

}
