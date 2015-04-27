package com.easymanage.activities;

import java.util.List;

import com.easymanage.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * An activity that allows the user to edit or create an instance
 * of a group object. 
 * 
 * @author Adam Stevenson
 *
 */
public class CreateOrEditGroupActivity extends Activity {

	private Boolean editing = false;
	private String  userId  = "";
	private String  groupId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_group);
		
		setTitle("Create Group"); // will change dependent on edit or create
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user Id
    	//
    	Intent intent = getIntent();
    	
    	// retrieve the boolean that determines if we are creating or editing
    	editing       = intent.getBooleanExtra("editing", editing);
    	
    	if(!editing) userId		  = intent.getStringExtra("userId");
    		
    	Log.d("Create Group - User Id", userId); // for debugging
    	Log.d("Editing ", String.valueOf(editing));
    	
    	//*******************************************************************************************//
    	//											Views											 //
    	//*******************************************************************************************//
    	final TextView groupLabel       = (TextView) findViewById(R.id.txtCreateGroupsUserHelp);
    	final EditText groupName        = (EditText) findViewById(R.id.editCreateGroupName);
    	final EditText groupDescription    = (EditText) findViewById(R.id.editCreateGroupNotes);

    	final Button   groupCreateEvent = (Button)   findViewById(R.id.btnCreateGroup);
    	final Button   groupDeleteEvent = (Button)   findViewById(R.id.btnDeleteGroup);
    	
    	//*******************************************************************************************//
    	//									If Editing/deleting										 //
    	//*******************************************************************************************//
    	//
    	// Set text for user help if editing or deleting
    	//
    	if(editing)
    	{
    		//
    		// change use user help description
    		//
    		groupLabel.setText("To edit this group, please make all changes and press the 'edit'" +
    				" button below. \n To Delete, press the 'delete' button. ");
    			
    		// retrieve the projectId from the intent
    		groupId = intent.getStringExtra("groupId");
    				
    		Log.d("Create group - group ID", groupId); // for debugging
    				
    		//
    		// Get the relative information for the project, and set the information
    		//
    		ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
    			query.whereEqualTo("objectId", groupId);
    			query.findInBackground(new FindCallback<ParseObject>() {
    				public void done(List<ParseObject> group, ParseException e) {
    						
    					if (e == null) {      	
    					    	
	    					//
	    					// Check that there is a project (or more than one, I guess), and
	    					// then change the views so that the old data is shown
	    					//
	    					if(group.size() > 0)
	    					{
	    						groupName.setText(group.get(0).getString("groupName"));
	    						groupDescription.setText(group.get(0).getString("groupDescription"));
	    							
	    					}   
	    					
    					} else Log.d("score", "Event error: " + e.getMessage());
    			}
    		});
    			
    	    //
    	    // Change the text for the button (boolean determines on click function, but text needs to be changed
    	    // either way
    	    //
    	    groupCreateEvent.setText("UPDATE");
    	}
    	
    	//*******************************************************************************************//
    	//									If NOT Editing/deleting									 //
    	//*******************************************************************************************//
    	else if(!editing)
    	{
    		//
    		// Make delete button invisible
    		//
    		groupDeleteEvent.setVisibility(View.GONE);			
    	}
    	
    	//
		// Create event
		//
		groupCreateEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// if creating/ not editing
				// 
				if(!editing)
				{
					//
					// make sure that no fields are empty
					//
					if(groupName.getText().toString().equals("") || groupDescription.getText().toString().equals(""))
					{
						Toast toast = Toast.makeText(getApplicationContext(), 
								"One or more fields are empty, please revise and try again", Toast.LENGTH_LONG);
						toast.show();
					}
					else
					{
						//
						// Create a new Parse Object and add it to database
						//
						ParseObject newGroup = new ParseObject("Group");
						newGroup.put("groupCreatedBy", userId);
						newGroup.put("groupName", groupName.getText().toString());
						newGroup.put("groupDescription", groupDescription.getText().toString());
						newGroup.saveInBackground();
						
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(CreateOrEditGroupActivity.this);
			            pDialog.setMessage("Creating the group: " + groupName.getText().toString());
			            pDialog.setIndeterminate(false);
			            pDialog.setCancelable(true);
			            pDialog.show();
		            	
						final Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
						    @Override
						    public void run() {
						        
						    	//
						    	// Find the object Id of the new group, and create a new GroupMember object
						    	//
						    	ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
				    			query.whereEqualTo("groupCreatedBy", userId);
				    			query.whereEqualTo("groupName", groupName.getText().toString());
				    			query.findInBackground(new FindCallback<ParseObject>() {
				    				public void done(List<ParseObject> group, ParseException e) {
				    						
				    					if (e == null) {      	
				    					    	
					    					if(group.size() > 0)
					    					{
					    						ParseObject newGroupMember = new ParseObject("GroupMember");
					    						newGroupMember.put("groupId", group.get(0).getObjectId());
					    						newGroupMember.put("groupUserId", userId);
					    						newGroupMember.saveInBackground();
					    						
										    	//
										    	// Finish the Creation process
										    	//
										    	if(pDialog.isShowing()) pDialog.dismiss();
										    	
												Toast toast = Toast.makeText(getApplicationContext(), "Group: '"
														+ groupName.getText().toString() + "' has been created." , Toast.LENGTH_LONG);
												toast.show();
												
												finish();
					    					}   
					    					
				    					} else Log.d("score", "Group Member creation error: " + e.getMessage());
				    				}
				    			});
						    }
						}, 2000);
					}
				}
				if(editing)
				{
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
						 
					// Retrieve the object by id
					query.getInBackground(groupId, new GetCallback<ParseObject>() {
						public void done(ParseObject group, ParseException e) {
						    if (e == null) {
						      
						    	//
						    	// Update data
						    	//
						    	group.put("groupName", groupName.getText().toString());
						    	group.put("groupDescription", groupDescription.getText().toString());
						    	group.saveInBackground();
						    	
								// Progress Dialog
							    final ProgressDialog pDialog;
					            pDialog = new ProgressDialog(CreateOrEditGroupActivity.this);
					            pDialog.setMessage("Updating the details for the group: " + groupName.getText().toString());
					            pDialog.setIndeterminate(false);
					            pDialog.setCancelable(true);
					            pDialog.show();
				            	
								final Handler handler = new Handler();
								handler.postDelayed(new Runnable() {
								    @Override
								    public void run() {
								        
								    	//
								    	// Finish the updating process
								    	//
								    	if(pDialog.isShowing()) pDialog.dismiss();
								    	
								    	Toast toast = Toast.makeText(getApplicationContext(), "Group '"
												+ groupName.getText().toString() + "' has been updated." , Toast.LENGTH_LONG);
										toast.show();
										
										finish();
								    }
								}, 2000);
						    }
						}
					});
				}
			}
		});
		
		
		//
		// If the delete button is clicked
		//
		groupDeleteEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// Create dialog button for confirmation for deleting the project
				//
			    new AlertDialog.Builder(v.getContext())
			     .setIcon(android.R.drawable.ic_dialog_alert)
			     .setTitle("Delete Group")
			     .setMessage("Are you sure you want to delete this group? This change will be permanent.")
			     .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

						//
						// Delete project
						//
						ParseObject.createWithoutData("Group", groupId).deleteInBackground();
							
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(CreateOrEditGroupActivity.this);
			            pDialog.setMessage("Deleting the group: " + groupName.getText().toString());
			            pDialog.setIndeterminate(false);
			            pDialog.setCancelable(true);
			            pDialog.show();
		            	
						final Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
						    @Override
						    public void run() {
						        
						    	//
						    	// Finish the updating process
						    	//
						    	if(pDialog.isShowing()) pDialog.dismiss();
						    	
						    	Toast toast = Toast.makeText(getApplicationContext(), "Group '"
										+ groupName.getText().toString() + "' has been deleted." , Toast.LENGTH_LONG);
								toast.show();
								
								finish();
						    }
						}, 2000);
		            }

			     })
			     .setNegativeButton("Cancel", null)
			     .show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_or_edit_group, menu);
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
}
