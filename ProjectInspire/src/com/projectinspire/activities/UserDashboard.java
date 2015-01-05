package com.projectinspire.activities;

import com.projectinspire.R;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class UserDashboard extends Activity {

    // Navigational menu variables
	private DrawerLayout 		  navigationMenuLayout;
	private ListView 			  navigationMenuList;
	private ActionBarDrawerToggle navigationMenuToggle;
	
	// The title for the navigation menu
	private CharSequence 		  navigtationMenuTitle;
	
	// The title of the application
	private CharSequence 		  applicationTitle;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_dashboard);
		
		// Create the navigation menu
		createNavigationMenu();
		
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
		// create the layout and ListView for the navigation bar
		navigationMenuLayout = (DrawerLayout) findViewById(R.id.dashboard_drawer_nav_layout);
		navigationMenuList   = (ListView)     findViewById(R.id.dashboard_drawer_menu_listview);
		
		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
				
		navigationMenuToggle = new ActionBarDrawerToggle(this, navigationMenuLayout,
				R.drawable.nav_menu_icon, // nav menu toggle icon
				R.string.app_name, // nav drawer open 
				R.string.app_name) {
					
			public void onDrawerClosed(View view) {
				// send this view to the back
				navigationMenuLayout.bringChildToFront(view);
						
				getActionBar().setTitle(applicationTitle);
						
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}
					
			public void onDrawerOpened(View drawerView) {
				// bring this view to the front
				navigationMenuLayout.bringToFront();
						
				getActionBar().setTitle(navigtationMenuTitle);
						
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		
		navigationMenuLayout.setDrawerListener(navigationMenuToggle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (navigationMenuToggle.onOptionsItemSelected(item)) return true;
		
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
		// if the navigation drawer is opened, then hide the other menu items
		// well, they are basically hidden if the menu is not open basically.
		boolean drawerOpen = navigationMenuLayout.isDrawerOpen(navigationMenuList);
		
		// if the drawer is closed, show the other menu items
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void setTitle(CharSequence title) {
		
		// Set the navigation drawer title
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
		
		// before onRestore state, sync the toggle
		navigationMenuToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Any changes to configurations needs to be registered
		navigationMenuToggle.onConfigurationChanged(newConfig);
	}
	
}
