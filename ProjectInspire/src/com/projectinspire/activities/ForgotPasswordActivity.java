package com.projectinspire.activities;

import com.projectinspire.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ForgotPasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Forgot Password");
		
		//
		// Set the view variables
		//
		Button submitCode = (Button) findViewById(R.id.btnForgotPasswordSubmit);
		
		//
		// Set the on click listeners
		//
		submitCode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//
				// Send to activity
				//
				Intent resetPasswordIntent = new Intent(getApplicationContext(), ForgotPasswordResetActivity.class);
				startActivity(resetPasswordIntent);
				
				
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
