package com.easymanage.activities;

import com.easymanage.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * This activity requires that a code is submitted before the user
 * is allowed to change his/her password.
 * 
 * @author Adam Stevenson
 *
 */
public class ForgotPasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Reset Password");
		
		//
		// Set the view variables
		//
		Button submitButton = (Button) findViewById(R.id.btnForgotPasswordSubmit);
		final EditText submitEmail = (EditText) findViewById(R.id.editForgotPasswEmail);
		
		//
		// Set the on click listeners
		//
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(submitEmail.getText().toString() != "")
				{
					//
					// Send an email to the user with instructions of how to reset their password
					//
					ParseUser.requestPasswordResetInBackground(submitEmail.getText().toString(),
                            new RequestPasswordResetCallback() {
						public void done(ParseException e) {
								if (e == null) {
									
									// Close and inform user with a toast
									Toast.makeText(getApplicationContext(), "An email has been sent to the submitted acount " +
											" with details of how to reset your password." , Toast.LENGTH_LONG).show();
									
									finish();
									
								} else {
									
									Log.d("Parse Exception", "Parse exception: " + e.getLocalizedMessage());
									
									// Inform user with a toast
									Toast.makeText(getApplicationContext(), "Error, no account is currently attributed to the email" +
											" address provided.", Toast.LENGTH_LONG).show();
								}
						}
					});
				}
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
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
