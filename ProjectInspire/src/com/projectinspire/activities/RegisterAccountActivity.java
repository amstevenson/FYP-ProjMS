package com.projectinspire.activities;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.projectinspire.R;
import com.projectinspire.utilities.RegularExpressions;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * @author Adam Stevenson
 */
public class RegisterAccountActivity extends Activity {

	//
	// Global variables for toast
	//
	private Context context;
	private String  text;
	private int     duration;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_account);

		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Register Account");
		 
		Button submitRegistration = (Button) findViewById(R.id.btnRegisterSubmit);
		
		//
		// define toast variables
		//
		context  = getApplicationContext();
		text     = "defined, but not used";
		duration = Toast.LENGTH_LONG;
		
		//
		// Define views associated with the process of creating an account
		//
		final EditText registerForename       = (EditText) findViewById(R.id.editRegisterForename);
		final EditText registerSurname        = (EditText) findViewById(R.id.editRegisterSurname);
		final EditText registerPassword       = (EditText) findViewById(R.id.editRegisterPassword);
		final EditText registerRepeatPassword = (EditText) findViewById(R.id.editRegisterRepeatPassword);
		final EditText registerEmail          = (EditText) findViewById(R.id.editRegisterEmail);
		final EditText registerRepeatEmail    = (EditText) findViewById(R.id.editRegisterRepeatEmail);
 		
		//
		// When clicked, try to create an account
		//
		submitRegistration.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//********************************************************************//
				//							Variables   			      			  //
				//********************************************************************//
				//
				// Views
				//
				Editable forename = registerForename.getText();
				Editable surname  = registerSurname.getText();
				Editable password = registerPassword.getText();
				Editable repeatPassword = registerRepeatPassword.getText();
				Editable email = registerEmail.getText();
				Editable repeatEmail = registerRepeatEmail.getText();
				
				//
				// Booleans
				//
				boolean passwordValidation = false;
				boolean emailValidation    = false;
				
				//********************************************************************//
				//							Create account   			      		  //
				//********************************************************************//
				//
				// Check parameters before creating account
				//
				RegularExpressions regularExpressions = new RegularExpressions();
				
				//
				// Check if password contains at least one number and letter
				// 
				if(regularExpressions.passwordNumsAndLetters(password.toString()) && 
						regularExpressions.passwordNumsAndLetters(repeatPassword.toString()))
					
					//
					// Check if both the passwords are identical
					//
					if(password.toString().equals(repeatPassword.toString()))
						//
						// Check length
						//
						if(password.length() >= 5) 
							passwordValidation = true;
						else
						{
							passwordValidation = false;
							
							text = "The chosen password needs to be atleast five characters long, please revise and try again";
							
							// Inform the user that they have to revise their password
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
					else
					{
						passwordValidation = false;
						
						text = "Oops! The passwords do not match, please make sure that they are identical and try again";
						
						// Inform the user that they have to revise their password
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
				else 
				{
					passwordValidation = false;
					
					text = "Both password fields need to have atleast one number and one letter, please revise and try again";
					
					// Inform the user that they have to revise their password
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				
				//
				// Email check
				//
				if(passwordValidation) // step 2
				{
					if(regularExpressions.validateEmail(email.toString()) &&
							regularExpressions.validateEmail(repeatEmail.toString()))
						
						if(email.toString().equals(repeatEmail.toString()))
							emailValidation = true;
						else
						{
							emailValidation = false;
							
							text = "Both of the email addresses need to be identical, please revise and try again";
							
							// Inform the user that they have to revise their email
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
					else
					{
						emailValidation = false;
						
						text = "Oops! Both of the emails need to be in the correct format (perhaps there was no @ symbol?)";
						
						// Inform the user that they have to revise their email
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
					
					//
					// Create the user - if both validation steps are true
					//
					if(emailValidation) // step 3
					{
						//
						// Insert new row into the relevant Parse database class
						// It is important to get the .toString for each Editable.
						//
						// In addition to this, we need to use the email for the user name and email address
						// this is because the user name is the login identifier, and the email address
						// is used by parse to send email's to users - the problem of using an email as a logging in
						// variable was identified a year ago by Parse users, and still has not been addressed. 
						//
					    ParseUser registerUser = new ParseUser();
					    registerUser.setUsername(email.toString());
					    registerUser.put("forename", forename.toString());
					    registerUser.put("surname", surname.toString());  
					    registerUser.setPassword(password.toString());
					    registerUser.setEmail(email.toString());
					    
					    // Check if it was successful or not
					    registerUser.signUpInBackground(new SignUpCallback() {
							public void done(ParseException e) {
								if (e == null) {
									// Show a simple Toast message upon successful registration
									Toast.makeText(context,
											"Successfully Signed up, please log in.",
											duration)
											.show();
									
									//
									// Change the screen to the main activity
									//
									Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
									startActivity(mainIntent);
									
								} else {
									Toast.makeText(context,
											"Email Address is already in use, please revise and try again.", duration)
											.show();
								}
							}
						});
					}
				} 
			} 
			
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_account, menu);
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
