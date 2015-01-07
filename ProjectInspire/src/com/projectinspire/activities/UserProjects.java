package com.projectinspire.activities;

import com.projectinspire.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class UserProjects extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_projects);
		
		//
		// Create view variables
		//
		TextView txtProjectsNone = (TextView)findViewById(R.id.txtProjectsNone);
		ListView listProjectsAll = (ListView)findViewById(R.id.listProjectsAll);
		
		//
		// If we have no items, show this to the user
		// Chances are it will be the first time they have used the service too, so I
		// May want to add more user information than what is there currently.
		//
		if(listProjectsAll.getCount() == 0) txtProjectsNone.setVisibility(View.VISIBLE);
		else 								txtProjectsNone.setVisibility(View.GONE);	
		
		
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
