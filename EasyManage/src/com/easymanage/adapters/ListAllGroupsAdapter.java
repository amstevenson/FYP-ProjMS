package com.easymanage.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.easymanage.R;
import com.easymanage.activities.CreateOrEditGroupActivity;
import com.easymanage.activities.UserSelectedGroupActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * A custom built adapter to create, list and manipulate the
 * information received from an external call to Parse.
 * 
 * @author Adam Stevenson
 *
 */
public class ListAllGroupsAdapter extends BaseAdapter{

	private Context							   context;
	private ArrayList<HashMap<String, String>> userGroups;
	private LayoutInflater 					   mInflater;
	private String userId = "";
	
	//
    // JSON/key Node names that will be used for all of the project tags 
	//
	private static final String TAG_GROUPID          = "groupId";
	private static final String TAG_GROUPNAME        = "groupName";
    private static final String TAG_GROUPDESCRIPTION = "groupDescription";

    //
    // Default constructor
    //
    public ListAllGroupsAdapter(Context context, ArrayList<HashMap<String, String>> userGroups,
    		String userId){
		this.context 	= context;
		this.userGroups = userGroups;
		this.userId     = userId;
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userGroups.size();
	}

	@Override
	public Object getItem(int position) {

		return userGroups.get(position);
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
            convertView = mInflater.inflate(R.layout.adapter_all_groups , parent, false);
        }
		
		//
		// Get the view objects and assign values
		//
		TextView groupName        = (TextView) convertView.findViewById(R.id.groupName);
		TextView groupDescription = (TextView) convertView.findViewById(R.id.groupDescription);
		TextView groupEditButton  = (TextView) convertView.findViewById(R.id.groupEdit);
		
		groupName.setText(userGroups.get(position).get(TAG_GROUPNAME));
		groupDescription.setText(userGroups.get(position).get(TAG_GROUPDESCRIPTION));
		
		//
		// If the edit button is clicked, send the user to the relevant activity
		//
		final int innerPosition = position;
		
		groupEditButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditGroupActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra(TAG_GROUPID, userGroups.get(innerPosition).get(TAG_GROUPID));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		//
		// If anything else has been clicked, proceed to the selected group fragments
		//
		groupName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(v.getContext(), UserSelectedGroupActivity.class);
				intent.putExtra(TAG_GROUPID, userGroups.get(innerPosition).get(TAG_GROUPID));
				intent.putExtra("userId", userId);
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		groupDescription.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(v.getContext(), UserSelectedGroupActivity.class);
				intent.putExtra(TAG_GROUPID, userGroups.get(innerPosition).get(TAG_GROUPID));
				intent.putExtra("userId", userId);
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		return convertView;
	}

}
