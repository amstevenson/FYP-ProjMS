package com.easymanage.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class NetworkStateOperations {

	public static Boolean testNetworkConnection(Context c) {
	    
		Boolean networkState = false;
		
	    ConnectivityManager cm = (ConnectivityManager) c
	    		.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();

	    if (networkInfo != null && networkInfo.isConnected()) {
	       try {
	    	   networkState = new downloadWebPage().execute().get();
	       } catch (InterruptedException | ExecutionException e) {

				Log.d("Interrupted Exception/Excecution exeception detected", e.getMessage());
	       }
	    } else {
	    	networkState = false;
	        Toast.makeText(c, "Error: There is no active internet connection currently available.", Toast.LENGTH_LONG).show();
	    } 

	    return networkState;
	}
}

class downloadWebPage extends AsyncTask<String, Void, Boolean> {

    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        
        
    }
	
	
	@Override
	protected Boolean doInBackground(String... params) {
		
		// params comes from the execute() call: params[0] is the url.
        try {
            return downloadUrl("http://www.google.com");
        } catch (IOException e) {
            return false;
        }
	}
	
	/*
	 * After completing background task, dismiss the progress dialog
	 * and determine what will be done with the results
	 */
	protected void onPostExecute (String file_url){

		
	}

	// Given a URL, establishes an HttpUrlConnection and retrieves
	// the web page content as an InputStream, which it returns as
	// a string.
	private Boolean downloadUrl(String myurl) throws IOException {
	    
		Boolean downloadStatus = false;
		InputStream is = null;
	    // Only display the first 500 characters of the retrieved
	    // web page content.
	    //int len = 500;
	        
	    try {
	        URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(10000 /* milliseconds */);
	        conn.setConnectTimeout(15000 /* milliseconds */);
	        conn.setRequestMethod("GET");
	        conn.setDoInput(true);
	        // Starts the query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d("Internet test for google.com", "The response is: " + response);
	        is = conn.getInputStream();
	
	        // Convert the InputStream into a string
	        if(response == 200) downloadStatus = true;
	        
	    // Makes sure that the InputStream is closed after the app is
	    // finished using it.
	    } finally {
	        if (is != null) {
	            is.close();
	        } 
	    }
	    
	    return downloadStatus;
	    
	}
}
