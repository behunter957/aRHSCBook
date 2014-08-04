package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.*;
import com.rhsquashclub.arhscbook.model.*;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RHSCMyBookingsAdapter extends ArrayAdapter<RHSCCourtTime> {

    Context context;
    int layoutResourceId;   
    RHSCCourtTimeList data = null;
   
    public RHSCMyBookingsAdapter(Context context, int layoutResourceId, RHSCCourtTimeList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        BookingHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new BookingHolder();
            holder.courtAndtime = (TextView)row.findViewById(R.id.courtAndTime);
            holder.eventAndPlayers = (TextView)row.findViewById(R.id.eventAndPlayers);
           
            row.setTag(holder);
        }
        else
        {
            holder = (BookingHolder)row.getTag();
        }
       
        RHSCCourtTime courtTime = data.get(position);
        holder.courtAndtime.setText(courtTime.getCourtAndDateTime());
        holder.eventAndPlayers.setText(courtTime.getEventAndPlayers());
       
        return row;
    }
   
    static class BookingHolder
    {
        TextView courtAndtime;
        TextView eventAndPlayers;
    }
}
