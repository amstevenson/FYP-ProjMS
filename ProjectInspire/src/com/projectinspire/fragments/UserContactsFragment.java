package com.projectinspire.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.projectinspire.R;
import com.projectinspire.activities.CreateOrEditContactActivity;
import com.projectinspire.adapters.ListAllContactsAdapter;

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
import android.widget.ListView;

public class UserContactsFragment extends Fragment {

	private String userId = "empty";
    private ArrayList<HashMap<String, String>> userContacts;
	private ListView  listContactsAll; 
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		view = inflater.inflate(R.layout.fragment_user_contacts, container, false);
		
		// retrieve the array containing the elements for the project
		Bundle contactBundle      = getArguments();
		Log.d("contacts - User ID", contactBundle.getString("userId"));
				
		//
		// Get user id 
		//
		userId = contactBundle.getString("userId");
		
		//
		// Initiate HashMap
		//
		userContacts = new ArrayList<HashMap<String, String>>();
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		ImageView imageViewCreateContact = (ImageView)view.findViewById(R.id.imageViewCreateContact);
		listContactsAll = (ListView) view.findViewById(R.id.listContactsAll);

		//*******************************************************************************************//
		//									Create view listeners									 //
		//*******************************************************************************************//
		//
		// If we are creating a new task
		//
		imageViewCreateContact.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
							
				Intent createContact = new Intent(view.getContext(), CreateOrEditContactActivity.class);
				createContact.putExtra("userId", userId);
				createContact.putExtra("editing", false);
				startActivity(createContact);		
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
		imageViewCreateContact.setImageDrawable(plusImage);
		
		return view;
	}

	public void setText(String item) {
		//TextView view = (TextView) getView().findViewById(R.id.txtFragContactsTitle);
		//view.setText(item);
	}
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    //
	    // If this fragment is the main focus, once it has been put to the backstack, then we would
	    // need to adopt a way of refreshing the ListView that contains all of the tasks.
	    // This can be thought of as an observer pattern of sorts (although it isnt) - although in essence
	    // the observer pattern is just invoking a method to refresh lists, so it is similar.
	    //
	    // However, calling this method again when the main focus is turned back to this fragment, means that
	    // I can control what gets refreshed. 
	    //
	    // Note: Refreshes should be implemented this way 
	    //		 (well, there are other workarounds for refreshing lists,
	    // 		 but this is the least 'cost heavy', 
	    // 		 For example, removing and adding the fragments again would suffice, but then all of the above would have to
	    // 		 be called again (on create etc), which wouldn't be that great).  
	    //
	    // Note 2: This is also called once when the fragment is created, so the call to the createAndAssignAdapter method
	    // does not need to be included in onCreate.
	    //
	    createAndAssignAdapter();
	}
	
	public void createAndAssignAdapter()
	{
		//
		// First, clear the arrayList of HashMaps, so that we do not get duplicates of any contacts
		//
		userContacts.clear();
		
		//
		// Get all of the tasks assigned to a user, then create an adapter for the ListView
		// That will contain all of the tasks, and plug information into it. 
		//
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");
		query.whereEqualTo("userId", userId);
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> contactList, ParseException e) {
				if (e == null) {      	
			    	
					//
			    	// add all items to the array list of HashMaps
			    	//
			    	for(int i = 0; i < contactList.size(); i++)
			    	{
			    		//
			    	    // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
			    	    //
			    	    HashMap<String,String> userContact = new HashMap<String, String>();
			    	        		
			    	    userContact.put("contactId",    (String) contactList.get(i).getObjectId());
			    	    userContact.put("contactName",  (String) contactList.get(i).get("contactName").toString());
			    	    userContact.put("contactNotes", (String) contactList.get(i).get("contactNotes").toString());   		
			    	    
			    	    userContacts.add(userContact);
			    	}
			    	       
			    	//
			    	// Create adapter and assign to ListView
			    	//
			    	if(userContacts.size() > 0)
			    	{
			    		ListAllContactsAdapter allProjectsAdapter = new ListAllContactsAdapter(view.getContext(), userContacts);
			    	    listContactsAll.setAdapter(allProjectsAdapter);
			    	}
			    	        	
			    	//
			    	// If we have no items, show this to the user
			    	// Chances are it will be the first time they have used the service too, so I
			    	// May want to add more user information than what is there currently.
			    	//
			    	//if(listProjectsAll.getCount() == 0) txtProjectsNone.setVisibility(View.VISIBLE);
			    	//else 								txtProjectsNone.setVisibility(View.GONE);
			    	        	
			    	// Log.d("score", "Project Name " + userProjects.get(0).get("projectEndDate") + " scores"); // debug
			    	
		    	} else Log.d("Contacts create listview Error", "Error: " + e.getMessage());
		    	        
		    }

		});
		
	}
	
}
