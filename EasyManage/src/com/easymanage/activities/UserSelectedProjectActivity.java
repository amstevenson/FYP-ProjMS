package com.easymanage.activities;

import com.easymanage.fragments.UserSelectedProjectAllFilesFragment;
import com.easymanage.fragments.UserSelectedProjectTasksFragment;
import com.easymanage.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UserSelectedProjectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_selected_project);
		
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user email
    	//
		String projectId = "";
		
    	Intent intent = getIntent();
    	projectId		  = intent.getStringExtra("projectId");
		
		
		//*******************************************************************************************//
		//									Create the fragment tabs								 //
		//*******************************************************************************************//
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Create the ActionBar and set the navigation mode
		//
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//
		// Create the ActionBar tabs for both tasks and files
		//
		ActionBar.Tab allTasksTab = actionBar.newTab().setText(getString(R.string.tab_all_tasks));
		ActionBar.Tab allFilesTab  = actionBar.newTab().setText(getString(R.string.tab_all_files));
		
		//
		// Add arguments to fragments
		//
		Bundle projectBundle = new Bundle();
		projectBundle.putString("projectId", projectId);
		
		//
		// Create the fragments; allocate a class to each fragment specified.
		// This will be a class that returns a view that contains information specific to the fragment.
		//
		Fragment allTasksFragment 	     = new UserSelectedProjectTasksFragment();
		allTasksFragment.setArguments(projectBundle);
		
		Fragment allFilesFragment		 = new UserSelectedProjectAllFilesFragment();
		allFilesFragment.setArguments(projectBundle);
		
		//
		// For each fragment we have, create a transaction for each one.
		//
		// Starting with all of the tasks
		FragmentTransaction allTasksFragmentTransaction = getFragmentManager().beginTransaction();
		allTasksFragmentTransaction.add(R.id.selectedProjectFragmentContainer, allTasksFragment);
		allTasksFragmentTransaction.commit();
		
		// Next, all of the files for the project
		FragmentTransaction allFilesFragmentTransaction = getFragmentManager().beginTransaction();
		allFilesFragmentTransaction.add(R.id.selectedProjectFragmentContainer, allFilesFragment);
		allFilesFragmentTransaction.commit();
		
		//
		// Set a tab listener for each fragment; so when it is clicked, that view is returned.
		//
		allTasksTab.setTabListener(new ActionBarTabListener(allTasksFragment, getApplicationContext()));
		allFilesTab.setTabListener(new ActionBarTabListener(allFilesFragment, getApplicationContext()));
		
		//
		// Add the fragments to the ActionBar tabs created above
		//
		actionBar.addTab(allTasksTab);
		actionBar.addTab(allFilesTab);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_selected_project, menu);
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
	
	class ActionBarTabListener implements ActionBar.TabListener{

	    public Fragment fragment;
	    public Context context;

	    public ActionBarTabListener(Fragment fragment, Context context) {
	        this.fragment = fragment;
	        this.context = context;
	    }

	    @Override
	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	       //
	       // if reselected, nothing changes
	       //
	    }

	    @Override
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        //
	    	// fragment is selected - populate the view
	        //        
	    	ft.replace(R.id.selectedProjectFragmentContainer, fragment);
	    }

	    @Override
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    	//
	    	// remove fragment when unselected
	        //
	    	ft.remove(fragment);
	    }
	}
}
