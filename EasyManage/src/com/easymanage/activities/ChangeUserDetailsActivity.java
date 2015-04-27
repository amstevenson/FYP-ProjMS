package com.easymanage.activities;

import com.easymanage.R;
import com.parse.ParseUser;

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

/**
 * 
 * An activity that changes the information of the user. 
 * 
 * @author Adam Stevenson
 *
 */
public class ChangeUserDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_user_details);
		
		setTitle("Change Account Details"); 
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
		//
		// Retrieve the user Id
		//
		Intent intent = getIntent();
		    	
		// retrieve the boolean that determines if we are creating or editing	
		final String userEmail = intent.getStringExtra("userEmail");
		    		
		Log.d("Change Account Details - email", userEmail);
		
		//*******************************************************************************************//
    	//											Views											 //
    	//*******************************************************************************************//
		final EditText changeDetailsForename  = (EditText) findViewById(R.id.editRegisterChangeForename);
    	final EditText changeDetailsSurname   = (EditText) findViewById(R.id.editRegisterChangeSurname);
    	final Button   changeDetailsButton    = (Button) findViewById(R.id.btnChangeDetails);
    	
    	changeDetailsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			    final ProgressDialog pDialog;
	            pDialog = new ProgressDialog(ChangeUserDetailsActivity.this);
	            pDialog.setMessage("Changing your account details...this will only take a moment.");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
            	
	            ParseUser currentUser = ParseUser.getCurrentUser();
	    		if (currentUser != null) 
	    		{	
	    			//
	    			// Change the account details of the user
	    			//
	    			currentUser.add("forename", changeDetailsForename.getText().toString());
	    			currentUser.add("surname", changeDetailsSurname.getText().toString());
	    			
	    			currentUser.saveInBackground();
	    			
	    		}
	    		else
	    		{
	    			Log.d("No user retrieved", "no user retrieved");
	    		}
	            
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
				    @Override
				    public void run() {
				        
				    	//
				    	// Finish the updating process
				    	//
				    	if(pDialog.isShowing()) pDialog.dismiss();
				    	
				    	Toast toast = Toast.makeText(getApplicationContext(), "Your account" +
				    			" details have been updated." , Toast.LENGTH_LONG);
						toast.show();
						
						finish();
				    }
				}, 2000);
			}
			
    	});
    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_user_details, menu);
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
