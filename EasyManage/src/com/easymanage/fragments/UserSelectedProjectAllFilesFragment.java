package com.easymanage.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easymanage.activities.ChooseFileActivity;
import com.easymanage.adapters.ListAllFilesAdapter;
import com.easymanage.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
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
import android.widget.ListView;

/**
 * 
 * This activity lists all of the files that have been selected and downloaded to the
 * EasyManage directory, where all of the files are stored.
 * 
 * @author Adam Stevenson
 *
 */
public class UserSelectedProjectAllFilesFragment extends Fragment {

    private ArrayList<HashMap<String, String>> userFilesInformation;
	private ArrayList<HashMap<String, ParseFile>> userParseFiles;
	private ListView  listFilesAll; 
	private String projectId = "";
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		view = inflater.inflate(R.layout.fragment_user_selected_project_all_files, container, false);

		// retrieve the array containing the elements for the project
		Bundle projectBundle      = getArguments();
		Log.d("Files - Project ID", projectBundle.getString("projectId"));
		
		//
		// Get project id 
		//
		projectId = projectBundle.getString("projectId");
		
		//
		// Initiate HashMap
		//
		userFilesInformation = new ArrayList<HashMap<String, String>>();
		userParseFiles		 = new ArrayList<HashMap<String, ParseFile>>();
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		ImageView imageViewCreateFile = (ImageView)view.findViewById(R.id.imageViewCreateFile);
		listFilesAll = (ListView)view.findViewById(R.id.listFilesAll);			

		//*******************************************************************************************//
		//									Create view listeners									 //
		//*******************************************************************************************//
		//
		// If we are creating a new task
		//
		imageViewCreateFile.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
						
								
				Intent createFile = new Intent(view.getContext(), ChooseFileActivity.class);
				createFile.putExtra("projectId", projectId);
				startActivity(createFile);		
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
		imageViewCreateFile.setImageDrawable(plusImage);
				
		return view;
	}

	public void setText(String item) {
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
	    
	    Log.d("Files have resumed", "Files have resumed"); // debug
	}
	
	public void createAndAssignAdapter()
	{
		
		//
		// First, clear the arrayList of HashMaps, so that we do not get duplicates of any tasks
		//
		userFilesInformation.clear();
		
		//
		// Get all of the tasks assigned to a user, then create an adapter for the ListView
		// That will contain all of the tasks, and plug information into it. 
		//
		ParseQuery<ParseObject> query = ParseQuery.getQuery("File");
		query.whereEqualTo("fileProjectId", projectId);
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> fileList, ParseException e) {
				if (e == null) {      	
			    	
					//
			    	// add all items to the array list of HashMaps
			    	//
			    	for(int i = 0; i < fileList.size(); i++)
			    	{
			    		//
			    	    // For each part of the files information, create a HashMap and add it to the 
			    		// arrayList that will contain all of them
			    	    //
			    	    HashMap<String,String> userFileInfo = new HashMap<String, String>();
			    	        		
			    	    userFileInfo.put("fileId",          (String) fileList.get(i).getObjectId());
			    	    userFileInfo.put("fileProjectId",   (String) fileList.get(i).get("fileProjectId").toString());
			    	    userFileInfo.put("fileName",   		(String) fileList.get(i).get("fileName").toString());
			    	    userFileInfo.put("fileExtension",   (String) fileList.get(i).get("fileExtention").toString());
			    	    userFileInfo.put("createdAt",       fileList.get(i).getCreatedAt().toString());
			    	    
			    	    // Get and calculate size
			    	    long size = fileList.get(i).getLong("fileSize");
			    	    size = size / 1000; 
			    	    
			    	    userFileInfo.put("fileSize", String.valueOf(size) + " KB");
			    	    
			    	    //
			    	    // Add the ParseFile to a HashMap and then add it to the ArrayList
			    	    //
			    	    HashMap<String, ParseFile> userParseFile = new HashMap<String, ParseFile>();
			    	    
			    	    userParseFile.put("file",  			(ParseFile) fileList.get(i).get("file"));  		
			    	    
			    	    // Add to the ArrayLists
			    	    userFilesInformation.add(userFileInfo);
			    	    userParseFiles.add(userParseFile);
			    	}
			    	
			    	Log.d("files size", String.valueOf(userFilesInformation.size())); // debug
			    	
			    	//
			    	// Create adapter and assign to ListView
			    	//
			    	if(userFilesInformation.size() >= 0) // more than or equal to...because of course it starts at 0...
			    	{
			    		ListAllFilesAdapter allTasksAdapter = new ListAllFilesAdapter(view.getContext(), userFilesInformation,
			    												userParseFiles);
			    		listFilesAll.setAdapter(allTasksAdapter);
			    		
			    	}
			    	        	
		    	} else Log.d("Files listview Error", "Error: " + e.getMessage());
		    	        
		    }

		});
	}
}
