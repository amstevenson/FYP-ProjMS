package com.projectinspire.application;
import android.app.Application;
import com.parse.Parse;

/***
 * 
 * This class is used to initialise Parse.
 * If this is not done here, it would need to be done on each activity/fragment. 
 * 
 * @author Adam Stevenson
 *
 */
public class App extends Application {


    @Override public void onCreate() { 
        super.onCreate();

		// Enable Local Datastore.
		Parse.enableLocalDatastore(this);		
        
        // Define project parameters for API
        Parse.initialize(this, "ZNgAZH0yENfM7Vs6eNX2WGBEgRgTWEwTzhVS3jxG", "g8kgjmKjxBnXEMPqwknl1bJHwkcgwyQELQmjPOyU");
    }
	
}
