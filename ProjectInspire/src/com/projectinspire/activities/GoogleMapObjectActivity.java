package com.projectinspire.activities;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.projectinspire.R;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 
 * This Google Map Object class builds on from the Android tutorial that can be found
 * at http://www.vogella.com/tutorials/AndroidGoogleMaps/article.html#maps_device.
 * 
 * A Parser has been used to find the longitude and latitude for the address that was
 * passed via the intent, from the item selected from the ListAllEventsAdapter class.
 * 
 * @author Adam Stevenson
 *
 */
public class GoogleMapObjectActivity extends Activity implements LocationListener, LocationSource {

	private GoogleMap map;
	private OnLocationChangedListener mListener;
	private LocationManager locationManager;
	  
	private ArrayList<String> longitudes;
	private ArrayList<String> latitudes;
	private String location;
	private LatLng[] latlngs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map_object);
		
		// retrieve the array containing the elements of the property
		Intent mapIntent = getIntent(); 
		
		location   = mapIntent.getStringExtra("location");
		longitudes = mapIntent.getStringArrayListExtra("longitudes");
		latitudes  = mapIntent.getStringArrayListExtra("latitudes");
		latlngs    = new LatLng[latitudes.size()];
		
		setTitle("Map View for: " + location);
		
		// Convert each latitude and longitude into an instance of a LatLng
		for(int i = 0; i < latitudes.size(); i ++)
		{
			LatLng tempLatLng = new LatLng(Double.valueOf(latitudes.get(i)), Double.valueOf(longitudes.get(i)));
			latlngs[i] = tempLatLng;
		}
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
	    if(locationManager != null)
	    {
	        boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	        boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	        if(gpsIsEnabled)
	        {
	            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000L, 10F, this);
	        }
	        else if(networkIsEnabled)
	        {
	            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
	        }
	        else
	        {
	            //Show an error dialog that GPS is disabled.
	        }
	    }
	    else
	    {
	        //Show a generic error dialog since LocationManager is null for some reason
	    }

	    // Set up the map if it is required (if the CameraUpdateFactory has not been initialised).
	    setUpMapIfNeeded();
	    
	    // Create the markers
	    for(int i = 0; i < longitudes.size(); i++)
	    {
			@SuppressWarnings("unused")
			Marker newMarker = map.addMarker(new MarkerOptions().position(latlngs[i])
	    			.title("Retrieved Location:" + i)
	    			.icon(BitmapDescriptorFactory
	    		            .fromResource(R.drawable.google_map_marker)));
	    }
	    
	    // Change the users location on the map to the first retrieved value
		CameraUpdate center= CameraUpdateFactory.newLatLng(latlngs[0]);  // The location of the place
		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);				 // The zoom factor

		map.moveCamera(center);
		map.animateCamera(zoom);
	}
	
	
	@Override
	public void onPause()
	{
	    if(locationManager != null)
	    {
	        locationManager.removeUpdates(this);
	    }

	    super.onPause();
	}

	@Override
	public void onResume()
	{
	    super.onResume();

	    setUpMapIfNeeded();

	    if(locationManager != null)
	    {
	        map.setMyLocationEnabled(true);
	    }
	}
	
	/**
	 * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
	 * installed) and the map has not already been instantiated.. This will ensure that we only ever
	 * call {@link #setUpMap()} once when {@link #map} is not null.
	 * <p>
	 * If it isn't installed {@link SupportMapFragment} (and
	 * {@link com.google.android.gms.maps.MapView
	 * MapView}) will show a prompt for the user to install/update the Google Play services APK on
	 * their device.
	 * <p>
	 * A user can return to this Activity after following the prompt and correctly
	 * installing/updating/enabling the Google Play services. Since the Activity may not have been
	 * completely destroyed during this process (it is likely that it would only be stopped or
	 * paused), {@link #onCreate(Bundle)} may not be called again so we should call this method in
	 * {@link #onResume()} to guarantee that it will be called.
	 */
	private void setUpMapIfNeeded() {
	    // Do a null check to confirm that we have not already instantiated the map.
	    if (map == null) 
	    {
	        // Try to obtain the map from the Fragment Manager
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
			        .getMap();
			
	        // Check if we were successful in obtaining the map.
	        if (map != null) 
	        {
	            setUpMap();
	        }

	        //This is how you register the LocationSource
	        map.setLocationSource(this);
	    }
	}
	
	/**
	 * This is where markers or lines are added, in addition to listeners and camera movement.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap} is not null.
	 */
	private void setUpMap() 
	{
	    map.setMyLocationEnabled(true);
	}
	
	@Override
	public void onLocationChanged(Location location) 
	{
	    if( mListener != null )
	    {
	        mListener.onLocationChanged( location );

	        //Move the camera to the user's location and zoom in!
	        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f));
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_map_object, menu);
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

	@Override
	public void activate(OnLocationChangedListener listener) 
	{
	    mListener = listener;
	}

	@Override
	public void deactivate() 
	{
	    mListener = null;
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
	    // If the Internet is disconnected, notify the user
	    Toast.makeText(this, "provider disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
		// If the Internet is enabled, notify the user.
	    Toast.makeText(this, "provider enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
	}

}