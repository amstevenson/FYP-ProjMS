package com.easymanage.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easymanage.adapters.ListAllProjectsAdapter;
import com.easymanage.utilities.NetworkStateOperations;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * This activity lists all of the projects that have been 
 * created by the user. 
 * 
 * @author Adam Stevenson
 *
 */
public class UserListAllProjectsActivity extends Activity {

	private String userId = "";
    private ArrayList<HashMap<String, String>> userProjects;
    private ListView listProjectsAll;
    private TextView txtProjectsNone;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list_projects);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Projects");
		
		//
		// Initiate HashMap
		//
		userProjects = new ArrayList<HashMap<String, String>>();
		
		//*******************************************************************************************//
		//											Views								 		     //
		//*******************************************************************************************//
		txtProjectsNone  = (TextView) findViewById(R.id.txtProjectsNone);
		listProjectsAll  = (ListView) findViewById(R.id.listProjectsAll);
		ImageView imageViewCreateProject = (ImageView)findViewById(R.id.imageViewProjectCreate);
		EditText  editSearchProjects     = (EditText) findViewById(R.id.editSearchProjects);
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user Id
    	//
    	Intent intent = getIntent();
    	userId		  = intent.getStringExtra("userId");
		
    	Log.d("All Projects - Id", userId); // for debugging
		
		//*******************************************************************************************//
		//									Click listener for the list                     		 //
    	//								For selecting a specific project 				   			 //
		//*******************************************************************************************//
    	// on selecting single product, send the user
    	// to the screen that deals with listing more
    	// detailed information
    	//
    	// @see createAndSetProjectAdapter()
    	//
    	listProjectsAll.setOnItemClickListener(new OnItemClickListener() {
    	 
    		@Override
    	    public void onItemClick(AdapterView<?> parent, View view,
    	    		int position, long id) {
    	               
    			if (userProjects !=null)
    		    {
    				try {
    		            	
    					//
    		            // Retrieve the correct project that will be used to send the user to the relevant task activity
    		            //
    		            ListView chosenPropertyIndex = (ListView) findViewById(R.id.listProjectsAll);
    		            		
    		            String objectToString = chosenPropertyIndex.getItemAtPosition(position).toString();
    		            		
    		            int selectedIndex = 0;
    		            		
    		            // for each property compare against the selected ListView object.
    		            // if they match we have our correct index.
    			        for(int i = 0; i < userProjects.size(); i ++)
    			        {
    			           			
    			        	String chosenProject = userProjects.get(i).get("projectName").toString();
    			            			
    			            if(objectToString.contains("projectName=" + chosenProject)) selectedIndex = i;
    			        }
    			            	
    			        //
    			        // Once the project has been selected, and the right objectId has been collected
    			        // move the user onto the project activity; where all of the tasks/files are located.
    			        //
    			        Intent selectedProjectIntent = new Intent(getApplicationContext(), UserSelectedProjectActivity.class);
    			        selectedProjectIntent.putExtra("projectId", userProjects.get(selectedIndex).get("projectId"));
    			        startActivity(selectedProjectIntent);
    			        
    		       } catch (IndexOutOfBoundsException e) {
    		       		e.printStackTrace();
    		            		
    		       }
    		            		
    	       }
    	    }
    	});
    	
		//*******************************************************************************************//
		//									View On click listeners           						 //
		//*******************************************************************************************//			
		//
		// If the create a new project button is pressed
		//
		imageViewCreateProject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = false;
				
				Intent createProject = new Intent(getApplicationContext(), CreateOrEditProjectActivity.class);
				createProject.putExtra("editing", editing);
				createProject.putExtra("userId", userId);
				startActivity(createProject);
				
				finish();
			}
		});
		
    	
		//*******************************************************************************************//
		//									View settings    										 //
		//*******************************************************************************************//
    	editSearchProjects.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    	    @Override
    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    	        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
    	            //performSearch();
    	            return true;
    	        }
    	        return false;
    	    }

    	});
		
		//*******************************************************************************************//
		//									Create and assign the images							 //
		//*******************************************************************************************//
        //
		// Plus symbol for adding projects
		//
        Drawable plusImage = getResources().getDrawable(R.drawable.icon_add_48dp_black_circle);

        //
    	// Assign the drawable to the ImageView
        //
    	imageViewCreateProject.setImageDrawable(plusImage);
    	
    	//
    	// Set the project adapter
    	//
    	//createAndSetProjectAdapter();
    	
    	getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public void createAndSetProjectAdapter()
	{
		userProjects.clear(); // refresh the HashMap ArrayList
		
		//*******************************************************************************************//
		//									create the list view									 //
		//*******************************************************************************************//
		//
		// Get all of the projects assigned to a user
		//
		if(NetworkStateOperations.testNetworkConnection(getApplicationContext()))
		{
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
			query.whereEqualTo("projectCreatedBy", userId);
			query.orderByDescending("createdAt");
			query.findInBackground(new FindCallback<ParseObject>() {
			 public void done(List<ParseObject> projectList, ParseException e) {
				 if (e == null) {
			    	        	
				     //
				     // add all items to the array list of HashMaps
				     //
					 for(int i = 0; i < projectList.size(); i++)
			    	 {
						 //
			    	     // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
			    	     //
			    	     HashMap<String,String> userProject = new HashMap<String, String>();
			    	        		
			    	     userProject.put("projectId",           (String) projectList.get(i).getObjectId());
			    	     userProject.put("projectName",        (String) projectList.get(i).get("projectName").toString());
			    	     userProject.put("projectStartDate",   (String) projectList.get(i).get("projectStartDate").toString());
			    	     userProject.put("projectEndDate",     (String) projectList.get(i).get("projectEndDate").toString());
			    	     userProject.put("projectStatus",      (String) projectList.get(i).get("projectStatus").toString());
			    	     //userProject.put("projectMembers",     (String) projectList.get(i).get("projectMembers").toString());
			    	     userProject.put("projectDescription", (String) projectList.get(i).get("projectDescription").toString());
			    	        		
			    	     userProjects.add(userProject);
			    	 }
			    	        	
			    	 if(userProjects.size() >= 0)
			    	 {
			    		 ListAllProjectsAdapter allProjectsAdapter = new ListAllProjectsAdapter(getApplicationContext(), userProjects);
			    	     listProjectsAll.setAdapter(allProjectsAdapter);
			    	 }
			    	        	
			    	 //
			    	 // If we have no items, show this to the user
			    	 // Chances are it will be the first time they have used the service too, so I
			    	 // May want to add more user information than what is there currently.
			    	 //
			    	 if(listProjectsAll.getCount() == 0) txtProjectsNone.setVisibility(View.VISIBLE);
			    	 else 								txtProjectsNone.setVisibility(View.GONE);
			    	        	
			    	 // Log.d("score", "Project Name " + userProjects.get(0).get("projectEndDate") + " scores"); // debug
			    	 } else Log.d("score", "Error: " + e.getMessage());
			    	        
				}
	
			});		
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.projects, menu);
		return true;
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
	    createAndSetProjectAdapter();
	    
	    Log.d("List projects is now resumed", "List projects resumed");
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
			
			Log.d("on back pressed", "in items selected");

			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
