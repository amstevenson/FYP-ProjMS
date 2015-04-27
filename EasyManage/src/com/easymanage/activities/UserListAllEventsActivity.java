package com.easymanage.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easymanage.adapters.ListAllEventsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.easymanage.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * This activity lists all of the events that a user has created.
 * This uses a custom built adapter. 
 * 
 * @author Adam Stevenson
 *
 */
public class UserListAllEventsActivity extends Activity {

	private String  userId    = "empty";
    private ArrayList<HashMap<String, String>> userEvents;
    private ListView listEventsAll;
    private TextView txtEventsNone;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list_all_events);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Events");
		
		//
		// Initiate HashMap of events
		//
		userEvents = new ArrayList<HashMap<String, String>>();
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user Id
    	//
    	Intent intent = getIntent();
    	
    	userId		  = intent.getStringExtra("userId");
		
    	Log.d("List all events - User Id", userId); // for debugging
		
    	//*******************************************************************************************//
    	//											Views								 		     //
    	//*******************************************************************************************//	
		txtEventsNone = (TextView)findViewById(R.id.txtEventNone);
		listEventsAll = (ListView)findViewById(R.id.listEventsAll);
		ImageView imageViewCreateEvent = (ImageView)findViewById(R.id.imageViewEventCreate);
		
		//
		// If we have no items, show this to the user
		// Chances are it will be the first time they have used the service too, so I
		// May want to add more user information than what is there currently.
		//
		if(listEventsAll.getCount() == 0) txtEventsNone.setVisibility(View.VISIBLE);
		else 								txtEventsNone.setVisibility(View.GONE);	
				
		//
		// If the create a new project button is pressed
		//
		imageViewCreateEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = false;
				
				Intent createEvent = new Intent(getApplicationContext(), CreateOrEditEventActivity.class);
				createEvent.putExtra("editing", editing);
				createEvent.putExtra("userId", userId);
				startActivity(createEvent);
				
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
    	imageViewCreateEvent.setImageDrawable(plusImage);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_list_all_events, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if(id == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	    createAndSetEventAdapter();
	}
	
	public void createAndSetEventAdapter()
	{
		userEvents.clear(); // refresh the HashMap ArrayList
		
		//*******************************************************************************************//
		//									create the list view									 //
		//*******************************************************************************************//
		//
		// Get all of the events assigned to a user
		//
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
		query.whereEqualTo("eventCreatedBy", userId);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
		 public void done(List<ParseObject> eventList, ParseException e) {
			 if (e == null) {
		    	        	
			     //
			     // add all items to the array list of HashMaps
			     //
				 for(int i = 0; i < eventList.size(); i++)
		    	 {
					 //
		    	     // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
		    	     //
		    	     HashMap<String,String> userEvent = new HashMap<String, String>();
		    	        		
		    	     userEvent.put("eventId",           (String) eventList.get(i).getObjectId());
		    	     userEvent.put("eventName",        (String) eventList.get(i).get("eventName").toString());
		    	     userEvent.put("eventLocation",   (String) eventList.get(i).get("eventLocation").toString());
		    	     userEvent.put("eventPostcode",    (String) eventList.get(i).get("eventPostcode").toString());
		    	     userEvent.put("eventDescription",     (String) eventList.get(i).get("eventDescription").toString());
		    	     userEvent.put("eventDate",      (String) eventList.get(i).get("eventDate").toString());
		    	     userEvent.put("eventStartTime",     (String) eventList.get(i).get("eventStartTime").toString());
		    	     userEvent.put("eventEndTime", (String) eventList.get(i).get("eventEndTime").toString());
		    	     userEvent.put("eventVisibility", (String) eventList.get(i).get("eventVisibility").toString());
		    	     
		    	     userEvents.add(userEvent);
		    	 }
		    	        	
		    	 if(userEvents.size() >= 0)
		    	 {
		    		 ListAllEventsAdapter allEventsAdapter = new ListAllEventsAdapter(getApplicationContext(), userEvents);
		    	     listEventsAll.setAdapter(allEventsAdapter);
		    	 }
		    	        	
		    	 //
		    	 // If we have no items, show this to the user
		    	 // Chances are it will be the first time they have used the service too, so I
		    	 // May want to add more user information than what is there currently.
		    	 //
		    	 if(listEventsAll.getCount() == 0) txtEventsNone.setVisibility(View.VISIBLE);
		    	 else 								txtEventsNone.setVisibility(View.GONE);
		    	        	
		    	 // Log.d("score", "Project Name " + userProjects.get(0).get("projectEndDate") + " scores"); // debug
		    	 } else Log.d("Events adapter error", "Error: " + e.getMessage());
		    	        
			}

		});		
	}
}
