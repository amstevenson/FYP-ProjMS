package com.projectinspire.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.projectinspire.R;
import com.projectinspire.activities.CreateOrEditTaskActivity;
import com.projectinspire.adapters.ListAllTasksAdapter;

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

/***
 * 
 * 
 * 
 * @see createAndAssignAdapter for allocating tasks to the ListView in this fragment
 * 
 * @author adam
 *
 */
public class UserSelectedProjectTasksFragment extends Fragment {

	//
	// Get project id 
	//
	private String projectId = "empty";
    private ArrayList<HashMap<String, String>> userTasks;
	private ListView  listTasksAll; 
	private View view;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		view = inflater.inflate(R.layout.fragment_user_selected_project_tasks, container, false);
		
		//
		// retrieve the array containing the elements for the project
		//
		Bundle projectBundle      = getArguments();
		projectId = projectBundle.getString("projectId");
		
		Log.d("Tasks - Project ID", projectId); // for debugging
		
		//
		// Initiate HashMap
		//
		userTasks = new ArrayList<HashMap<String, String>>();
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		ImageView imageViewCreateTask = (ImageView)view.findViewById(R.id.imageViewCreateTask);
		listTasksAll = (ListView) view.findViewById(R.id.listTasksAll);
		
		//*******************************************************************************************//
		//									Create view listeners									 //
		//*******************************************************************************************//
		//
		// If we are creating a new task
		//
		imageViewCreateTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = false; // we are not editing, but creating
				
				Intent createTask = new Intent(view.getContext(), CreateOrEditTaskActivity.class);
				createTask.putExtra("editing", editing);
				createTask.putExtra("projectId", projectId);
				startActivity(createTask);
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
    	imageViewCreateTask.setImageDrawable(plusImage);
		  	
		return view;
	}

	public void setText(String item) {
		//TextView view = (TextView) getView().findViewById(R.id.txtProjectAllTasks);
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
	    
	    Log.d("Tasks resume", "Tasks resume"); // debug
	}
	
	public void createAndAssignAdapter()
	{
		
		//
		// First, clear the arrayList of HashMaps, so that we do not get duplicates of any tasks
		//
		userTasks.clear();
		
		//
		// Get all of the tasks assigned to a user, then create an adapter for the ListView
		// That will contain all of the tasks, and plug information into it. 
		//
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
		query.whereEqualTo("projectId", projectId);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> taskList, ParseException e) {
				if (e == null) {      	
			    	
					//
			    	// add all items to the array list of HashMaps
			    	//
			    	for(int i = 0; i < taskList.size(); i++)
			    	{
			    		//
			    	    // For each of the projects, create a HashMap and add it to the arrayList that will contain all of them
			    	    //
			    	    HashMap<String,String> userTask = new HashMap<String, String>();
			    	        		
			    	    userTask.put("taskId",          (String) taskList.get(i).getObjectId());
			    	    userTask.put("taskName",        (String) taskList.get(i).get("taskName").toString());
			    	    userTask.put("taskStartDate",   (String) taskList.get(i).get("taskStartDate").toString());
			    	    userTask.put("taskEndDate",     (String) taskList.get(i).get("taskEndDate").toString());
			    	    userTask.put("taskPriority",    (String) taskList.get(i).get("taskPriority").toString());
			    	    userTask.put("taskAssignedTo",  (String) taskList.get(i).get("taskAssignedTo").toString());
			    	    userTask.put("taskCategory",    (String) taskList.get(i).get("taskCategory").toString());
			    	    userTask.put("taskDescription", (String) taskList.get(i).get("taskDescription").toString());
			    	    userTask.put("taskStatus",      (String) taskList.get(i).get("taskStatus").toString());    		
			    	    
			    	    userTasks.add(userTask);
			    	}
			    	
			    	Log.d("Tasks size", String.valueOf(userTasks.size())); // debug
			    	
			    	//
			    	// Create adapter and assign to ListView
			    	//
			    	if(userTasks.size() >= 0) // more than or equal to...because of course it starts at 0...
			    	{
			    		ListAllTasksAdapter allProjectsAdapter = new ListAllTasksAdapter(view.getContext(), userTasks);
			    		listTasksAll.setAdapter(allProjectsAdapter);
			    		
			    		//allProjectsAdapter.notifyDataSetChanged();
			    	}
			    	        	
			    	//
			    	// If we have no items, show this to the user
			    	// Chances are it will be the first time they have used the service too, so I
			    	// May want to add more user information than what is there currently.
			    	//
			    	//if(listProjectsAll.getCount() == 0) txtProjectsNone.setVisibility(View.VISIBLE);
			    	//else 								txtProjectsNone.setVisibility(View.GONE);
			    	        	
			    	// Log.d("score", "Project Name " + userProjects.get(0).get("projectEndDate") + " scores"); // debug
			    	
		    	} else Log.d("Tasks listview Error", "Error: " + e.getMessage());
		    	        
		    }

		});
	}
	
}
