package com.easymanage.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easymanage.utilities.NetworkStateOperations;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.easymanage.R;

/**
 * The main class for the application. Allows the user to progress to either
 * registering an account, or logging in to their dashboard. 
 * 
 * @author Adam Stevenson
 */
public class MainActivity extends Activity {

	private EditText userEmail;
	private EditText userPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//
		// Check to see if the information of a user has been cached. That is to say
		// that a person may have already logged in once, and it would be annoying to 
		// have to resubmit information each time. The logout option from the dashboard
		// can be used to delete the cached information belonging to the user.
		//
		ParseUser currentUser = ParseUser.getCurrentUser();
		if(currentUser != null)
		{
			//
			// If the user is currently cached.
			//
			Intent intent = new Intent(
					getApplicationContext(),
					UserDashboardActivity.class);
			
			// Intent extras
			intent.putExtra("userId", currentUser.getObjectId());
			intent.putExtra("justRegistered", false);
			
			// Proceed to the dashboard activity
			startActivity(intent);
		}
		
		//
		// View variables
		//
		TextView registerAccount    = (TextView)  findViewById(R.id.txtMainRegister);
		TextView userLogin          = (TextView)  findViewById(R.id.btnMainLogin);
		TextView userForgotPassword = (TextView)  findViewById(R.id.txtMainForgotPassword);
		ImageView imageBook         = (ImageView) findViewById(R.id.iconMainImage);
		
		userEmail          = (EditText)  findViewById(R.id.editMainEmail);
		userPassword       = (EditText)  findViewById(R.id.editMainPassword);
		
		//
		// Provide the implementation for each on click listener
		// In most cases this will be used to simply go to another form
		// Although some will have networking functionalities attached.
		//
		registerAccount.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				Intent sendToRegister = new Intent(getApplicationContext(), RegisterAccountActivity.class );
				startActivity(sendToRegister);
				
			}
		});
		
		userLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(NetworkStateOperations.testNetworkConnection(getApplicationContext()))
				{
					final String email    = userEmail.getText().toString();
					final String password = userPassword.getText().toString();
										
				    // Progress Dialog
				    final ProgressDialog pDialog;
		            pDialog = new ProgressDialog(MainActivity.this);
		            pDialog.setMessage("Currently logging you in, please wait...");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
				    
					//
					// Log in using Parse LogInInbackground
					//
					// Send data to Parse.com for verification
					ParseUser.logInInBackground(email, password,
							new LogInCallback() {
								public void done(ParseUser user, ParseException e) {
									if (user != null) { // if we have a user
										
										//
										// If the user is stored in the database
										//
										Intent intent = new Intent(
												getApplicationContext(),
												UserDashboardActivity.class);
										
										// Intent extras
										intent.putExtra("userId", user.getObjectId());
										intent.putExtra("justRegistered", false);
										
										// Close progress dialog
										if(pDialog.isShowing()) pDialog.dismiss();     
										
										// Proceed to dashboard
										startActivity(intent);
										
									} else {
										
										// 
										// Login has failed, ask user to register
										//
										if(pDialog.isShowing()) pDialog.dismiss();
										
										Toast.makeText(
												getApplicationContext(),
												"Either the email address or password is incorrect, please try again.",
												Toast.LENGTH_LONG).show();
									}
								}
					}); // end of LogInBackground	
				}// end of network test
			}
		});
		
		userForgotPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent sendToForgotPassword = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
				startActivity(sendToForgotPassword);
				
			}
		});
		
		
		//*******************************************************************************************//
		//									Create and assign the images							 //
		//*******************************************************************************************//
        //
		// If we do not have a image stored for the user, create the default user image drawable
		//
        Drawable iconBook = getResources().getDrawable(R.drawable.icon_book_reading_white_48dp);

        //
    	// Assign the drawable to the ImageView
        //
    	imageBook.setImageDrawable(iconBook);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    //
	    // Set the text to nothing, so a back press on the keypad doesn't reveal any 
	    // Sensitive information.
	    //
	    userEmail.setText("");
	    userPassword.setText("");
	}

}
