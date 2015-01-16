package com.projectinspire.activities;

import com.projectinspire.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserDashboardActivity extends Activity {

    // Navigation menu variables
	private DrawerLayout 		  navigationMenuLayout;
	private ListView 			  navigationMenuList;
	private ActionBarDrawerToggle navigationMenuToggle;
	
	// The title for the navigation menu
	private CharSequence 		  navigtationMenuTitle;
	
	// The title of the application
	private CharSequence 		  applicationTitle;
	
	// ScrollView
	ScrollView 					  scrollViewDashboard;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_dashboard);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Your Dashboard");
		
		//
		// Determine the views
		//
		TextView  projectSummary  = (TextView) this.findViewById(R.id.txtDashboardProjects);
		TextView  projectTitle    = (TextView) this.findViewById(R.id.txtDashboardProjectsTitle);
		TextView  messagesSummary = (TextView) this.findViewById(R.id.txtDashboardMessages);
		TextView  messagesTitle   = (TextView) this.findViewById(R.id.txtDashboardMessagesTitle);
		TextView  eventsSummary   = (TextView) this.findViewById(R.id.txtDashboardEvents);
		TextView  eventsTitle     = (TextView) this.findViewById(R.id.txtDashboardEventsTitle);
		ImageView userImage       = (ImageView) this.findViewById(R.id.imgviewDashboardUser);
		
		//
		// Set the on click listeners for the activity
		//
		projectSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allProjects = new Intent(getApplicationContext(),UserListAllProjectsActivity.class);
				startActivity(allProjects);
			}
		});
		
		projectTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allProjects = new Intent(getApplicationContext(),UserListAllProjectsActivity.class);
				startActivity(allProjects);
			}
		});
		
		messagesSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allMessages = new Intent(getApplicationContext(), UserContactsActivity.class);
				startActivity(allMessages);
				
			}
		});
		
		messagesTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent allMessages = new Intent(getApplicationContext(), UserContactsActivity.class);
				startActivity(allMessages);
				
			}
		});
		
		eventsSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allEvents = new Intent(getApplicationContext(), UserListAllEventsActivity.class);
				startActivity(allEvents);
				
			}
		});
		
		eventsTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allEvents = new Intent(getApplicationContext(), UserListAllEventsActivity.class);
				startActivity(allEvents);
				
			}
		});
		
		
		//
		// Create the navigation menu
		//
		createNavigationMenu();
		
		//*******************************************************************************************//
		//									Create user image										 //
		//*******************************************************************************************//
        //
		// If we do not have a image stored for the user, create the default user image drawable
		//
        Drawable addUserImage = getResources().getDrawable(R.drawable.icon_person_test);

        //
    	// Assign the drawable to the ImageView
        //
    	userImage.setImageDrawable(addUserImage);
		
	}
	
	/***
	 * This method is responsible for setting up the navigation menu.
	 * The user profile (picture, name, email address) will be displayed at the top
	 * of the drawer and links to other activities will be presented below.
	 * Adapters are used to collect and specify the information that will be used.
	 * 
	 * @see names of adapters etc
	 */
	public void createNavigationMenu()
	{
		//
		// create the layout and ListView for the navigation bar
		//
		navigationMenuLayout = (DrawerLayout) findViewById(R.id.dashboard_drawer_nav_layout);
		navigationMenuList   = (ListView)     findViewById(R.id.dashboard_drawer_menu_listview);
		scrollViewDashboard  = (ScrollView) findViewById(R.id.scrollViewDashboard);
		
		//
		// enabling action bar application icon and behaving it as toggle button
		//
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
				
		navigationMenuToggle = new ActionBarDrawerToggle(this, navigationMenuLayout,
				R.drawable.nav_menu_icon, // icon for the navigation menu in the action bar
				R.string.app_name, 		  // title for when the navigation menu is open
				R.string.app_name) {	  // title for when the navigation menu is closed
					
			public void onDrawerClosed(View view) {
				
				//
				// Bring the scrollView to the front, if the navigation menu is closed
				//
				scrollViewDashboard.bringToFront();		
				
				//
				// set the title of the activity
				//
				getActionBar().setTitle(applicationTitle);
					
				//
				// calling onPrepareOptionsMenu() to show action bar icons
				//
				invalidateOptionsMenu();
			}
					
			public void onDrawerOpened(View drawerView) {
				
				//
				// bring the navigation drawer to the front
				// so that it can be seen and used
				//
				navigationMenuLayout.bringToFront();
				
				//
				// set the title
				//
				getActionBar().setTitle(navigtationMenuTitle);
						
				//
				// calling onPrepareOptionsMenu() to hide action bar icons
				//
				invalidateOptionsMenu();
			}
		};
		
		//
		// set the drawer listener for the navgiation menu
		//
		navigationMenuLayout.setDrawerListener(navigationMenuToggle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		//
		// Inflate the menu; this adds items to the action bar if it is present.
		//
		getMenuInflater().inflate(R.menu.user_dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		//
		// toggle nav drawer on selecting action bar app icon/title
		//
		if (navigationMenuToggle.onOptionsItemSelected(item))
		{
			navigationMenuList.bringToFront();
			return true;
		}
		
		//
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/***
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		//
		// if the navigation drawer is opened, then hide the other menu items
		// well, they are basically hidden if the menu is not open basically.
		//
		boolean drawerOpen = navigationMenuLayout.isDrawerOpen(navigationMenuList);
		
		//
		// if the drawer is closed, show the other menu items
		//
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		
		//
		// Set the navigation drawer title
		//
		navigtationMenuTitle = title;
		getActionBar().setTitle(navigtationMenuTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		
		//
		// before onRestore state, sync the toggle
		//
		navigationMenuToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		//
		// Any changes to configurations needs to be registered
		//
		navigationMenuToggle.onConfigurationChanged(newConfig);
	}
	
}
