package com.rhsquashclub.arhscbook.view;

import com.rhsquashclub.arhscbook.*;
import com.rhsquashclub.arhscbook.model.*;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RHSCCourtTimeAdapter extends ArrayAdapter<RHSCCourtTime> {

    Context context;
    int layoutResourceId;   
    RHSCCourtTimeList data = null;
   
    public RHSCCourtTimeAdapter(Context context, int layoutResourceId, RHSCCourtTimeList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CourtTimeHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new CourtTimeHolder();
            holder.courtAndtime = (TextView)row.findViewById(R.id.courtAndTime);
            holder.status = (TextView)row.findViewById(R.id.status);
            holder.eventAndPlayers = (TextView)row.findViewById(R.id.eventAndPlayers);
           
            row.setTag(holder);
        }
        else
        {
            holder = (CourtTimeHolder)row.getTag();
        }
       
        RHSCCourtTime courtTime = data.get(position);
        holder.courtAndtime.setText(courtTime.getCourtAndTime());
        holder.status.setText(courtTime.getStatus());
        if (courtTime.getStatus().equals("Available")) {
           	holder.eventAndPlayers.setText("");
           	holder.status.setTextColor(Color.rgb(1,104,32));
        } else {
        	holder.eventAndPlayers.setText(courtTime.getEventAndPlayers());
           	holder.status.setTextColor(Color.BLACK);
        	String user = RHSCPreferences.get().getUserid(); 
        	for (int i = 0; i < 4; i++) {
        		if (user.equals(courtTime.getPlayer_id()[i])) {
                   	holder.status.setTextColor(Color.RED);
        			break;
        		}
        	}
        }
       
        return row;
    }
   
    static class CourtTimeHolder
    {
        TextView courtAndtime;
        TextView status;
        TextView eventAndPlayers;
    }
}
