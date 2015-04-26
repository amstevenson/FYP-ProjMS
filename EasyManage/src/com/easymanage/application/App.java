package com.easymanage.application;
import android.app.Application;
import com.parse.Parse;

/***
 * 
 * This class is used to initialise Parse.
 * If the data store is not enabled here, it would need to be replicated in every class
 *  (activity/fragment/adapter etc) that makes use of the API; this does it all in one go. 
 * 
 * @author Adam Stevenson
 *
 */
public class App extends Application {


    @Override public void onCreate() { 
        super.onCreate();
        
		// Enable Local Data store.
		Parse.enableLocalDatastore(this);		
        
        // Define project parameters for API
        Parse.initialize(this, "ZNgAZH0yENfM7Vs6eNX2WGBEgRgTWEwTzhVS3jxG", "g8kgjmKjxBnXEMPqwknl1bJHwkcgwyQELQmjPOyU");
    }
	
}




