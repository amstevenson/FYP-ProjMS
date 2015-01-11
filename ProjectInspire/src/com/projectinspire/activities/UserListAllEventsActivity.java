package com.projectinspire.activities;

import com.projectinspire.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserListAllEventsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list_all_events);
		
		//
		// Create view variables
		//
		TextView txtEventsNone = (TextView)findViewById(R.id.txtEventNone);
		ListView listEventsAll = (ListView)findViewById(R.id.listEventsAll);
		ImageView imageViewCreateEvent = (ImageView)findViewById(R.id.imageViewEventCreate);
		
		//
		// If we have no items, show this to the user
		// Chances are it will be the first time they have used the service too, so I
		// May want to add more user information than what is there currently.
		//
		if(listEventsAll.getCount() == 0) txtEventsNone.setVisibility(View.VISIBLE);
		else 								txtEventsNone.setVisibility(View.GONE);	
				
		//
		// If the create a new project button is pressed
		//
		imageViewCreateEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent createProject = new Intent(getApplicationContext(), CreateOrEditEventActivity.class);
				startActivity(createProject);
				
			}
		});
		
		//*******************************************************************************************//
		//									Create and assign the images							 //
		//*******************************************************************************************//
        //
		// If we do not have a image stored for the user, create the default user image drawable
		//
        Drawable plusImage = getResources().getDrawable(R.drawable.icon_add_48dp_white_circle);

        //
    	// Assign the drawable to the ImageView
        //
    	imageViewCreateEvent.setImageDrawable(plusImage);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_list_all_events, menu);
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
