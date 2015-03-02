package com.projectinspire.activities;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.projectinspire.R;

import android.app.ActionBar;
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

public class CreateOrEditContactActivity extends Activity {

	private String  userId    = "empty";
	private String  contactId = "empty";
	private boolean editing   = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_contact);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Contacts"); // will change dependent on edit or create
	
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
		    		
		Log.d("Create Contact - User Id", userId); // for debugging
		Log.d("Contact - Editing ", String.valueOf(editing));
	
    	//*******************************************************************************************//
    	//											Views											 //
    	//*******************************************************************************************//
    	final TextView createContactLabel = (TextView) findViewById(R.id.txtCreateContactsUserHelp);
		final EditText createContactName  = (EditText) findViewById(R.id.editCreateContactName);
    	final EditText createContactEmail = (EditText) findViewById(R.id.editCreateContactEmail);
    	final EditText createContactNotes = (EditText) findViewById(R.id.editCreateContactNotes);
    	
    	final Button   btnCreateContact   = (Button)   findViewById(R.id.btnCreateContact);
    	final Button   btnDeleteContact    = (Button) findViewById(R.id.btnDeleteContact);
    	
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
    		createContactLabel.setText("To edit this project, please make all changes and press the 'edit'" +
    				" button below. \n To Delete, press the 'delete' button. ");
    			
    		// retrieve the projectId from the intent
    		contactId = intent.getStringExtra("contactId");
    				
    		Log.d("Create contact - contact ID", contactId); // for debugging
    				
    		//
    		// Get the relative information for the project, and set the information
    		//
    		ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");
    			query.whereEqualTo("objectId", contactId);
    			query.findInBackground(new FindCallback<ParseObject>() {
    				public void done(List<ParseObject> project, ParseException e) {
    						
    					if (e == null) {      	
    					    	
	    					//
	    					// Check that there is a project (or more than one, I guess), and
	    					// then change the views so that the old data is shown
	    					//
	    					if(project.size() > 0)
	    					{
	    						createContactName.setText(project.get(0).getString("contactName"));
	    						createContactEmail.setText(project.get(0).getString("contactEmail"));
	    						createContactNotes.setText(project.get(0).getString("contactNotes"));
	    					}   
	    					
    					} else Log.d("Contacts", "Edit contact error: " + e.getMessage());
    			}
    		});
    			
    		//
    		// Change the text for the button (boolean determines on click function, but text needs to be changed
    		// either way
    		//
    		btnCreateContact.setText("Update Contact");
    			
    	}
    	//*******************************************************************************************//
    	//									If NOT Editing/deleting									 //
    	//*******************************************************************************************//
    	else if(!editing)
    	{
    		//
    		// Make delete button invisible
    		//
    		btnDeleteContact.setVisibility(View.GONE);			
    	}
    	
    	//*******************************************************************************************//
    	//									Adding a contact     									 //
    	//*******************************************************************************************//
    	btnCreateContact.setOnClickListener(new OnClickListener() {
			
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
					if(createContactEmail.getText().toString().equals(""))
					{
						Toast toast = Toast.makeText(getApplicationContext(), 
								"Please enter a valid email address", Toast.LENGTH_LONG);
						toast.show();
					}
					else
					{
						//
						// Get the relative information for the project, and set the information
						//
						@SuppressWarnings("deprecation")
						ParseQuery<ParseUser> query = ParseUser.getQuery();
						query.whereEqualTo("email", createContactEmail.getText().toString()); // get email and find userId
						query.findInBackground(new FindCallback<ParseUser>() {
							public void done(List<ParseUser> userContact, ParseException e) {
								
								if (e == null) {      	
							    	
									//
									// Check that there is a project (or more than one, I guess), and
									// then change the views so that the old data is shown
									//
									if(userContact.size() > 0)
									{
										Log.d("Create Contact - Email", createContactEmail.getText().toString());
										Log.d("create contact - User", userContact.get(0).getObjectId());
										
										
										if(userId.equals(userContact.get(0).getObjectId()))
										{
											Toast toast = Toast.makeText(getApplicationContext(), 
													"You cannot add yourself as a contact, please revise and try again.", Toast.LENGTH_LONG);
											toast.show();
										}
										else
										{
											//
											// Create a new Parse Object and add it to database
											//
											ParseObject newContact = new ParseObject("Contact");
											newContact.put("userId", userId);
											newContact.put("contactUserId", userContact.get(0).getObjectId());
											newContact.put("contactEmail", createContactEmail.getText().toString());
											
											//
											// Enter user name, or "N/A" if not entered.
											//
											if(createContactName.getText().toString().equals(""))
												newContact.put("contactName", "N/A");
											else
												newContact.put("contactName", createContactName.getText().toString());
											
											//
											// Enter notes, or N/A if not entered.
											//
											if(createContactName.getText().toString().equals(""))
												newContact.put("contactNotes", "None");
											else
												newContact.put("contactNotes", createContactNotes.getText().toString());
											newContact.saveInBackground();
											
											// Progress Dialog
										    final ProgressDialog pDialog;
								            pDialog = new ProgressDialog(CreateOrEditContactActivity.this);
								            pDialog.setMessage("Adding " + createContactName.getText().toString() + "to your contact list");
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
											    	
													Toast toast = Toast.makeText(getApplicationContext(), "Contact: '"
															+ createContactName.getText().toString() + "' has been added." , Toast.LENGTH_LONG);
													toast.show();
													
													finish();
											    }
											}, 2000);
										}
									}
									else
									{
										//
										// Error message informing that email is not valid
										// Might want to change this so it still makes the contact...for privacy purposes? Not too sure.
										//
										Toast toast = Toast.makeText(getApplicationContext(), 
												"Email address is not in use, please try again.", Toast.LENGTH_LONG);
										toast.show();
									}
							    	        		             	
						    	} else Log.d("score", "Error: " + e.getMessage());
						    }
						});
					}
				}
				else // we are editing 
				{
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Contact");
					 
					// Retrieve the object by id
					query.getInBackground(contactId, new GetCallback<ParseObject>() {
						public void done(ParseObject contact, ParseException e) {
						    if (e == null) {
						      
						    	//
						    	// Update data
						    	//
						    	contact.put("contactName", createContactName.getText().toString());
						    	contact.put("contactEmail", createContactEmail.getText().toString());
						    	contact.put("contactNotes", createContactNotes.getText().toString());
						    	contact.saveInBackground();
						    	
								// Progress Dialog
							    final ProgressDialog pDialog;
					            pDialog = new ProgressDialog(CreateOrEditContactActivity.this);
					            pDialog.setMessage("Editing the details of " + createContactName.getText().toString());
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
								    	
								    	Toast toast = Toast.makeText(getApplicationContext(), "Contact '"
												+ createContactName.getText().toString() + "' has been updated." , Toast.LENGTH_LONG);
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
		btnDeleteContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// Create dialog button for confirmation for deleting the project
				//
			    new AlertDialog.Builder(v.getContext())
			     .setIcon(android.R.drawable.ic_dialog_alert)
			     .setTitle("Delete Contact")
			     .setMessage("Are you sure you want to remove this contact? This change will be permanent.")
			     .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {

						//
						// Delete project
						//
						ParseObject.createWithoutData("Contact", contactId).deleteInBackground();
							
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(CreateOrEditContactActivity.this);
			            pDialog.setMessage("Removing " + createContactName.getText().toString() + "from your contact list");
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
						    	
						    	Toast toast = Toast.makeText(getApplicationContext(), "'"
										+ createContactName.getText().toString() + "' has been removed from the contact list." , Toast.LENGTH_LONG);
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
		getMenuInflater().inflate(R.menu.create_or_edit_contact, menu);
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
