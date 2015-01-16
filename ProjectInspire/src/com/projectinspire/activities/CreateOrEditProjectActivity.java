package com.projectinspire.activities;

import com.projectinspire.R;
import com.projectinspire.utilities.UtilitiesPickers;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CreateOrEditProjectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_project);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Projects"); // depends on what button is pressed
										// Whether from a list or if the create button is pressed
		
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
		final EditText projStartDate = (EditText) findViewById(R.id.editCreateProjectStartDate);
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
		final EditText projEndDate = (EditText) findViewById(R.id.editCreateProjectEndDate);
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
