package com.easymanage.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easymanage.R;
import com.easymanage.activities.ChooseGroupMemberActivity;
import com.easymanage.utilities.NetworkStateOperations;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * 
 * This activity lists and retrieves all of the members that have been
 * allocated to a group.
 * 
 * @author Adam Stevenson
 *
 */
public class UserSelectedGroupMembersFragment extends Fragment{

	private View view;
	private String groupId = "";
	private String userId  = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_user_selected_group_members, container, false);

		// retrieve the array containing the elements for the project
		Bundle projectBundle      = getArguments();

		//
		// Get group and user Id
		//
		groupId = projectBundle.getString("groupId");
		userId  = projectBundle.getString("userId");
		
		//
		// ArrayLists
		//
		final ArrayList<HashMap<String, String>> allGroupMembers = new ArrayList<HashMap<String, String>>(); 
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		final ImageView imageViewCreateGroupMember = (ImageView)view.findViewById(R.id.imageViewGroupMemberCreate);
		
		//*******************************************************************************************//
		//									Create view listeners									 //
		//*******************************************************************************************//
		//
		// If we are creating a new group project
		//
		//
		// Get all of the projects assigned to a user, then create an adapter for the ListView
		// That will contain all of the tasks, and plug information into it. 
		//
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");
		query.whereEqualTo("userId", userId);
		query.orderByDescending("createdAt");
		if(NetworkStateOperations.testNetworkConnection(getActivity().getApplicationContext()));
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(final List<ParseObject> userContacts, ParseException e) {
				
				if (e == null) {      	
			    	
					for(int i = 0; i < userContacts.size(); i ++)
					{ 		
			    	    //
			    	    // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
			    	    //
			    	    HashMap<String,String> userContact = new HashMap<String, String>();
			    	        		
			    	    userContact.put("contactName",   (String) userContacts.get(i).get("contactName").toString());
			    	    userContact.put("contactUserId", (String) userContacts.get(i).get("contactUserId").toString());
	
			    	    // Add to the ArrayLists
			    	    allGroupMembers.add(userContact);
					}
					   	
		    	} else Log.d("User Contact retrieval error", "Error: " + e.getMessage());
		    	  
				imageViewCreateGroupMember.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						ArrayList<String> contactUserIds   = new ArrayList<String>();
						ArrayList<String> contactNames = new ArrayList<String>();
						
						//
						// Get the names of the projects and the Id
						//
						for(int i = 0; i < allGroupMembers.size(); i++)
						{
							contactUserIds.add(allGroupMembers.get(i).get("contactUserId"));
							contactNames.add(allGroupMembers.get(i).get("contactName"));
						}
						
						//
						// Send to the intent where the user can select a project
						//
						Intent intent = new Intent(v.getContext(), ChooseGroupMemberActivity.class);
						intent.setFlags(268435456); // Put activity on the back stack
						intent.putStringArrayListExtra("contactUserIds", contactUserIds);
						intent.putStringArrayListExtra("contactNames", contactNames);
						intent.putExtra("groupId", groupId);
						v.getContext().startActivity(intent);
						
						Log.d("User contacts size", "User contacts size: " + contactNames.size());
					}
				});
		    }
		});
		
		//*******************************************************************************************//
		//									Create and assign the images							 //
		//*******************************************************************************************//
	    //
		// If we do not have a image stored for the user, create the default user image drawable
		//
		Drawable plusImage = getResources().getDrawable(R.drawable.icon_add_48dp_black_circle);

		//
		// Assign the drawable to the ImageView
		//
		imageViewCreateGroupMember.setImageDrawable(plusImage);
						

		
		return view;
	}
}
