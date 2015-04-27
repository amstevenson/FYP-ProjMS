package com.easymanage.activities;

import java.util.ArrayList;
import com.easymanage.R;
import com.parse.ParseObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * An activity that presents a list of projects. These projects are linked
 * directly with the users account. If a project is clicked on, the user will
 * have the option to add it to the group or not. 
 * 
 * @author Adam Stevenson
 *
 */
public class ChooseGroupProjectActivity extends ListActivity {
	
    private ArrayList<String> projectIds;
    private ArrayList<String> projectNames;
    private String			  groupId = "";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_group_project);
		
		// Set the title
		setTitle("Choose a Project to Add");
		
		//
		// Initiate HashMap
		//
		projectIds = new ArrayList<String>();
		projectNames = new ArrayList<String>();
		
		//
		// Get extras
		//
		Intent intent = getIntent();
		projectIds    = intent.getStringArrayListExtra("projectIds");
		projectNames  = intent.getStringArrayListExtra("projectNames");
		groupId 	  = intent.getStringExtra("groupId");
		
		Log.d("GroupID:" ,"Choose group projects: Group Id = " + groupId);
		
		if(projectNames.size() > 0)
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, projectNames ));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_group_project, menu);
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
    protected void onListItemClick(ListView l, View v, final int position, long id){
	
		super.onListItemClick(l, v, position, id);
		
		//
		// If the user wishes to save the file, create a new database row
		//
	    new AlertDialog.Builder(v.getContext())
	     .setIcon(android.R.drawable.ic_dialog_alert)
	     .setTitle("Add this group")
	     .setMessage("Are you sure you want to add this project to your group?")
	     .setPositiveButton("Add", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				// Progress Dialog
			    final ProgressDialog pDialog;
	            pDialog = new ProgressDialog(ChooseGroupProjectActivity.this);
	            pDialog.setMessage("Adding project: '" + projectNames.get(position) + "' to the group..." +
	            		"this will only take a moment.");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
            	
				//
				// Create a new Parse Object and add it to database (In this case a project)
				//
	    		Log.d("GroupID:" ,"Inside of Parse (create group project): Group Id = " + groupId);
	            
				ParseObject newProject = new ParseObject("GroupProject");
				newProject.put("groupId", groupId);
				newProject.put("groupProjectId", projectIds.get(position));
				newProject.saveInBackground();
	            
	            final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
				    @Override
				    public void run() {
				        
				    	//
				    	// Finish the updating process
				    	//
				    	if(pDialog.isShowing()) pDialog.dismiss();
				    	
				    	Toast toast = Toast.makeText(getApplicationContext(), "Project: '"
								+ projectNames.get(position) +  "' has been added to the group." , Toast.LENGTH_LONG);
						toast.show();
						
						finish();
				    }
				}, 2000);
				
			}
	     })
	     .setNegativeButton("Cancel", null)
	     .show();
		
	}
		
	
}
