package com.projectinspire.activities;

import com.projectinspire.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserListAllProjectsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list_projects);
		
		//
		// Create view variables
		//
		TextView  txtProjectsNone        = (TextView) findViewById(R.id.txtProjectsNone);
		ListView  listProjectsAll        = (ListView) findViewById(R.id.listProjectsAll);
		ImageView imageViewCreateProject = (ImageView)findViewById(R.id.imageViewProjectCreate);
		EditText  editSearchProjects     = (EditText) findViewById(R.id.editSearchProjects);
		
		//*******************************************************************************************//
		//									View On click listeners           						 //
		//*******************************************************************************************//
		//
		// If we have no items, show this to the user
		// Chances are it will be the first time they have used the service too, so I
		// May want to add more user information than what is there currently.
		//
		if(listProjectsAll.getCount() == 0) txtProjectsNone.setVisibility(View.VISIBLE);
		else 								txtProjectsNone.setVisibility(View.GONE);	
		
		txtProjectsNone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//
				// test purposes, send to the selected project activity
				//
				Intent intent = new Intent(getApplicationContext(), UserSelectedProjectActivity.class);
				startActivity(intent);
				
			}
		});
		
		//
		// If the create a new project button is pressed
		//
		imageViewCreateProject.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent createProject = new Intent(getApplicationContext(), CreateOrEditProjectActivity.class);
				startActivity(createProject);
				
			}
		});
		
    	
		//*******************************************************************************************//
		//									View settings    										 //
		//*******************************************************************************************//
    	editSearchProjects.setOnEditorActionListener(new TextView.OnEditorActionListener() {
    	    @Override
    	    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    	        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
    	            //performSearch();
    	            return true;
    	        }
    	        return false;
    	    }

    	});
		
		//*******************************************************************************************//
		//									Create and assign the images							 //
		//*******************************************************************************************//
        //
		// If we do not have a image stored for the user, create the default user image drawable
		//
        Drawable plusImage = getResources().getDrawable(R.drawable.icon_add_48dp_black_circle);

        //
    	// Assign the drawable to the ImageView
        //
    	imageViewCreateProject.setImageDrawable(plusImage);

    	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.projects, menu);
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
