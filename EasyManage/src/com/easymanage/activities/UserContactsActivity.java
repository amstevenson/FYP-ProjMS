package com.easymanage.activities;

import com.easymanage.fragments.UserContactsFragment;
import com.easymanage.fragments.UserContactsMessagesFragment;
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

/***
 * 
 * This class creates two fragments that provide views for both viewing all
 * of the contacts a user may have, in addition to providing all of their messages.
 * 
 * Although this was created by me (Adam), the android tutorial provided a basis for
 * the understanding of how to implement it: 
 * http://developer.android.com/training/basics/fragments/fragment-ui.html
 * 
 * @author Adam Stevenson
 *
 */
public class UserContactsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_contacts);
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
    	//
    	// Retrieve the user email
    	//
		String userId = "";
		
    	Intent intent = getIntent();
    	userId		  = intent.getStringExtra("userId");
		String userEmail  = intent.getStringExtra("userEmail");
    	
		//*******************************************************************************************//
		//									Create the fragment tabs								 //
		//*******************************************************************************************//
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//
		// Create the ActionBar and set the navigation mode
		//
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//
		// Add arguments to fragments
		//
		Bundle userBundle = new Bundle();
		userBundle.putString("userId", userId);
		userBundle.putString("userEmail", userEmail);
		
		//
		// Create the ActionBar tabs for both all contacts and all messages
		//
		ActionBar.Tab allContactsTab = actionBar.newTab().setText(getString(R.string.tab_contacts));
		ActionBar.Tab allMessagesTab  = actionBar.newTab().setText(getString(R.string.tab_contacts_messages));
		
		//
		// Create the fragments; allocate a class to each fragment specified.
		// This will be a class that returns a view that contains information specific to the fragment.
		//
		Fragment allContactsFrag 	     = new UserContactsFragment();
		allContactsFrag.setArguments(userBundle);
		
		Fragment allContactsMessagesFrag = new UserContactsMessagesFragment();
		allContactsMessagesFrag.setArguments(userBundle);
		
		//
		// For each fragment we have, create a transaction for each one.
		//
		// Starting with all of the contacts
		FragmentTransaction allContactsFragmentTransaction = getFragmentManager().beginTransaction();
		allContactsFragmentTransaction.add(R.id.contactsFragmentContainer, allContactsFrag);
		allContactsFragmentTransaction.commit();
		
		// Next, all of the messages for the user
		FragmentTransaction allContactsMessagesFragmentTransaction = getFragmentManager().beginTransaction();
		allContactsMessagesFragmentTransaction.add(R.id.contactsFragmentContainer, allContactsMessagesFrag);
		allContactsMessagesFragmentTransaction.commit();
		
		//
		// Set a tab listener for each fragment; so when it is clicked, that view is returned.
		//
		allContactsTab.setTabListener(new ActionBarTabListener(allContactsFrag, getApplicationContext()));
		allMessagesTab.setTabListener(new ActionBarTabListener(allContactsMessagesFrag, getApplicationContext()));
		
		//
		// Add the fragments to the ActionBar tabs created above
		//
		actionBar.addTab(allContactsTab);
		actionBar.addTab(allMessagesTab);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_contacts, menu);
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
	       // if reselected, nothing changes
	    }

	    @Override
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        // fragment is selected - populate the view
	               
	    	ft.replace(R.id.contactsFragmentContainer, fragment);
	    }

	    @Override
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    	// remove fragment when unselected
	        ft.remove(fragment);
	    }
	}
}
