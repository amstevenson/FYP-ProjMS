package com.projectinspire.activities;

import com.parse.ParseObject;
import com.projectinspire.R;
import com.projectinspire.utilities.UtilitiesPickers;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
		final EditText taskStartDate = (EditText) findViewById(R.id.editCreateTaskStartDate);
		final EditText taskEndDate = (EditText) findViewById(R.id.editCreateTaskEndDate);
		final EditText taskName = (EditText) findViewById(R.id.editCreateTaskName);
		final EditText taskAssignedTo = (EditText) findViewById(R.id.editCreateTaskAssigned);
		final EditText taskCategory = (EditText) findViewById(R.id.editCreateTaskCategory);
		final Spinner taskPriority = (Spinner) findViewById(R.id.editCreateTaskPriority);
		final EditText taskDescription = (EditText) findViewById(R.id.editCreateTaskDescription);
		
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
	    	taskId        = intent.getStringExtra("taskId");
	    	Log.d("TID - Create Task", taskId); // debug
		} 
		else if(!editing)
		{
	    	projectId	  = intent.getStringExtra("projectId");
	    	Log.d("PID - Create Task", projectId); // debug
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
					Log.d("goes into on click", "goesi n here");
					
					//
					// Create a new Parse Object and add it to database
					//
					ParseObject newProject = new ParseObject("Task");
					newProject.put("projectId", projectId);
					newProject.put("taskName", taskName.getText().toString());
					newProject.put("taskAssignedTo", taskAssignedTo.getText().toString());
					newProject.put("taskCategory", taskCategory.getText().toString());
					newProject.put("taskStartDate", taskStartDate.getText().toString());
					newProject.put("taskEndDate", taskEndDate.getText().toString());
					newProject.put("taskPriority", taskPriority.getSelectedItem().toString());
					newProject.put("taskStatus", "Active"); // others are: on hold, dropped, completed
					newProject.put("taskDescription", taskDescription.getText().toString()); // others are: on hold, dropped, completed
					newProject.saveInBackground();
					
					Toast toast = Toast.makeText(getApplicationContext(), "Task: '"
							+ taskName.getText().toString() + "' has been created." , Toast.LENGTH_LONG);
					toast.show();
					
					//
					// Show all projects class
					//
					//Intent intent = new Intent(getApplicationContext(), UserListAllProjectsActivity.class);
					//intent.putExtra("projectId", projectId);
					//startActivity(intent);
					
					finish(); // go back to the previous screen
							  // which will invoke the "onResume" method.
					
				}
				
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
