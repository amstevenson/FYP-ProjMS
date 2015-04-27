package com.easymanage.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easymanage.R;
import com.easymanage.adapters.ListAllGroupsAdapter;
import com.easymanage.utilities.NetworkStateOperations;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
 * This activity lists all groups created by the user. With aid from a custom
 * adapter.
 * 
 * @author Adam Stevenson
 *
 */
public class UserListAllGroupsActivity extends Activity {

	private String userId = "";
    private ArrayList<HashMap<String, String>> userGroups;
    private ListView listGroupsAll;
    private TextView txtGroupsNone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list_all_groups);
		
		setTitle("Groups");
		
		//
		// Initiate HashMap of groups
		//
		userGroups = new ArrayList<HashMap<String, String>>();
		
		//*******************************************************************************************//
		//									Retrieve user information								 //
		//*******************************************************************************************//
		//
		// Retrieve the user Id
		//
		Intent intent = getIntent();
		    	
		userId		  = intent.getStringExtra("userId");
				
		Log.d("List all groups - User Id", userId);
			
		//*******************************************************************************************//
    	//											Views								 		     //
    	//*******************************************************************************************//	
		txtGroupsNone = (TextView)findViewById(R.id.txtGroupsNone);
		listGroupsAll = (ListView)findViewById(R.id.listGroupsAll);
		ImageView imageViewCreateGroup = (ImageView)findViewById(R.id.imageViewGroupCreate);
		
		// If there are no groups (which there most probably will not be at this step)
		if(listGroupsAll.getCount() == 0) txtGroupsNone.setVisibility(View.VISIBLE);
		else 							  txtGroupsNone.setVisibility(View.GONE);	
				
		//
		// If the create a new project button is pressed
		//
		imageViewCreateGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = false;
				
				Intent createGroup = new Intent(getApplicationContext(), CreateOrEditGroupActivity.class);
				createGroup.putExtra("editing", editing);
				createGroup.putExtra("userId", userId);
				startActivity(createGroup);
				
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
    	imageViewCreateGroup.setImageDrawable(plusImage);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_list_all_groups, menu);
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
	    createAndSetEventAdapter();
	}
	
	public void createAndSetEventAdapter()
	{
		userGroups.clear(); // refresh the HashMap ArrayList
		
		//*******************************************************************************************//
		//									create the list view									 //
		//*******************************************************************************************//
		//
		// Get all of the groups assigned to a user
		//
		if(NetworkStateOperations.testNetworkConnection(getApplicationContext()))
		{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("GroupMember");
			query.whereEqualTo("groupUserId", userId);
			query.orderByDescending("createdAt");
			query.findInBackground(new FindCallback<ParseObject>() {
			 public void done(List<ParseObject> groupMemberList, ParseException e) {
				 if (e == null) {
			    	        	
				     //
				     // add all items to the array list of HashMaps
				     //
					 for(int i = 0; i < groupMemberList.size(); i++)
			    	 {
						 //
						 // Get the group Member
						 //
						 String groupId = groupMemberList.get(i).get("groupId").toString();
						 
						 ParseQuery<ParseObject> groupQuery = ParseQuery.getQuery("Group");
						 groupQuery.whereEqualTo("objectId", groupId);
						 groupQuery.orderByDescending("createdAt");
						 groupQuery.findInBackground(new FindCallback<ParseObject>() {
	
							@Override
							public void done(List<ParseObject> groupList, ParseException e1) {
								
								if(e1 == null)
								{
									for(int j = 0; j < groupList.size(); j++)
									{
										//
										// Get all groups allocated to the group member
										//
							    	    HashMap<String,String> userGroup = new HashMap<String, String>();
					    	        		
							    	    userGroup.put("groupId",          (String) groupList.get(j).getObjectId());
							    	    userGroup.put("groupName",        (String) groupList.get(j).get("groupName").toString());
							    	    userGroup.put("groupDescription", (String) groupList.get(j).get("groupDescription").toString());
		
							    	    userGroups.add(userGroup);
							    	}
								}
								else
								{
									Log.d("Groups retrieval error", "Error: " + e1.getMessage());
								}
								
						    	if(groupList.size() >= 0)
						    	{
						    		ListAllGroupsAdapter allGroupsAdapter = new ListAllGroupsAdapter(getApplicationContext(), userGroups, userId);
						    		listGroupsAll.setAdapter(allGroupsAdapter);
						    	}
						    	 
						    	//
						    	// If we have no items, show this to the user
						    	// Chances are it will be the first time they have used the service too, so I
						    	// May want to add more user information than what is there currently.
						    	//
						    	if(listGroupsAll.getCount() == 0) txtGroupsNone.setVisibility(View.VISIBLE);
						    	else 							  txtGroupsNone.setVisibility(View.GONE);
						    	        	
							}
						 }); 
			    	 }     	
				 	 } else Log.d("Group Member retrieval error", "Error: " + e.getMessage());        
				}
			});		
		}
	}
}
