package com.projectinspire.activities;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.projectinspire.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

public class CreateOrEditMessageActivity extends Activity {

	private String userId, userEmail = "empty";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_or_edit_message);
	
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Messages"); // will change dependent on edit or create
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user Id
    	//
    	Intent intent = getIntent();
    	
    	// retrieve the boolean that determines if we are creating or editing
    	userId		  = intent.getStringExtra("userId");
		userEmail     = intent.getStringExtra("userEmail");
    	
    	//*******************************************************************************************//
    	//											Views											 //
    	//*******************************************************************************************//
    	final EditText editMessageTo   = (EditText) findViewById(R.id.editCreateMessageRecipient);
    	final EditText editMessageBody = (EditText) findViewById(R.id.editCreateMessageBody);

    	final Button   btnCreateMessage = (Button)   findViewById(R.id.btnCreateMessage);
    	
    	//*******************************************************************************************//
    	//									Sending a new message     								 //
    	//*******************************************************************************************//
    	btnCreateMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// if creating/ not editing
				// 
				//
				// make sure that no fields are empty
				//
				if(editMessageTo.getText().toString().equals("") || 
						editMessageBody.getText().toString().equals(""))
				{
					Toast toast = Toast.makeText(getApplicationContext(), 
							"Please check that there is a valid recipient and that the message is not empty.", Toast.LENGTH_LONG);
					toast.show();
				}
				else
				{
					//
					// Get the relative information for the project, and set the information
					//
					@SuppressWarnings("deprecation")
					ParseQuery<ParseUser> query = ParseUser.getQuery();
					query.whereEqualTo("email", editMessageTo.getText().toString()); // get email and find userId
					query.findInBackground(new FindCallback<ParseUser>() {
						public void done(List<ParseUser> userContact, ParseException e) {
								
							if (e == null) {      	
							    	
								//
								// Check that there is a project (or more than one, I guess), and
								// then change the views so that the old data is shown
								//
								if(userContact.size() > 0)
								{
									Log.d("Create message - Email", editMessageTo.getText().toString());
									Log.d("create message - User", userContact.get(0).getObjectId());
										
										
									if(userId.equals(userContact.get(0).getObjectId()))
									{
										Toast toast = Toast.makeText(getApplicationContext(), 
												"You cannot send a message to yourself, please try again.", Toast.LENGTH_LONG);
										toast.show();
									}
									else
									{
										//
										// Create a new Parse Object and add it to database
										//
										ParseObject newContact = new ParseObject("Message");
										newContact.put("messageFromID", userId);
										newContact.put("messageToID",    userContact.get(0).getObjectId());
										newContact.put("messageBody",    editMessageBody.getText().toString());
										newContact.put("messageFromEmail", userEmail);	
										newContact.saveInBackground();
											
										// Progress Dialog
									    final ProgressDialog pDialog;
							            pDialog = new ProgressDialog(CreateOrEditMessageActivity.this);
							            pDialog.setMessage("Sending the message to: " + editMessageTo.getText().toString());
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
										    	
												Toast toast = Toast.makeText(getApplicationContext(), "Message to: '"
														+ editMessageTo.getText().toString() + "' has been sent successfully." , Toast.LENGTH_LONG);
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
											"Error finding user, please try again.", Toast.LENGTH_LONG);
									toast.show();
								}
							    	        		             	
						    } else Log.d("score", "Error: " + e.getMessage());
						    }
						});
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_or_edit_message, menu);
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
