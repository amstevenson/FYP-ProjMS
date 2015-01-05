package com.projectinspire.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.projectinspire.R;

/*
 * The main class for the application. Allows the user to progress to either
 * registering an account, or logging in to their dashboard. 
 * 
 * @author Adam Stevenson
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Get the identifiers for the objects that will be used as links to other activities
		TextView registerIntent = (TextView)this.findViewById(R.id.mainTxtRegister);
		TextView loginIntent    = (TextView)this.findViewById(R.id.mainBtnLogin);
		
		// Provide the implementation for each on click listener
		// In most cases this will be used to simply go to another form
		// Although some will have networking functionalities attached.
		registerIntent.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				Intent sendToRegister = new Intent(getApplicationContext(), RegisterAccountActivity.class );
				startActivity(sendToRegister);
				
			}
		});
		
		loginIntent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent sendToDashboard = new Intent(getApplicationContext(), UserDashboard.class);
				startActivity(sendToDashboard);
				
			}
		});
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

}
