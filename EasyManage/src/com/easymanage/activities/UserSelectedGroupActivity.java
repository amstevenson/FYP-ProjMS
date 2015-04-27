package com.easymanage.activities;

import com.easymanage.R;
import com.easymanage.fragments.UserSelectedGroupMembersFragment;
import com.easymanage.fragments.UserSelectedGroupProjectsFragment;

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

/**
 * 
 * When a group has been selected, two fragments are created that shift the focus
 * between group projects and members. Different fragments are called dependant 
 * on the tab which is selected. This activity can be thought of as the parent, and the
 * fragments are the children. 
 * 
 * @author Adam Stevenson
 *
 */
public class UserSelectedGroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_selected_group);
		
		setTitle("Group Details");
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
		//
		// Retrieve the users group Id and userId
		//
		Intent intent = getIntent();
		String groupId = intent.getStringExtra("groupId");
		String userId  = intent.getStringExtra("userId");
				
		//*******************************************************************************************//
		//									Create the fragment tabs								 //
		//*******************************************************************************************//
				
		getActionBar().setDisplayHomeAsUpEnabled(true);
				
		// Create the ActionBar and set the navigation mode
		//
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				
		//
		// Create the ActionBar tabs for both group projects and members
		//
		ActionBar.Tab allGroupProjectsTab = actionBar.newTab().setText(getString(R.string.tab_all_group_projects));
		ActionBar.Tab allGroupMembersTab  = actionBar.newTab().setText(getString(R.string.tab_all_group_members));
				
		//
		// Add arguments to fragments
		//
		Bundle groupBundle = new Bundle();
		groupBundle.putString("groupId", groupId);
		groupBundle.putString("userId", userId);	
		
		//
		// Create the fragments; allocate a class to each fragment specified.
		// This will be a class that returns a view that contains information specific to the fragment.
		//
		Fragment allGroupProjectsFragment = new UserSelectedGroupProjectsFragment();
		allGroupProjectsFragment.setArguments(groupBundle);
				
		Fragment allGroupMembersFragment  = new UserSelectedGroupMembersFragment();
		allGroupMembersFragment.setArguments(groupBundle);
				
		//
		// For each fragment we have, create a transaction for each one.
		//
		// Starting with all of the projects
		FragmentTransaction allTasksFragmentTransaction = getFragmentManager().beginTransaction();
		allTasksFragmentTransaction.add(R.id.selectedGroupFragmentContainer, allGroupProjectsFragment);
		allTasksFragmentTransaction.commit();
				
		// Next, all of the files for the members
		FragmentTransaction allFilesFragmentTransaction = getFragmentManager().beginTransaction();
		allFilesFragmentTransaction.add(R.id.selectedGroupFragmentContainer, allGroupMembersFragment);
		allFilesFragmentTransaction.commit();
				
		//
		// Set a tab listener for each fragment; so when it is clicked, that view is returned.
		//
		allGroupProjectsTab.setTabListener(new ActionBarTabListener(allGroupProjectsFragment, getApplicationContext()));
		allGroupMembersTab.setTabListener(new ActionBarTabListener(allGroupMembersFragment, getApplicationContext()));
				
		//
		// Add the fragments to the ActionBar tabs created above
		//
		actionBar.addTab(allGroupProjectsTab);
		actionBar.addTab(allGroupMembersTab);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_selected_group, menu);
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
	    	ft.replace(R.id.selectedGroupFragmentContainer, fragment);
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
