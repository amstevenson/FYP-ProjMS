package com.easymanage.activities;

import java.util.ArrayList;

import com.easymanage.navslider.adapter.NavigationDrawerListAdapter;
import com.easymanage.navslider.model.NavigationDrawerItem;
import com.easymanage.navslider.model.NavigationDrawerProfileItem;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.easymanage.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * The dashboard allows the user to navigate to every place available in the application.
 * The main points of interest are: projects, groups, events and contacts.
 * 
 * @author Adam Stevenson
 *
 */
public class UserDashboardActivity extends Activity {

    // Navigation XML Menu variables
	private DrawerLayout 		  navigationMenuLayout;
	private ListView 			  navigationMenuList;
	private ActionBarDrawerToggle navigationMenuToggle;
	
    // Navigation tab lists to store values.
	// -- These are used to store all of the information
	// -- that relates to the navigation tabs.
	private ArrayList<NavigationDrawerItem> 		navigationDrawerItems;
	private ArrayList<NavigationDrawerProfileItem>  navigationDrawerProfileItem;
	private NavigationDrawerListAdapter 			navigationMenuAdapter;
	
	// Individual Navigation list item variables.
	// -- For each tab we have, if it is user information
	// -- use the background image, profile image etc.
	// -- Else use the icon and navigation tab name.
	private Bitmap 				  profileBackgroundImage;
	private Bitmap 				  profileImage;
	private String 				  profileName;
	private String 				  profileEmail;
	private String[] 			  navigationMenuTitles;
	private TypedArray 			  navigationMenuIcons;
	
	// The title for the navigation menu
	private CharSequence 		  navigtationMenuTitle;
	
	// ScrollView
	private ScrollView 			  scrollViewDashboard;
	
	private String 		  		  userId    = "empty";
	private String				  userEmail = "empty";
	
	// The user retrieved from the main activity
	private ParseUser currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_dashboard);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Your Dashboard");
		
		//
		// Determine the views
		//
		TextView dashboardUsername   = (TextView) this.findViewById(R.id.txtDashboardUsername);
		TextView dashboardEmail      = (TextView) this.findViewById(R.id.txtDashboardUserEmail);
		TextView  projectSummary     = (TextView) this.findViewById(R.id.txtDashboardProjects);
		TextView  projectTitle       = (TextView) this.findViewById(R.id.txtDashboardProjectsTitle);
		TextView  projectMostRecent  = (TextView) this.findViewById(R.id.txtDashboardProjectMostRecentAdded);
		TextView  groupsSummary      = (TextView) this.findViewById(R.id.txtDashboardGroups);
		TextView  groupsTitle        = (TextView) this.findViewById(R.id.txtDashboardTasksTitle);
		TextView  groupsMostRecent   = (TextView) this.findViewById(R.id.txtDashboardMostRecentGroups);
		TextView  messagesSummary    = (TextView) this.findViewById(R.id.txtDashboardMessages);
		TextView  messagesTitle      = (TextView) this.findViewById(R.id.txtDashboardMessagesTitle);
		TextView  messagesMostRecent = (TextView) this.findViewById(R.id.txtDashboardMostRecentMessages);
		TextView  eventsSummary      = (TextView) this.findViewById(R.id.txtDashboardEvents);
		TextView  eventsTitle        = (TextView) this.findViewById(R.id.txtDashboardEventsTitle);
		TextView  eventsMostRecent   = (TextView) this.findViewById(R.id.txtDashboardMostRecentEvents);
		final ImageView userImage    = (ImageView) this.findViewById(R.id.imgviewDashboardUser);
		
		//*******************************************************************************************//
		//									retrieve user information								 //
		//*******************************************************************************************//
		//
		// Retrieve the user email
		//
		Intent intent = getIntent();
		userId		  = intent.getStringExtra("userId");
		
		//
		// If the user has just registered
		//
		Boolean justRegistered = intent.getBooleanExtra("justRegistered", false);
		
		if(justRegistered == true)
		{
			//
			// Notify Registration success with a toast message
			//
			Toast.makeText(getApplicationContext(),
					"You have successfully registered your account and have been " +
					"automatically logged in.",
					Toast.LENGTH_LONG).show();
		}
		
		Log.d("Dashboard - Id", userId); // for debugging
		    	
		currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) 
		{	  
			//
		    // Set the users information
		    //
		    dashboardUsername.setText(currentUser.get("forename").toString() + " " + currentUser.get("surname").toString());
		    userEmail = currentUser.getEmail();
		    dashboardEmail.setText(userEmail);
		    		
		    ParseFile fileObject = (ParseFile) currentUser.get("userImageFile");
		    fileObject.getDataInBackground(new GetDataCallback() {
		    	public void done(byte[] data, ParseException e) {
		    		if (e == null)
		            {
		    			//
		                // The bitmap will be constructed using the image file column
		                // that is stored in the Parse database.
		                //
		                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
		                                
		                //
		                // Set the image bitmap after constructing it from the byte array
		                // 
		                userImage.setImageBitmap(bmp);
		                                
		                } else {
		                	Log.d("Error: Code: " + e.getCode(), "Message: " + e.getMessage());
		                }
		            }
		        });
		    		
		} else 
		{
		    // show the signup or login screen.
			// Although this functionality being invoked would imply
			// that the logic regarding validation for the main login
			// activity has failed.
		}
		
		//
		// Set the on click listeners for the activity
		//
		projectSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allProjects = new Intent(getApplicationContext(),UserListAllProjectsActivity.class);
				allProjects.putExtra("userId", userId);
				startActivity(allProjects);
			}
		});
		
		projectTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allProjects = new Intent(getApplicationContext(),UserListAllProjectsActivity.class);
				allProjects.putExtra("userId", userId);
				startActivity(allProjects);
			}
		});
		
		projectMostRecent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allProjects = new Intent(getApplicationContext(),UserListAllProjectsActivity.class);
				allProjects.putExtra("userId", userId);
				startActivity(allProjects);
			}
		});
		
		groupsSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allGroups = new Intent(getApplicationContext(), UserListAllGroupsActivity.class);
				allGroups.putExtra("userId", userId);
				startActivity(allGroups);
				
			}
		});
		
		groupsTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allGroups = new Intent(getApplicationContext(), UserListAllGroupsActivity.class);
				allGroups.putExtra("userId", userId);
				startActivity(allGroups);
				
			}
		});
		
		groupsMostRecent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allGroups = new Intent(getApplicationContext(), UserListAllGroupsActivity.class);
				allGroups.putExtra("userId", userId);
				startActivity(allGroups);
				
			}
		});
		
		messagesSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allMessages = new Intent(getApplicationContext(), UserContactsActivity.class);
				allMessages.putExtra("userId", userId);
				allMessages.putExtra("userEmail", userEmail);
				startActivity(allMessages);
				
			}
		});
		
		messagesTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent allMessages = new Intent(getApplicationContext(), UserContactsActivity.class);
				allMessages.putExtra("userId", userId);
				allMessages.putExtra("userEmail", userEmail);
				startActivity(allMessages);
				
			}
		});
		
		messagesMostRecent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allMessages = new Intent(getApplicationContext(), UserContactsActivity.class);
				allMessages.putExtra("userId", userId);
				allMessages.putExtra("userEmail", userEmail);
				startActivity(allMessages);
				
			}
		});
		
		eventsSummary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allEvents = new Intent(getApplicationContext(), UserListAllEventsActivity.class);
				allEvents.putExtra("userId", userId);
				startActivity(allEvents);
				
			}
		});
		
		eventsTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allEvents = new Intent(getApplicationContext(), UserListAllEventsActivity.class);
				allEvents.putExtra("userId", userId);
				startActivity(allEvents);
				
			}
		});
		
		eventsMostRecent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent allEvents = new Intent(getApplicationContext(), UserListAllEventsActivity.class);
				allEvents.putExtra("userId", userId);
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
		
		// Name of the navigation function followed by the icon
		navigationMenuTitles = getResources().getStringArray(R.array.navigation_drawer_items);
		navigationMenuIcons  = getResources().obtainTypedArray(R.array.navigation_drawer_icons);
			
		// navigation function items
		navigationDrawerItems = new ArrayList<NavigationDrawerItem>();
		// Empty space container (else the first item will stack on top of the user profile)
		navigationDrawerItems.add(new NavigationDrawerItem());
		// Home
		navigationDrawerItems.add(new NavigationDrawerItem(navigationMenuTitles[0], navigationMenuIcons.getResourceId(0, -1)));
		// Properties
		navigationDrawerItems.add(new NavigationDrawerItem(navigationMenuTitles[1], navigationMenuIcons.getResourceId(1, -1)));
		navigationDrawerItems.add(new NavigationDrawerItem(navigationMenuTitles[2], navigationMenuIcons.getResourceId(2, -1)));
		// Messages
		navigationDrawerItems.add(new NavigationDrawerItem(navigationMenuTitles[3], navigationMenuIcons.getResourceId(3, -1)));
		// Payments
		navigationDrawerItems.add(new NavigationDrawerItem(navigationMenuTitles[4], navigationMenuIcons.getResourceId(4, -1)));
		// Log off
		navigationDrawerItems.add(new NavigationDrawerItem(navigationMenuTitles[5], navigationMenuIcons.getResourceId(5, -1)));
				
		// Recycle typed array 'navmenuicons'
		navigationMenuIcons.recycle();
				
		// load slide menu navigation bar
		// the profile of the user: 
		profileBackgroundImage = BitmapFactory.decodeResource(getResources(),R.drawable.drawer_list_shape_one);
		profileImage 		   = BitmapFactory.decodeResource(getResources(),R.drawable.icon_person_white_48dp);
		profileName 		   = "test";
		profileEmail 		   = "test";
				
		// navigation user profile
		navigationDrawerProfileItem = new ArrayList<NavigationDrawerProfileItem>();
		navigationDrawerProfileItem.add(new NavigationDrawerProfileItem(profileBackgroundImage, profileImage, profileName, profileEmail));
			
		// setting the nav drawer list adapter
		navigationMenuAdapter = new NavigationDrawerListAdapter(getApplicationContext(), navigationDrawerProfileItem,
				navigationDrawerItems, "Dashboard");
		navigationMenuList.setAdapter(navigationMenuAdapter);
		
		
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
				getActionBar().setTitle("Your Dashboard");
					
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
				getActionBar().setTitle("Your Dashboard");
						
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
		
		//
		// Set the onItemClickListener for the navigation drawer.
		// This will be used to send the user to the activity that
		// relates to the tab that they have clicked on.
		//
		navigationMenuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// Get the navigation item at the position that has been clicked on.
				NavigationDrawerItem selectedItem = navigationDrawerItems.get(position);
				
				// Retrieve the title by invoking by the getTitle method.
				String selectedItemTitle = selectedItem.getTitle();
				
				// Based on the title, navigate the user to a different activity
				switch(selectedItemTitle)
				{
					case "My Projects": 
						
						Intent projectsIntent = new Intent(getApplicationContext(), UserListAllProjectsActivity.class);
						
						// Pass the userId and start the activity
						projectsIntent.putExtra("userId", userId);
						projectsIntent.putExtra("userEmail", userEmail);
						startActivity(projectsIntent);
						
						break;
					
					case "My Messages": 
						
						Intent messageIntent = new Intent(getApplicationContext(), UserContactsActivity.class);
						
						// Pass the userId and start the activity
						messageIntent.putExtra("userId", userId);
						messageIntent.putExtra("userEmail", userEmail);
						startActivity(messageIntent);
						
						break;
					
					case "My Events": 
						
						Intent eventIntent = new Intent(getApplicationContext(), UserListAllEventsActivity.class);
						
						// Pass the userId and start the activity
						eventIntent.putExtra("userId", userId);
						eventIntent.putExtra("userEmail", userEmail);
						startActivity(eventIntent);
						
						break;
					
					case "My Groups": 
						
						Intent allGroups = new Intent(getApplicationContext(), UserListAllGroupsActivity.class);
						allGroups.putExtra("userId", userId);
						startActivity(allGroups);
						
						break;
					
					case "Change Account Details":
					
						Intent accountIntent = new Intent(getApplicationContext(), ChangeUserDetailsActivity.class);
						
						// Pass the userId and start the activity
						accountIntent.putExtra("userEmail", userEmail);
						startActivity(accountIntent);
						
						break;
					
					case "Log off": 
						
						Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
						
						//
						// Delete the cached information of the user
						//
						ParseUser.logOut();
						
						// Start the activity
						startActivity(mainIntent);
						
						// Finish the current activity.
						finish();
						
						break;
					
					default: break; // If the top part is clicked on (user information), or something has gone wrong.
				}
			}
		});
		
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
