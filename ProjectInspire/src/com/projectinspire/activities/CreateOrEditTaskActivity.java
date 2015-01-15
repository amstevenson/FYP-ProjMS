package com.projectinspire.activities;

import com.projectinspire.R;
import com.projectinspire.utilities.UtilitiesPickers;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CreateOrEditTaskActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_task);
		
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
		final EditText taskStartDate = (EditText) findViewById(R.id.editCreateTaskStartDate);
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
		final EditText taskEndDate = (EditText) findViewById(R.id.editCreateTaskEndDate);
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
