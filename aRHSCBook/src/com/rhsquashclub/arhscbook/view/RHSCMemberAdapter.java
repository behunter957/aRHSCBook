package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.*;
import com.rhsquashclub.arhscbook.model.*;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class RHSCMemberAdapter extends ArrayAdapter<RHSCMember> implements Filterable {

    Context context;
    int layoutResourceId;   
    RHSCMemberList data = null;
    RHSCMemberList fullList = null;
   
    public RHSCMemberAdapter(Context context, int layoutResourceId, RHSCMemberList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.fullList = data;
    }
    
    public int getCount() {
    	return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        MemberHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new MemberHolder();
            holder.fullName = (TextView)row.findViewById(R.id.fullName);
           
            row.setTag(holder);
        }
        else
        {
            holder = (MemberHolder)row.getTag();
        }
        RHSCMember member = data.get(position);
        holder.fullName.setText(member.getSortName());
       
        return row;
    }
   
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0) {
                    notifyDataSetInvalidated();
                } else {
                    data = (RHSCMemberList) results.values;
                    notifyDataSetChanged();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults result = new FilterResults();
                RHSCMemberList FilteredArrayNames = new RHSCMemberList();

                // perform your search here using the searchConstraint String.
                if(constraint == null || constraint.length() == 0){
                    result.values = fullList;
                    result.count = fullList.size();
                }else{
                	RHSCMemberList filteredList = new RHSCMemberList();
                    for(RHSCMember j: fullList){
                        if(j.getSortName().toLowerCase().contains(constraint))
                            filteredList.add(j);
                    }
                    result.values = filteredList;
                    result.count = filteredList.size();
                }
                return result;
            }
        };

        return filter;
    }
    
    static class MemberHolder
    {
        TextView fullName;
    }
}
