package com.projectinspire.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.projectinspire.R;
import com.projectinspire.activities.CreateOrEditMessageActivity;
import com.projectinspire.adapters.ListAllMessagesAdapter;

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

public class UserContactsMessagesFragment extends Fragment {

	private String userId, userEmail = "empty";
    private ArrayList<HashMap<String, String>> userMessages;
	private ListView  listMessagesAll; 
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		view = inflater.inflate(R.layout.fragment_user_contacts_messages, container, false);
		
		// retrieve the array containing the elements for the project
		Bundle messageBundle      = getArguments();
		Log.d("Messages - User ID", messageBundle.getString("userId"));
		Log.d("messages - User Email", messageBundle.getString("userEmail"));
		
		//
		// Get user id 
		//
		userId = messageBundle.getString("userId");
		
		//
		// get user email
		//
		userEmail = messageBundle.getString("userEmail");
		
		//
		// Initiate HashMap
		//
		userMessages = new ArrayList<HashMap<String, String>>();
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		ImageView imageViewCreateMessage = (ImageView) view.findViewById(R.id.imageViewCreateMessage);
		listMessagesAll = (ListView) view.findViewById(R.id.listMessagesAll);
		
		//*******************************************************************************************//
		//									Create view listeners									 //
		//*******************************************************************************************//
		//
		// If we are creating a new task
		//
		imageViewCreateMessage.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
								
				Intent createContact = new Intent(view.getContext(), CreateOrEditMessageActivity.class);
				createContact.putExtra("userId", userId);
				createContact.putExtra("userEmail", userEmail);
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
		imageViewCreateMessage.setImageDrawable(plusImage);
		
		return view;
	}

	public void setText(String item) {
		//TextView view = (TextView) getView().findViewById(R.id.txtFragMessagesTitle);
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
		userMessages.clear();
		
		//
		// Get all of the tasks assigned to a user, then create an adapter for the ListView
		// That will contain all of the tasks, and plug information into it. 
		//
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
		query.whereEqualTo("messageToID", userId);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> messageList, ParseException e) {
				if (e == null) {      	
			    	
					//
			    	// add all items to the array list of HashMaps
			    	//
			    	for(int i = 0; i < messageList.size(); i++)
			    	{
			    		//
			    	    // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
			    	    //
			    	    HashMap<String,String> userMessage = new HashMap<String, String>();
			    	        		
			    	    userMessage.put("messageFromEmail",  (String) messageList.get(i).get("messageFromEmail").toString());
			    	    userMessage.put("messageBody", (String) messageList.get(i).get("messageBody").toString());   		
			    	    
			    	    userMessages.add(userMessage);
			    	}
			    	       
			    	//
			    	// Create adapter and assign to ListView
			    	//
			    	if(userMessages.size() >= 0)
			    	{
			    		ListAllMessagesAdapter allMessagesAdapter = new ListAllMessagesAdapter(view.getContext(), userMessages);
			    	    listMessagesAll.setAdapter(allMessagesAdapter);
			    	}
			    	        	
		    	} else Log.d("Messages create listview Error", "Error: " + e.getMessage());
		    	        
		    }

		});
		
	}
}
