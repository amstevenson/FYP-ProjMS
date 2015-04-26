package com.easymanage.activities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.easymanage.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * An activity that allows the user to search the external storage for files, to then upload
 * to the Parse datastore. Several methods were taken from Android; isExternalStorageReadable/writable.
 * 
 * @author Adam Stevenson
 *
 */
public class ChooseFileActivity extends ListActivity {

	private File file;
	private List<String> items = null;
	private String projectId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_file);
		
		items = new ArrayList<String>();   
		
		// Get the project Id
		Intent intent = getIntent();
		projectId = intent.getStringExtra("projectId");
		
		//
		// Check to see if external storage is available on device
		//
		Boolean isWritable = isExternalStorageWritable();
		Boolean isReadable = isExternalStorageReadable();
		
		if(isWritable || isReadable)
		{
			try{
			    //String root_sd = Environment.getExternalStorageDirectory().toString();
			    file = new File( Environment.getExternalStorageDirectory().getPath() ) ;       
			    File list[] = file.listFiles();
		
			    for( int i=0; i< list.length; i++)
			    {
			            items.add( list[i].getName() );
			    }
		
			    setListAdapter(new ArrayAdapter<String>(this,
			            android.R.layout.simple_list_item_1, items ));
			    
			}catch(NullPointerException e)
			{
				Log.d("Error: null pointer", "Null pointer encounted: " + e.toString());
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "There is no external storage available" +
					"for reading or writing.", Toast.LENGTH_LONG).show();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_file, menu);
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
    protected void onListItemClick(ListView l, View v, int position, long id){
		
		super.onListItemClick(l, v, position, id);

	    final File temp_file = new File( file, items.get( position ) );  

	    // The selected extension of the file (excluding directories)
	    String temp_file_path       = temp_file.getAbsolutePath();

	    if(temp_file_path.contains(".") && temp_file.isFile())
	    {

	    	//
			// If the user wishes to save the file, create a new database row
			//
		    new AlertDialog.Builder(v.getContext())
		     .setIcon(android.R.drawable.ic_dialog_alert)
		     .setTitle("Save this file")
		     .setMessage("Are you sure you want to save this file to your project space?")
		     .setPositiveButton("Save", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {

					// Progress Dialog
				    final ProgressDialog pDialog;
		            pDialog = new ProgressDialog(ChooseFileActivity.this);
		            pDialog.setMessage("Saving file...this may take awhile");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
	            	
					//
	            	// Create the file and Parse Object
	            	//
			        String extention = android.webkit.MimeTypeMap.getFileExtensionFromUrl(
			        		Uri.fromFile(temp_file).toString());
			        final String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extention);
		            final int size = (int) temp_file.length();
			        
		            byte[] bytes = new byte[size];
			        
		            try {
		            	
		            	// Get the byte information from the file
		            	BufferedInputStream fileBuffer = new BufferedInputStream(new FileInputStream(temp_file));
						fileBuffer.read(bytes, 0, bytes.length);
						fileBuffer.close();
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
					//
					// Create the ParseFile
					//
					final ParseFile parseFile = new ParseFile(temp_file.getName(), bytes);
					
					//
					// Upload the image into Parse Cloud
					//
					parseFile.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException e) {
							
							if(e == null)
							{
								//
								// Create a new Parse Object and add it to database
								//
								ParseObject newEvent = new ParseObject("File");
								newEvent.put("fileProjectId", projectId);
								newEvent.put("fileName", temp_file.getName());
								newEvent.put("fileExtention", mimetype);
								newEvent.put("fileSize", size);
								newEvent.put("file", parseFile);
								newEvent.saveInBackground();
							}
							else
							{
								Toast.makeText(getApplicationContext(),
										"Something went wrong: " + e.getMessage(),
										Toast.LENGTH_LONG).show();
							}
						}
					});
					
		            
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
					    @Override
					    public void run() {
					        
					    	//
					    	// Finish the updating process
					    	//
					    	if(pDialog.isShowing()) pDialog.dismiss();
					    	
					    	Toast toast = Toast.makeText(getApplicationContext(), "File: '"
									+ temp_file.getName() +  "' has been saved." , Toast.LENGTH_LONG);
							toast.show();
							
							finish();
					    }
					}, 2000);
	            }

		     })
		     .setNegativeButton("Cancel", null)
		     .show();
		
	    }
	    
	    //
	    // List the contents of the directory
	    //
	    else if( !temp_file.isFile()) // If it is not a file     
	    {
	        file = new File( file, items.get( position ));
	        File list[] = file.listFiles();

	        items.clear();

	        for( int i=0; i< list.length; i++)
	        {
	        	items.add( list[i].getName() );
	        }

	        // Set the adapter
	        setListAdapter(new ArrayAdapter<String>(this,
	                android.R.layout.simple_list_item_1, items ));
	    }
    }
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
}
