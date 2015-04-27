package com.easymanage.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easymanage.R;
import com.easymanage.activities.ChooseGroupProjectActivity;
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
 * This activity lists all of the projects that have been created by a User.
 * 
 * @author Adamst
 *
 */
public class UserSelectedGroupProjectsFragment extends Fragment {
	
	private View view;
	private String groupId = "";
	private String userId  = "";
	//private ListView  listGroupProjects; 
    private ArrayList<HashMap<String, String>> userProjectsAll; // Projects currently created by user
	
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
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		final ImageView imageViewCreateGroupProject = (ImageView)view.findViewById(R.id.imageViewGroupProjectCreate);
		//listGroupProjects = (ListView)view.findViewById(R.id.listGroupProjectsAll);			

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
			    	    //userProject.put("projectMembers",     (String) projectList.get(i).get("projectMembers").toString());
			    	    userProject.put("projectDescription", (String) userProjects.get(i).get("projectDescription").toString());
			    	        		
			    	    // Add to the ArrayLists
			    	    userProjectsAll.add(userProject);
					}
					   	
		    	} else Log.d("Files listview Error", "Error: " + e.getMessage());
		    	  
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
}
