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

public class CreateOrEditEventActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_event);
	
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Events"); // will change dependent on edit or create
		
		//*******************************************************************************************//
		//									On Click Listeners										 //
		//*******************************************************************************************//
		//
		// Utilities for types of pickers; date and time
		//
		final UtilitiesPickers pickers = new UtilitiesPickers();
		
		//
		// Set the date of the event
		//
		final EditText setDate = (EditText) findViewById(R.id.editCreateEventDate);
		setDate.setInputType(InputType.TYPE_NULL);

		setDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				// Set date
				//
				pickers.showDatePickerDialog(CreateOrEditEventActivity.this, setDate);
			}
		});
		
		//
		// Set the start time
		//
		final EditText eventStartTime = (EditText) findViewById(R.id.editCreateEventStartTime);
		eventStartTime.setInputType(InputType.TYPE_NULL);
		
		eventStartTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				// Set time
				//
				pickers.showTimePickerDialog(CreateOrEditEventActivity.this, eventStartTime);
				
			}
		});
		
		//
		// Set the end time
		//
		final EditText eventEndTime = (EditText) findViewById(R.id.editCreateEventEndTime);
		eventEndTime.setInputType(InputType.TYPE_NULL);
		
		eventEndTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				// Set time
				//
				pickers.showTimePickerDialog(CreateOrEditEventActivity.this, eventEndTime);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_or_edit_event, menu);
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
