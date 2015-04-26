package com.easymanage.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.easymanage.activities.CreateOrEditEventActivity;
import com.easymanage.activities.GoogleMapObjectActivity;
import com.easymanage.utilities.JsonParser;
import com.easymanage.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListAllEventsAdapter extends BaseAdapter{

	private Context							   context;
	private ArrayList<HashMap<String, String>> userEvents;
	private LayoutInflater 					   mInflater;
	
	// For retrieving and passing the location to the GoogleMapsObject
	private String location 						= "";
	
	//
    // JSON/key Node names that will be used for all of the event tags 
	//
	private static final String TAG_EVENTNAME        = "eventName";
    private static final String TAG_EVENTLOCATION    = "eventLocation";
    private static final String TAG_EVENTPOSTCODE    = "eventPostcode";
    private static final String TAG_EVENTDESCRIPTION = "eventDescription";
    private static final String TAG_EVENTDATE        = "eventDate";
    private static final String TAG_EVENTSTARTIME    = "eventStartTime";
    private static final String TAG_EVENTENDTIME     = "eventEndTime";
	
    //
    // JSON Key nodes for Asynchronous Task
    //
    private static final String TAG_LOCATION  = "location";
	private static final String TAG_RESULTS   = "results";
    private static final String TAG_GEOMETRY  = "geometry";
    private static final String TAG_LAT       = "lat";
	private static final String TAG_LNG       = "lng";
    
    //
    // Two arraylists of strings to keep track of longitudes and latitudes
    //
    private ArrayList<String> longitudes;
    private ArrayList<String> latitudes;
    
    //
    // Default constructor
    //
    public ListAllEventsAdapter(Context context, ArrayList<HashMap<String, String>> userEvents){
		this.context 		= context;
		this.userEvents   = userEvents;
    }
    
	@Override
	public int getCount() {

		return userEvents.size();
	}

	@Override
	public Object getItem(int position) {

		return userEvents.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	
	// Set the location so it can be referenced in the on click listener for each item
	public void setLocation(String location)
	{
		this.location = location;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_all_events , parent, false);
        }
		
		TextView txtEventName        = (TextView ) convertView.findViewById(R.id.eventName);
		TextView txtEventLocation    = (TextView ) convertView.findViewById(R.id.eventLocation);
		TextView txtEventDescription = (TextView ) convertView.findViewById(R.id.eventDescription);
		TextView txtEventDate        = (TextView ) convertView.findViewById(R.id.eventDate);
		TextView txtEventStartTime   = (TextView ) convertView.findViewById(R.id.eventStartTime);
		TextView txtEventEndTime     = (TextView ) convertView.findViewById(R.id.eventEndTime);
		ImageView imgGoogleMap       = (ImageView) convertView.findViewById(R.id.eventMap);
		TextView imgEdit			 = (TextView) convertView.findViewById(R.id.eventEdit);
		
		//
		// Provide the information for each item
		//
		txtEventName.setText(userEvents.get(position).get(TAG_EVENTNAME).toString());
		
		txtEventLocation.setText("Location: " + userEvents.get(position).get(TAG_EVENTLOCATION).toString()
				+ ", " + userEvents.get(position).get(TAG_EVENTPOSTCODE).toString());
		setLocation(userEvents.get(position).get(TAG_EVENTLOCATION).toString() + ", " + 
				userEvents.get(position).get(TAG_EVENTPOSTCODE).toString());
		
		txtEventDescription.setText(userEvents.get(position).get(TAG_EVENTDESCRIPTION).toString());
		txtEventDate.setText("Date: " + userEvents.get(position).get(TAG_EVENTDATE).toString());
		txtEventStartTime.setText("Starts: " + userEvents.get(position).get(TAG_EVENTSTARTIME).toString());
		txtEventEndTime.setText("Ends: " + userEvents.get(position).get(TAG_EVENTENDTIME).toString());

		final int innerPosition = position;
		
		//
		// If the edit button is clicked, send to edit page for project
		//
		imgEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditEventActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("eventId", userEvents.get(innerPosition).get("eventId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		//
		// If the google images map is selected, transfer the location information to the
		// google maps object class
		//
		imgGoogleMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// Initialise arraylists
				longitudes = new ArrayList<String>();
				latitudes  = new ArrayList<String>();
				
				// Create intent 
				Intent intent = new Intent(v.getContext(), GoogleMapObjectActivity.class);
				intent.setFlags(268435456);
				
				try {
					// Retrieve results from GooglePlacesAPI
					// ".get()" tells the main thread to wait.
					new RetrieveGoogleMapLocation().execute().get();
					
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Pass the location information to the next activity
				if(latitudes.size() > 0 && longitudes.size() > 0)
				{
					intent.putExtra("location", location);
					intent.putStringArrayListExtra("latitudes", latitudes);
					intent.putStringArrayListExtra("longitudes", longitudes);
					v.getContext().startActivity(intent);
				}
				else
				{
					// Inform the user that no results have been found.
					Toast.makeText(v.getContext(), "No longitude and latitude information has been found for the specified location"
							, Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		txtEventDescription.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditEventActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("eventId", userEvents.get(innerPosition).get("eventId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		txtEventName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditEventActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("eventId", userEvents.get(innerPosition).get("eventId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		return convertView;
	}

	/**
	 * 
	 * This class is used to retrieve information relating to the 
	 * longitude and latitudes received from the GooglePlacesAPI
	 * 
	 */
	class RetrieveGoogleMapLocation extends AsyncTask<String, Boolean, String> {

	    /**
	     * Before starting background thread Show Progress Dialog
	     * */
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	    }
		
		@Override
		protected String doInBackground(String... args) {
			
		    // Find the longitude and latitude using GooglePlaces API
		    // There is most likely going to be inaccuracy as a response to this, and is dependent on the amount
		    // of information that the user wishes to provide in the location field. Providing a postcode for example
		    // obviously helps in this matter.
		    JsonParser jsonParser = new JsonParser();
		    
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		    parameters.add(new BasicNameValuePair("query", location));
		    parameters.add(new BasicNameValuePair("key", "AIzaSyDU3ZDN-wKRktsn7SgUWOrLFxe-GG1u1hc"));
			
			try {
				// Attempt to retrieve a response from Google Places
			    JSONObject placesDetails = jsonParser.makeHttpRequest("https://maps.googleapis.com/maps/api/place/textsearch/json",
			    		"GET", parameters);
				
				if(placesDetails!=null)
				{
					// Retrieve the JSONArray of places
	                JSONArray results = placesDetails.getJSONArray(TAG_RESULTS);
	 
	                for (int i = 0; i < results.length(); i++) {
	                	
	                	// For each result we have search for the geometry JSONObject
	                    JSONObject result = results.getJSONObject(i);
	                    
	                    // Then for each JSONObject within this array, find the location JSONObject
	                    JSONObject geometry = result.getJSONObject(TAG_GEOMETRY);
	                    
	                    // As a last step, retrieve the location JSONObject
	                    JSONObject loc = geometry.getJSONObject(TAG_LOCATION);
	                    
	                    // Store each value to the ArrayList
	                    latitudes.add(loc.getString(TAG_LAT));
	                    longitudes.add(loc.getString(TAG_LNG));
	                }
				}
				else
				{
				}
	        } catch (NullPointerException e){
	        	e.printStackTrace();
	        } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		
		protected void onPostExecute (String postExecuteString){

  
		}
	}
	
}
