package com.easymanage.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easymanage.R;
import com.easymanage.activities.ChooseGroupProjectActivity;
import com.easymanage.activities.UserSelectedProjectActivity;
import com.easymanage.adapters.ListAllProjectsAdapter;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * This activity lists all of the projects that have been created by a User.
 * 
 * @author Adamst
 *
 */
public class UserSelectedGroupProjectsFragment extends Fragment {
	
	private View view;
	private String groupId = "";
	private String userId  = "";
	private ListView  listGroupProjects; 
    private ArrayList<HashMap<String, String>> userProjectsAll;   // Projects currently created by user
    
    //
    // The arraylists that will be used to store the information about the group project identifers (groupProjectId)
    // and the projects themselves (which will be the projectId and the associated variables)
    //
	private ArrayList<HashMap<String, String>> userGroupProjectIdentifers; // The identifers for each group project
    private ArrayList<HashMap<String, String>> userGroupProjectList;       // The projects that belong to a group
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_user_selected_group_projects, container, false);

		// retrieve the array containing the elements for the project
		Bundle projectBundle      = getArguments();

		//
		// Get group and user Id
		//
		groupId = projectBundle.getString("groupId");
		userId  = projectBundle.getString("userId");
		
		Log.d("GroupID:" ,"Group details: Group Id = " + groupId);
		
		//
		// Initiate HashMap
		//
		userProjectsAll = new ArrayList<HashMap<String, String>>();
		userGroupProjectIdentifers = new ArrayList<HashMap<String, String>>();
		userGroupProjectList = new ArrayList<HashMap<String, String>>();
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		final ImageView imageViewCreateGroupProject = (ImageView)view.findViewById(R.id.imageViewGroupProjectCreate);
		listGroupProjects = (ListView)view.findViewById(R.id.listGroupProjectsAll);			

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
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
		query.whereEqualTo("projectCreatedBy", userId);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(final List<ParseObject> userProjects, ParseException e) {
				
				if (e == null) {      	
			    	
					for(int i = 0; i < userProjects.size(); i ++)
					{ 		
			    	    //
			    	    // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
			    	    //
			    	    HashMap<String,String> userProject = new HashMap<String, String>();
			    	        		
			    	    userProject.put("projectId",           (String) userProjects.get(i).getObjectId());
			    	    userProject.put("projectName",        (String) userProjects.get(i).get("projectName").toString());
			    	    userProject.put("projectStartDate",   (String) userProjects.get(i).get("projectStartDate").toString());
			    	    userProject.put("projectEndDate",     (String) userProjects.get(i).get("projectEndDate").toString());
			    	    userProject.put("projectStatus",      (String) userProjects.get(i).get("projectStatus").toString());
			    	    userProject.put("projectDescription", (String) userProjects.get(i).get("projectDescription").toString());
			    	        		
			    	    // Add to the ArrayLists
			    	    userProjectsAll.add(userProject);
					}
					   	
		    	} else Log.d("Projects retrieval Error", "Error: " + e.getMessage());
		    	  
				imageViewCreateGroupProject.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						ArrayList<String> projectIds   = new ArrayList<String>();
						ArrayList<String> projectNames = new ArrayList<String>();
						
						//
						// Get the names of the projects and the Id
						//
						for(int i = 0; i < userProjectsAll.size(); i++)
						{
							projectIds.add(userProjectsAll.get(i).get("projectId"));
							projectNames.add(userProjectsAll.get(i).get("projectName"));
						}
						
						//
						// Send to the intent where the user can select a project
						//
						Intent intent = new Intent(v.getContext(), ChooseGroupProjectActivity.class);
						intent.setFlags(268435456); // Put activity on the back stack
						intent.putStringArrayListExtra("projectIds", projectIds);
						intent.putStringArrayListExtra("projectNames", projectNames);
						intent.putExtra("groupId", groupId);
						v.getContext().startActivity(intent);
					}
				});
		    }
		});
		
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
		listGroupProjects.setOnItemClickListener(new OnItemClickListener() {
		    	 
			@Override
		    public void onItemClick(AdapterView<?> parent, View view,
		    		int position, long id) {
		    	               
				if (userGroupProjectList.size() > 0)
		    	{
					try {
		    		            	
						//
		    		    // Retrieve the correct project that will be used to send the user to the relevant task activity
		    		    //  		
		    		    String objectToString = listGroupProjects.getItemAtPosition(position).toString();
		    		            		
		    		    int selectedIndex = 0;
		    		            		
		    		    // for each property compare against the selected ListView object.
		    		    // if they match we have our correct index.
		    			for(int i = 0; i < userGroupProjectList.size(); i ++)
		    			{
		    			           			
		    				String chosenProject = userGroupProjectList.get(i).get("projectName").toString();
		    			            			
		    			    if(objectToString.contains("projectName=" + chosenProject)) selectedIndex = i;
		    			}
		    			            	
		    		    //
		    			// Once the project has been selected, and the right objectId has been collected
		    			// move the user onto the project activity; where all of the tasks/files are located.
		    			//
		    			Intent selectedProjectIntent = new Intent(getActivity().getApplicationContext(), UserSelectedProjectActivity.class);
		    		    selectedProjectIntent.putExtra("projectId", userGroupProjectList.get(selectedIndex).get("projectId"));
		    			startActivity(selectedProjectIntent);
		    			        
		    		} catch (IndexOutOfBoundsException e) {
		    		       		e.printStackTrace();
		    		            		
		    		}
		    		            		
		    	}
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
		imageViewCreateGroupProject.setImageDrawable(plusImage);
				
	
		return view;
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
	    
	    Log.d("Group projects resume", "Group projects resume"); // debug
	}
	
	public void createAndAssignAdapter()
	{
		
		//
		// First, clear the arrayList of HashMaps, so that we do not get duplicates of any tasks
		//
		if(userGroupProjectIdentifers.size() > 0) userGroupProjectIdentifers.clear();
		if(userGroupProjectList.size() > 0) 	  userGroupProjectList.clear();
		
		//
		// Get all of the tasks assigned to a user, then create an adapter for the ListView
		// That will contain all of the tasks, and plug information into it. 
		//
		ParseQuery<ParseObject> groupIdentiferQuery = ParseQuery.getQuery("GroupProject");
		groupIdentiferQuery.whereEqualTo("groupId", groupId);
		groupIdentiferQuery.orderByDescending("createdAt");
		
		if(NetworkStateOperations.testNetworkConnection(getActivity().getApplicationContext()))
			groupIdentiferQuery.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> groupProjectIdentiferList, ParseException e) {
					if (e == null) {      	
				    	
						if(groupProjectIdentiferList.size() > 0)
						{
							//
					    	// add all items to the array list of HashMaps
					    	//
					    	for(int i = 0; i < groupProjectIdentiferList.size(); i++)
					    	{
					    		//
					    	    // For each of the tasks, create a HashMap and add it to the arrayList that will contain all of them
					    	    //
					    	    HashMap<String,String> userGroupProjectIdentifier = new HashMap<String, String>();
					    	        		
					    	    userGroupProjectIdentifier.put("objectId",       (String) groupProjectIdentiferList.get(i).getObjectId());
					    	    userGroupProjectIdentifier.put("groupId",        (String) groupProjectIdentiferList.get(i).get("groupId").toString());
					    	    userGroupProjectIdentifier.put("groupProjectId", (String) groupProjectIdentiferList.get(i).get("groupProjectId").toString());;    		
					    	    
					    	    userGroupProjectIdentifers.add(userGroupProjectIdentifier);
					    	}
					    	
					    	//
					    	// Create a list of queries that will come together to make the "OR" constraints
					    	// of the database call to Parse
					    	//
					    	List<ParseQuery<ParseObject>> groupProjectOrQueries = new ArrayList<ParseQuery<ParseObject>>();
					    	
					    	for(int i = 0; i < groupProjectIdentiferList.size(); i++)
					    	{
					    		ParseQuery<ParseObject> projectOrQuery = ParseQuery.getQuery("Project");
					    		projectOrQuery.whereEqualTo("objectId", groupProjectIdentiferList.get(i).get("groupProjectId"));
					    		groupProjectOrQueries.add(projectOrQuery);
					    	}
					    	
					    	//
					    	// Find the actual projects, by referencing the "project" table
					    	//
					    	ParseQuery<ParseObject> groupProjectListQuery = ParseQuery.or(groupProjectOrQueries);
					    	groupProjectListQuery.findInBackground(new FindCallback<ParseObject>() {
		
								@Override
								public void done(List<ParseObject> groupProjectsList, ParseException e2) {
									
									if(e2 == null)
									{
										for(int i = 0; i < groupProjectsList.size(); i++)
										{
											
											 //
								    	     // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
								    	     //
								    	     HashMap<String,String> groupProject = new HashMap<String, String>();
								    	        		
								    	     groupProject.put("projectId",          (String) groupProjectsList.get(i).getObjectId());
								    	     groupProject.put("projectName",        (String) groupProjectsList.get(i).get("projectName").toString());
								    	     groupProject.put("projectStartDate",   (String) groupProjectsList.get(i).get("projectStartDate").toString());
								    	     groupProject.put("projectEndDate",     (String) groupProjectsList.get(i).get("projectEndDate").toString());
								    	     groupProject.put("projectStatus",      (String) groupProjectsList.get(i).get("projectStatus").toString());
								    	     groupProject.put("projectDescription", (String) groupProjectsList.get(i).get("projectDescription").toString());
								    	        		
								    	     userGroupProjectList.add(groupProject);
											
										}
										
										//
										// Create adapter and assign to ListView
										//
										if(userGroupProjectList.size() >= 0)
										{
											ListAllProjectsAdapter alltasksAdapter = new ListAllProjectsAdapter(view.getContext(), userGroupProjectList);
											listGroupProjects.setAdapter(alltasksAdapter);
											
											//
											// Change the visibility of the text view that tells the user to add a group
											// project, in the case where none has been added.
											//
											TextView groupProjectsNone = (TextView) getActivity().findViewById(R.id.txtGroupProjectsNone);
											groupProjectsNone.setVisibility(View.GONE);
										}
										
									}
									else Log.d("Group projects retrieval error", "Group projects retrieval error: " + e2.getLocalizedMessage());
								}
							});
					    	
					    	
					    	Log.d("Group projects identifiers size", "Group projects identifiers size: " + String.valueOf(groupProjectIdentiferList.size()));
						}
						else Log.d("No groups retrieved.", "No groups retrieved, there could be no projects allocated to the group");
				    	
			    	} else Log.d("Groups retrieval listview Error", "Group projects retrieval error: " + e.getMessage());       
			    }
			});
	}
}
