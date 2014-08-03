package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.R;
import com.rhsquashclub.arhscbook.RHSCMain;
import com.rhsquashclub.arhscbook.model.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class RHSCMemberListFragment extends Fragment {
	
	private RHSCMemberList members;
	private RHSCMemberAdapter adapter;
	
	public RHSCMemberAdapter getAdapter() {
		return adapter;
	}

	EditText inputSearch;

	public RHSCMemberListFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override public void onCreate( Bundle savedInstanceState) { 
		super.onCreate( savedInstanceState); 
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b) {
//		Log.i("RHSCMemberListFragment:onCreateView","entry");
		View view = inflater.inflate(R.layout.fragment_memberlist, container, false);

		getActivity().setTitle(R.string.members_title); 
		
		members = RHSCMemberList.get(getActivity());
		
		adapter = 
				new RHSCMemberAdapter(getActivity(), R.layout.member_list_item_row, members); 
		ListView lv = (ListView) view.findViewById(R.id.member_list_view);
		lv.setAdapter( adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.d("Member List",
						String.format("item %d clicked", position));
				// Intent intent = new Intent(context, SendMessage.class);
				// String message = "abc";
				// intent.putExtra(EXTRA_MESSAGE, message);
				// startActivity(intent);
			}
		});		
		
		members.loadFromServer(adapter);


		inputSearch = (EditText) view.findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	RHSCMemberListFragment.this.getAdapter().getFilter().filter(cs);  
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
		
		return view;
	}

}
