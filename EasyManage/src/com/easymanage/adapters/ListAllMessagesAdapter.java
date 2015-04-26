package com.easymanage.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.easymanage.activities.CreateOrEditMessageActivity;
import com.easymanage.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAllMessagesAdapter extends BaseAdapter {

	private Context							   context;
	private ArrayList<HashMap<String, String>> userMessages;
	private LayoutInflater 					   mInflater;
	
	//
    // JSON/key Node names that will be used for all of the contact tags 
	//
	private static final String TAG_MESSAGETO   = "messageFromEmail";
    private static final String TAG_MESSAGEBODY = "messageBody";
    
    //
    // Default constructor
    //
    public ListAllMessagesAdapter(Context context, ArrayList<HashMap<String, String>> userMessages){
		this.context 		= context;
		this.userMessages   = userMessages;
    }
	
	@Override
	public int getCount() {

		return userMessages.size();
	}

	@Override
	public Object getItem(int position) {

		return userMessages.get(position);
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
            convertView = mInflater.inflate(R.layout.adapter_all_messages , parent, false);
        }
		
		TextView txtMessageFrom   = (TextView ) convertView.findViewById(R.id.adapter_message_name);
		TextView txtMessageBody   = (TextView ) convertView.findViewById(R.id.adapter_message_body);
		TextView imgEdit		  = (TextView) convertView.findViewById(R.id.contactEdit);
		
		//
		// Provide the information for each item
		//
		txtMessageFrom.setText(userMessages.get(position).get(TAG_MESSAGETO).toString());
		txtMessageBody.setText(userMessages.get(position).get(TAG_MESSAGEBODY).toString());

		//final int innerPosition = position;
		
		//
		// If the edit button is clicked, send to edit page for project
		//
		imgEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// Delete message on click (or bring up menu for that)
				//boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditMessageActivity.class);
				intent.setFlags(268435456); 
				v.getContext().startActivity(intent);
				
			}
		});
		
		
		return convertView;
	}

}
