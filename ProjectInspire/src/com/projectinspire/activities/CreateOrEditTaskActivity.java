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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateOrEditTaskActivity extends Activity {

	private String  projectId = "empty";
	private String  taskId    = "empty";
	private boolean editing   = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_task);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Tasks"); // will change dependent on edit or create
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user email
    	//
    	Intent intent			  = getIntent();
    	
    	// determine if we are editing/deleting or creating
    	editing       = intent.getBooleanExtra("editing", editing);
		
    	Log.d("Editing - Create Task", String.valueOf(editing)); // debug
    	
		//*******************************************************************************************//
		//											Views											 //
		//*******************************************************************************************//
		final TextView taskLabel  = (TextView) findViewById(R.id.txtCreateTaskUserHelp);
    	final EditText taskStartDate = (EditText) findViewById(R.id.editCreateTaskStartDate);
		final EditText taskEndDate = (EditText) findViewById(R.id.editCreateTaskEndDate);
		final EditText taskName = (EditText) findViewById(R.id.editCreateTaskName);
		final EditText taskAssignedTo = (EditText) findViewById(R.id.editCreateTaskAssigned);
		final EditText taskCategory = (EditText) findViewById(R.id.editCreateTaskCategory);
		final Spinner taskPriority = (Spinner) findViewById(R.id.editCreateTaskPriority);
		final EditText taskDescription = (EditText) findViewById(R.id.editCreateTaskDescription);
		final Space    taskSpace   = (Space) findViewById(R.id.spaceCreateTaskButton);
		
		//
		// Button to create or edit a row in the database for a task
		//
		final Button createTask = (Button) findViewById(R.id.btnCreateTask);
		final Button deleteTask = (Button) findViewById(R.id.btnDeleteTask);
		
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
			taskLabel.setText("To edit this task, please make all changes and press the 'edit'" +
					" button below. \n To Delete, press the 'delete' button. ");
		
			// retrieve the taskId from the intent
	    	taskId        = intent.getStringExtra("taskId");
	    	Log.d("TID - Create Task", taskId); // debug
	    	
			//
			// Get the relative information for the project, and set the information
			//
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
			query.whereEqualTo("objectId", taskId);
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> task, ParseException e) {
					
					if (e == null) {      	
				    	
						//
						// Check that there is a project (or more than one, I guess), and
						// then change the views so that the old data is shown
						//
						if(task.size() > 0)
						{
							taskName.setText(task.get(0).getString("taskName"));
							taskStartDate.setText(task.get(0).getString("taskStartDate"));
							taskEndDate.setText(task.get(0).getString("taskEndDate"));
							taskAssignedTo.setText(task.get(0).getString("taskAssignedTo"));
							
							String priority = task.get(0).getString("taskPriority");
							
							if     (priority.equals("Critical")) taskPriority.setSelection(0);
							else if(priority.equals("Medium"))   taskPriority.setSelection(1);
							else if(priority.equals("Low"))      taskPriority.setSelection(2);
							else if(priority.equals("Other"))	 taskPriority.setSelection(3);
							
							taskCategory.setText(task.get(0).getString("taskCategory"));
							taskDescription.setText(task.get(0).getString("taskDescription"));
						}
				    	        		             	
			    	} else Log.d("score", "Error: " + e.getMessage());
			    }
			});
			
			//
			// Change the text for the button (boolean determines on click function, but text needs to be changed
			// either way
			//
			createTask.setText("Update Project");
			
		} 
		//*******************************************************************************************//
		//									If NOT Editing/deleting									 //
		//*******************************************************************************************//
		else if(!editing)
		{
	    	projectId	  = intent.getStringExtra("projectId");
	    	Log.d("PID - Create Task", projectId); // debug
	    	
			//
			// Position the space view for layout purposes
			//
			RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
			        ViewGroup.LayoutParams.WRAP_CONTENT);

			p.addRule(RelativeLayout.BELOW, R.id.btnCreateTask);

			taskSpace.setLayoutParams(p);
			
			//
			// Make delete button invisible
			//
			deleteTask.setVisibility(View.GONE);
		}
		
		//*******************************************************************************************//
		//									On Click Listeners										 //
		//*******************************************************************************************//
		//
		// Utilities for types of pickers; date and time
		//
		final UtilitiesPickers pickers = new UtilitiesPickers();
						
		//
		// Set the start date of the task
		//
		taskStartDate.setInputType(InputType.TYPE_NULL);

		// on click
		taskStartDate.setOnClickListener(new OnClickListener() {
							
			@Override
			public void onClick(View v) {
				//
				// Set date
				//
				pickers.showDatePickerDialog(CreateOrEditTaskActivity.this, taskStartDate);
			}
		});	
		
		//
		// set the end date of the task
		//
		taskEndDate.setInputType(InputType.TYPE_NULL);
		
		// on click
		taskEndDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				// Set date
				//
				pickers.showDatePickerDialog(CreateOrEditTaskActivity.this, taskEndDate);
				
			}
		});
		
		//
		// When the 'create' new task button is pressed
		//
		createTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!editing)
				{
					//
					// Check validation on fields; make sure none are null
					// (and possibly others in the future if required changes are needed)
					//
					if(taskName.getText().toString().equals("") || taskAssignedTo.getText().toString().equals("")
							|| taskCategory.getText().toString().equals("") || taskStartDate.getText().toString().equals("")
							|| taskEndDate.getText().toString().equals("") || taskPriority.getSelectedItem().toString().equals("")
							|| taskDescription.getText().toString().equals(""))
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
						ParseObject newTask = new ParseObject("Task");
						newTask.put("projectId", projectId);
						newTask.put("taskName", taskName.getText().toString());
						newTask.put("taskAssignedTo", taskAssignedTo.getText().toString());
						newTask.put("taskCategory", taskCategory.getText().toString());
						newTask.put("taskStartDate", taskStartDate.getText().toString());
						newTask.put("taskEndDate", taskEndDate.getText().toString());
						newTask.put("taskPriority", taskPriority.getSelectedItem().toString());
						newTask.put("taskStatus", "Active"); // others are: on hold, dropped, completed
						newTask.put("taskDescription", taskDescription.getText().toString()); 
						newTask.saveInBackground();
						
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(CreateOrEditTaskActivity.this);
			            pDialog.setMessage("Creating the new task: " + taskName.getText().toString() + ", please be patient.");
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
						    	
								Toast toast = Toast.makeText(getApplicationContext(), "Task: '"
										+ taskName.getText().toString() + "' has been created." , Toast.LENGTH_LONG);
								toast.show();
								
								finish();
						    }
						}, 2000);
					}
				}
				if(editing)
				{
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
					 
					// Retrieve the object by id
					query.getInBackground(taskId, new GetCallback<ParseObject>() {
					  public void done(ParseObject task, ParseException e) {
					    if (e == null) {
					      
					    	//
					    	// Update data
					    	//
					    	task.put("taskName", taskName.getText().toString());
					    	task.put("taskStartDate", taskStartDate.getText().toString());
					    	task.put("taskEndDate", taskEndDate.getText().toString());
					    	task.put("taskPriority", taskPriority.getSelectedItem().toString());
							task.put("taskCategory", taskCategory.getText().toString());
					    	task.put("taskAssignedTo", taskAssignedTo.getText().toString());
							task.put("taskDescription", taskDescription.getText().toString()); // others are: on hold, dropped, completed
					    	task.saveInBackground();
					    	
							// Progress Dialog
						    final ProgressDialog pDialog;
				            pDialog = new ProgressDialog(CreateOrEditTaskActivity.this);
				            pDialog.setMessage("Updating the details of the task: " + taskName.getText().toString() + "...");
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
							    	
							    	Toast toast = Toast.makeText(getApplicationContext(), "Task '"
											+ taskName.getText().toString() + "' has been updated." , Toast.LENGTH_LONG);
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
		deleteTask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// Create dialog button for confirmation for deleting the task
				//
			    new AlertDialog.Builder(v.getContext())
			     .setIcon(android.R.drawable.ic_dialog_alert)
			     .setTitle("Delete Task")
			     .setMessage("Are you sure you want to delete this task? This change will be permanent.")
			     .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

						//
						// Delete task
						//
						ParseObject.createWithoutData("Task", taskId).deleteInBackground();
								
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(CreateOrEditTaskActivity.this);
			            pDialog.setMessage("Deleting the task: " + taskName.getText().toString() + "...");
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
						    	
						    	Toast toast = Toast.makeText(getApplicationContext(), "Task '"
										+ taskName.getText().toString() + "' has been deleted." , Toast.LENGTH_LONG);
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
		getMenuInflater().inflate(R.menu.create_or_edit_task, menu);
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
