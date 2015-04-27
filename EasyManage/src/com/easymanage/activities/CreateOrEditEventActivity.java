package com.easymanage.activities;

import java.util.List;

import com.easymanage.utilities.UtilitiesPickers;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.easymanage.R;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * An activity that allows the user to create or edit an instance 
 * of an event object.
 * 
 * @author Adam Stevenson
 *
 */
public class CreateOrEditEventActivity extends Activity {
	
	private String  userId    = "empty";
	private String  eventId = "empty";
	private boolean editing   = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_event);
	
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Events"); // will change dependent on edit or create
		
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
    		
    	Log.d("Create Event - User Id", userId); // for debugging
    	Log.d("Editing ", String.valueOf(editing));
    	
    	//*******************************************************************************************//
    	//											Views											 //
    	//*******************************************************************************************//
    	final TextView eventLabel       = (TextView) findViewById(R.id.txtCreateEventUserHelp);
    	final EditText eventName        = (EditText) findViewById(R.id.editCreateEventName);
    	final EditText eventLocation    = (EditText) findViewById(R.id.editCreateEventLocation);
    	final EditText eventPostcode    = (EditText) findViewById(R.id.editCreateEventPostcode);
    	final EditText eventDescription = (EditText) findViewById(R.id.editCreateEventDescription);
    	final EditText eventDate        = (EditText) findViewById(R.id.editCreateEventDate);
    	final EditText eventStartTime   = (EditText) findViewById(R.id.editCreateEventStartTime);
    	final EditText eventEndTime     = (EditText) findViewById(R.id.editCreateEventEndTime);
    	final Spinner  eventVisibility  = (Spinner)  findViewById(R.id.editCreateEventVisibility);
    	
    	final Button   eventCreateEvent = (Button)   findViewById(R.id.btnCreateEvent);
    	final Button   eventDeleteEvent = (Button)   findViewById(R.id.btnDeleteEvent);
    	
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
    		eventLabel.setText("To edit this project, please make all changes and press the 'edit'" +
    				" button below. \n To Delete, press the 'delete' button. ");
    			
    		// retrieve the projectId from the intent
    		eventId = intent.getStringExtra("eventId");
    				
    		Log.d("Create event - event ID", eventId); // for debugging
    				
    		//
    		// Get the relative information for the project, and set the information
    		//
    		ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
    			query.whereEqualTo("objectId", eventId);
    			query.findInBackground(new FindCallback<ParseObject>() {
    				public void done(List<ParseObject> event, ParseException e) {
    						
    					if (e == null) {      	
    					    	
	    					//
	    					// Check that there is a project (or more than one, I guess), and
	    					// then change the views so that the old data is shown
	    					//
	    					if(event.size() > 0)
	    					{
	    						eventName.setText(event.get(0).getString("eventName"));
	    						eventLocation.setText(event.get(0).getString("eventLocation"));
	    						eventPostcode.setText(event.get(0).getString("eventPostcode"));
	    						eventDescription.setText(event.get(0).getString("eventDescription"));
	    						eventDate.setText(event.get(0).getString("eventDate"));
	    						eventStartTime.setText(event.get(0).getString("eventStartTime"));	
	    						eventEndTime.setText(event.get(0).getString("eventEndTime"));
	    						
	    						String visibility = event.get(0).getString("eventVisibility");
	    								
	    						if     (visibility.equals("Everyone"))     eventVisibility.setSelection(0);
	    						else if(visibility.equals("Select Group")) eventVisibility.setSelection(1);
	    						else if(visibility.equals("Myself"))       eventVisibility.setSelection(2);
	    								
	    					}   
	    					
    					} else Log.d("score", "Event error: " + e.getMessage());
    			}
    		});
    			
    		//
    		// Change the text for the button (boolean determines on click function, but text needs to be changed
    		// either way
    		//
    		eventCreateEvent.setText("UPDATE");
    			
    	}
    	//*******************************************************************************************//
    	//									If NOT Editing/deleting									 //
    	//*******************************************************************************************//
    	else if(!editing)
    	{
    		//
    		// Make delete button invisible
    		//
    		eventDeleteEvent.setVisibility(View.GONE);			
    	}
    	
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
		eventDate.setInputType(InputType.TYPE_NULL);

		eventDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				// Set date
				//
				pickers.showDatePickerDialog(CreateOrEditEventActivity.this, eventDate);
			}
		});
		
		//
		// Set the start time
		//
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
		
		//
		// Create event
		//
		eventCreateEvent.setOnClickListener(new OnClickListener() {
			
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
					if(eventName.getText().toString().equals("") || eventLocation.getText().toString().equals("")
							|| eventDescription.getText().toString().equals("") || eventDate.getText().toString().equals("")
							|| eventStartTime.getText().toString().equals("")
							|| eventEndTime.getText().toString().equals("") || eventVisibility.getSelectedItem().toString().equals(""))
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
						ParseObject newEvent = new ParseObject("Event");
						newEvent.put("eventCreatedBy", userId);
						newEvent.put("eventName", eventName.getText().toString());
						newEvent.put("eventDescription", eventDescription.getText().toString());
						newEvent.put("eventDate", eventDate.getText().toString());
						newEvent.put("eventStartTime", eventStartTime.getText().toString());
						newEvent.put("eventVisibility", eventVisibility.getSelectedItem().toString());
						newEvent.put("eventEndTime", eventEndTime.getText().toString());
						newEvent.put("eventLocation", eventLocation.getText().toString()); // others are: on hold, dropped, completed
						newEvent.put("eventPostcode", eventPostcode.getText().toString());
						newEvent.saveInBackground();
						
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(CreateOrEditEventActivity.this);
			            pDialog.setMessage("Creating the event: " + eventName.getText().toString());
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
						    	
								Toast toast = Toast.makeText(getApplicationContext(), "Event: '"
										+ eventName.getText().toString() + "' has been created." , Toast.LENGTH_LONG);
								toast.show();
								
								finish();
						    }
						}, 2000);
					}
				}
				if(editing)
				{
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
						 
					// Retrieve the object by id
					query.getInBackground(eventId, new GetCallback<ParseObject>() {
						public void done(ParseObject event, ParseException e) {
						    if (e == null) {
						      
						    	//
						    	// Update data
						    	//
						    	event.put("eventName", eventName.getText().toString());
						    	event.put("eventLocation", eventLocation.getText().toString());
						    	event.put("eventPostcode", eventPostcode.getText().toString());
						    	event.put("eventDescription", eventDescription.getText().toString());
						    	event.put("eventDate", eventDate.getText().toString());
						    	event.put("eventVisibility", eventVisibility.getSelectedItem().toString());
						    	event.put("eventStartTime", eventStartTime.getText().toString());
						    	event.put("eventEndTime", eventEndTime.getText().toString());
						    	event.saveInBackground();
						    	
								// Progress Dialog
							    final ProgressDialog pDialog;
					            pDialog = new ProgressDialog(CreateOrEditEventActivity.this);
					            pDialog.setMessage("Updating the details for the event: " + eventName.getText().toString());
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
								    	
								    	Toast toast = Toast.makeText(getApplicationContext(), "Event '"
												+ eventName.getText().toString() + "' has been updated." , Toast.LENGTH_LONG);
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
		eventDeleteEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// Create dialog button for confirmation for deleting the project
				//
			    new AlertDialog.Builder(v.getContext())
			     .setIcon(android.R.drawable.ic_dialog_alert)
			     .setTitle("Delete Event")
			     .setMessage("Are you sure you want to delete this event? This change will be permanent.")
			     .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

						//
						// Delete project
						//
						ParseObject.createWithoutData("Event", eventId).deleteInBackground();
							
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(CreateOrEditEventActivity.this);
			            pDialog.setMessage("Deleting the event: " + eventName.getText().toString());
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
						    	
						    	Toast toast = Toast.makeText(getApplicationContext(), "Event '"
										+ eventName.getText().toString() + "' has been deleted." , Toast.LENGTH_LONG);
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
		if(id == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
