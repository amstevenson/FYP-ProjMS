package com.easymanage.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.easymanage.activities.CreateOrEditContactActivity;
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

public class ListAllContactsAdapter extends BaseAdapter{

	private Context							   context;
	private ArrayList<HashMap<String, String>> userContacts;
	private LayoutInflater 					   mInflater;
	
	//
    // JSON/key Node names that will be used for all of the contact tags 
	//
	private static final String TAG_CONTACTNAME  = "contactName";
    //private static final String TAG_CONTACTEMAIL = "eventLocation";
    private static final String TAG_CONTACTNOTES = "contactNotes";
    
    // add in image later if needed (which it will be).
	
    //
    // Default constructor
    //
    public ListAllContactsAdapter(Context context, ArrayList<HashMap<String, String>> userContacts){
		this.context 		= context;
		this.userContacts   = userContacts;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userContacts.size();
	}

	@Override
	public Object getItem(int position) {
		
		return userContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_all_contacts , parent, false);
        }
		
		TextView    txtContactSpace  = (TextView)     convertView.findViewById(R.id.adapter_contact_space);
		TextView txtContactName   = (TextView ) convertView.findViewById(R.id.adapter_contact_name);
		TextView txtContactNotes  = (TextView ) convertView.findViewById(R.id.adapter_contact_notes);
		TextView imgEdit		  = (TextView) convertView.findViewById(R.id.contactEdit);
		
		//
		// Provide the information for each item
		//
		txtContactName.setText(userContacts.get(position).get(TAG_CONTACTNAME).toString());
		txtContactNotes.setText(userContacts.get(position).get(TAG_CONTACTNOTES).toString());

		final int innerPosition = position;
		
		//
		// If the edit button is clicked, send to edit page for project
		//
		imgEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditContactActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("contactId", userContacts.get(innerPosition).get("contactId"));
				intent.setFlags(268435456); 
				v.getContext().startActivity(intent);
				
			}
		});
		
		txtContactSpace.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditContactActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("contactId", userContacts.get(innerPosition).get("contactId"));
				intent.setFlags(268435456); 
				v.getContext().startActivity(intent);
				
			}
		});
		
		txtContactName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditContactActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("contactId", userContacts.get(innerPosition).get("contactId"));
				intent.setFlags(268435456); 
				v.getContext().startActivity(intent);
				
			}
		});
		
		return convertView;
	}

	
}
