package com.projectinspire.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.projectinspire.R;
import com.projectinspire.activities.CreateOrEditEventActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAllEventsAdapter extends BaseAdapter{

	private Context							   context;
	private ArrayList<HashMap<String, String>> userEvents;
	private LayoutInflater 					   mInflater;
	
	//
    // JSON/key Node names that will be used for all of the event tags 
	//
	private static final String TAG_EVENTNAME        = "eventName";
    private static final String TAG_EVENTLOCATION    = "eventLocation";
    private static final String TAG_EVENTDESCRIPTION = "eventDescription";
    private static final String TAG_EVENTDATE        = "eventDate";
    private static final String TAG_EVENTSTARTIME    = "eventStartTime";
    private static final String TAG_EVENTENDTIME     = "eventEndTime";
    //private static final String TAG_EVENTVISIBILITY  = "eventVisibility";
	
    //
    // Default constructor
    //
    public ListAllEventsAdapter(Context context, ArrayList<HashMap<String, String>> userEvents){
		this.context 		= context;
		this.userEvents   = userEvents;
    }
    
	@Override
	public int getCount() {

		return userEvents.size();
	}

	@Override
	public Object getItem(int position) {

		return userEvents.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_all_events , parent, false);
        }
		
		TextView txtEventName        = (TextView ) convertView.findViewById(R.id.eventName);
		TextView txtEventLocation    = (TextView ) convertView.findViewById(R.id.eventLocation);
		TextView txtEventDescription = (TextView ) convertView.findViewById(R.id.eventDescription);
		TextView txtEventDate        = (TextView ) convertView.findViewById(R.id.eventDate);
		TextView txtEventStartTime   = (TextView ) convertView.findViewById(R.id.eventStartTime);
		TextView txtEventEndTime     = (TextView ) convertView.findViewById(R.id.eventEndTime);
		ImageView imgEdit			 = (ImageView) convertView.findViewById(R.id.eventEdit);
		
		//
		// Provide the information for each item
		//
		txtEventName.setText(userEvents.get(position).get(TAG_EVENTNAME).toString());
		txtEventLocation.setText(userEvents.get(position).get(TAG_EVENTLOCATION).toString());
		txtEventDescription.setText(userEvents.get(position).get(TAG_EVENTDESCRIPTION).toString());
		txtEventDate.setText("Date: " + userEvents.get(position).get(TAG_EVENTDATE).toString());
		txtEventStartTime.setText("Starts: " + userEvents.get(position).get(TAG_EVENTSTARTIME).toString());
		txtEventEndTime.setText("Ends: " + userEvents.get(position).get(TAG_EVENTENDTIME).toString());

		final int innerPosition = position;
		
		//
		// If the edit button is clicked, send to edit page for project
		//
		imgEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditEventActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("eventId", userEvents.get(innerPosition).get("eventId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		txtEventDescription.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditEventActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("eventId", userEvents.get(innerPosition).get("eventId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		txtEventName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditEventActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("eventId", userEvents.get(innerPosition).get("eventId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		return convertView;
	}

}
