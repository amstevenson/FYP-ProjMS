package com.projectinspire.activities;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.projectinspire.R;
import com.projectinspire.utilities.UtilitiesPickers;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateOrEditProjectActivity extends Activity {

	private String  userId    = "empty";
	private String  projectId = "empty";
	private boolean editing   = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_project);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Projects"); // depends on what button is pressed
										// Whether from a list or if the create button is pressed
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user Id
    	//
    	Intent intent = getIntent();
    	
    	// retrieve the boolean that determines if we are creating or editing
    	editing       = intent.getBooleanExtra("editing", editing);
    	
    	// if we are creating, we need the user id, else we don't. 
    	if(!editing) userId		  = intent.getStringExtra("userId");
		
    	Log.d("Create Project - User Id", userId); // for debugging
		Log.d("Create Proj - Edititing", String.valueOf(editing)); // for debugging
    	
		//*******************************************************************************************//
		//											Views											 //
		//*******************************************************************************************//
		final TextView projectLabel  = (TextView) findViewById(R.id.txtCreateProjectUserHelp);
		final EditText projName      = (EditText) findViewById(R.id.editCreateProjectName);
		final EditText projDescription = (EditText) findViewById(R.id.editCreateProjectDescription);
		final Spinner  projType        = (Spinner) findViewById(R.id.editCreateProjectType);
		final EditText projMembers     = (EditText) findViewById(R.id.editCreateProjectResponsible);
		final EditText projStartDate = (EditText) findViewById(R.id.editCreateProjectStartDate);
		final EditText projEndDate = (EditText) findViewById(R.id.editCreateProjectEndDate);
		final Space    projSpace   = (Space) findViewById(R.id.spaceCreateProjectButton);
		
		//
		// Button to create or edit a row in the database for a project
		//
		final Button btnCreateProject = (Button) findViewById(R.id.btnCreateProject);
		final Button btnDeleteProject = (Button) findViewById(R.id.btnDeleteProject);
		
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
			projectLabel.setText("To edit this project, please make all changes and press the 'edit'" +
					" button below. \n To Delete, press the 'delete' button. ");
		
			// retrieve the projectId from the intent
			projectId = intent.getStringExtra("projectId");
			
			Log.d("Create Proj - Proj ID", projectId); // for debugging
			
			//
			// Get the relative information for the project, and set the information
			//
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
			query.whereEqualTo("objectId", projectId);
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> project, ParseException e) {
					
					if (e == null) {      	
				    	
						//
						// Check that there is a project (or more than one, I guess), and
						// then change the views so that the old data is shown
						//
						if(project.size() > 0)
						{
							projName.setText(project.get(0).getString("projectName"));
							projStartDate.setText(project.get(0).getString("projectStartDate"));
							projEndDate.setText(project.get(0).getString("projectEndDate"));
							projMembers.setText(project.get(0).getString("projectMembers"));
							
							String type = project.get(0).getString("projectType");
							
							if     (type.equals("Individual")) projType.setSelection(0);
							else if(type.equals("Group"))      projType.setSelection(1);
							else if(type.equals("Other"))      projType.setSelection(2);
							
							projDescription.setText(project.get(0).getString("projectDescription"));
						}
				    	        		             	
			    	} else Log.d("score", "Error: " + e.getMessage());
			    }
			});
		
			//
			// Change the text for the button (boolean determines on click function, but text needs to be changed
			// either way
			//
			btnCreateProject.setText("Update Project");
		
		}
		//*******************************************************************************************//
		//									If NOT Editing/deleting									 //
		//*******************************************************************************************//
		else if(!editing)
		{
			//
			// Position the space view for layout purposes
			//
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
			        ViewGroup.LayoutParams.WRAP_CONTENT);

			p.addRule(RelativeLayout.BELOW, R.id.btnCreateProject);

			projSpace.setLayoutParams(p);
			
			//
			// Make delete button invisible
			//
			btnDeleteProject.setVisibility(View.GONE);
			
		}
		
		//*******************************************************************************************//
		//									On Click Listeners										 //
		//*******************************************************************************************//
		//
		// Utilities for types of pickers; date and time
		//
		final UtilitiesPickers pickers = new UtilitiesPickers();
				
		//
		// Set the start date of the project
		//
		projStartDate.setInputType(InputType.TYPE_NULL);

		// on click
		projStartDate.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				//
				// Set date
				//
				pickers.showDatePickerDialog(CreateOrEditProjectActivity.this, projStartDate);
			}
		});	
		
		//
		// Set the end date of the project
		//
		projEndDate.setInputType(InputType.TYPE_NULL);
		
		// on click
		projEndDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				// Set date
				//
				pickers.showDatePickerDialog(CreateOrEditProjectActivity.this, projEndDate);
				
			}
		});
		
		// on click
		btnCreateProject.setOnClickListener(new OnClickListener() {
			
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
					if(projDescription.getText().toString().equals("") || projEndDate.getText().toString().equals("")
							|| projMembers.getText().toString().equals("") || projName.getText().toString().equals("")
							|| projStartDate.getText().toString().equals("") || projType.getSelectedItem().toString().equals(""))
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
						ParseObject newProject = new ParseObject("Project");
						newProject.put("projectCreatedBy", userId);
						newProject.put("projectName", projName.getText().toString());
						newProject.put("projectStartDate", projStartDate.getText().toString());
						newProject.put("projectEndDate", projEndDate.getText().toString());
						newProject.put("projectMembers", projMembers.getText().toString());
						newProject.put("projectType", projType.getSelectedItem().toString());
						newProject.put("projectDescription", projDescription.getText().toString());
						newProject.put("projectStatus", "Active"); // others are: on hold, dropped, completed
						newProject.saveInBackground();
						
						Toast toast = Toast.makeText(getApplicationContext(), "Project '"
								+ projName.getText().toString() + "' has been created." , Toast.LENGTH_LONG);
						toast.show();
						
						//
						// Show all projects class
						//
						finish();
					}
				}
				if(editing)
				{
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Project");
					 
					// Retrieve the object by id
					query.getInBackground(projectId, new GetCallback<ParseObject>() {
					  public void done(ParseObject project, ParseException e) {
					    if (e == null) {
					      
					    	//
					    	// Update data
					    	//
					    	project.put("projectName", projName.getText().toString());
							project.put("projectStartDate", projStartDate.getText().toString());
							project.put("projectEndDate", projEndDate.getText().toString());
							project.put("projectMembers", projMembers.getText().toString());
							project.put("projectType", projType.getSelectedItem().toString());
							project.put("projectDescription", projDescription.getText().toString());
					    	project.saveInBackground();
					    	
					    	Toast toast = Toast.makeText(getApplicationContext(), "Project '"
									+ projName.getText().toString() + "' has been updated." , Toast.LENGTH_LONG);
							toast.show();
							
							//
							// Show all projects class
							//
							finish();
					    }
					  }
					});
				}
			}
		});
		
		//
		// If the delete button is clicked
		//
		btnDeleteProject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// Create dialog button for confirmation for deleting the project
				//
			    new AlertDialog.Builder(v.getContext())
			     .setIcon(android.R.drawable.ic_dialog_alert)
			     .setTitle("Delete Project")
			     .setMessage("Are you sure you want to delete this project? This change will be permanent.")
			     .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

						//
						// Delete project
						//
						ParseObject.createWithoutData("Project", projectId).deleteInBackground();
							
				    	Toast toast = Toast.makeText(getApplicationContext(), "Project '"
								+ projName.getText().toString() + "' has been deleted." , Toast.LENGTH_LONG);
						toast.show();
							
						//
						// Return to list all projects
						//
						finish();
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
		getMenuInflater().inflate(R.menu.create_or_edit_project, menu);
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
	
}
