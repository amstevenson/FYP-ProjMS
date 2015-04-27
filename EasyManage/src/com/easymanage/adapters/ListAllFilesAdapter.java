package com.easymanage.adapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.easymanage.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Adapter for creating a file in a local directory (/data/EasyManage) and then
 * opening it. THe file needs to be downloaded into the directory before it is opened
 * or else it will simply fail. There may be a way to open a byte array with a specific mime type
 * but Android specifies that the file should (and indeed is required to) be downloaded first.
 * 
 * @author Adam Stevenson
 *
 */
public class ListAllFilesAdapter extends BaseAdapter {

	private Context					      context;
	private LayoutInflater 				  mInflater;
	private ArrayList<HashMap<String, String>>    userFileInformation;
	private ArrayList<HashMap<String, ParseFile>> userParseFiles;
	
	//
    // JSON/key Node names that will be used for all of the project tags 
	//
	private static final String TAG_FILENAME      = "fileName";
    private static final String TAG_FILECREATEDAT = "fileCreatedAt";
    private static final String TAG_FILESIZE      = "fileSize";
    private static final String TAG_FILEEXTENSION = "fileExtension";
    private static final String TAG_FILE = "file";
    
    //
    // Default constructor
    //
    public ListAllFilesAdapter(Context context, ArrayList<HashMap<String, String>> userFileInformation,
    							ArrayList<HashMap<String, ParseFile>> userParseFiles){
		this.context 		     = context;
		this.userFileInformation = userFileInformation;
		this.userParseFiles      = userParseFiles;
	}
	
	@Override
	public int getCount() {
		
		return userFileInformation.size();
	}

	@Override
	public Object getItem(int position) {
		
		return userFileInformation.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adapter_all_files , parent, false);
        }
		
		TextView  txtFileName      = (TextView ) convertView.findViewById(R.id.filesName);
		TextView  txtFileCreatedAt = (TextView ) convertView.findViewById(R.id.filesStartDate);
		TextView  txtFileSize      = (TextView ) convertView.findViewById(R.id.filesSize);
		TextView  txtFileDelete    = (TextView)  convertView.findViewById(R.id.filesDelete);
		ImageView imgFileType      = (ImageView ) convertView.findViewById(R.id.filesImageType);

		txtFileName.setText(userFileInformation.get(position).get(TAG_FILENAME).toString());
		txtFileCreatedAt.setText("Created: " + userFileInformation.get(position).get(TAG_FILECREATEDAT));
		txtFileSize.setText("Size: " + userFileInformation.get(position).get(TAG_FILESIZE));
		
		// Get the mimetype - used to tell the device how to deal with the file
		final String mimeType = userFileInformation.get(position).get(TAG_FILEEXTENSION);
		
		// Get the file
		final ParseFile file = userParseFiles.get(position).get(TAG_FILE);

		// Get the nane
		final String name = userFileInformation.get(position).get(TAG_FILENAME);
		
		//
		// When anything but the button is pressed, load up the file
		//
		txtFileName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadFile(file, name, mimeType, v);
			}
		});
		
		txtFileCreatedAt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadFile(file, name, mimeType, v);
			}
		});
		
		txtFileSize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadFile(file, name, mimeType, v);
			}
		});
		
		imgFileType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				loadFile(file, name, mimeType, v);
			}
		});
		
		//
		// If the delete button is pressed, delete the file
		//
		txtFileDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		return convertView;
	}

	public void loadFile(ParseFile file, final String name, final String mimeType, final View v){
		
		//
		// Create the file
		//
		file.getDataInBackground(new GetDataCallback() {
			
			@Override
			public void done(byte[] data, ParseException e) {
				
				if(e == null)
				{
					 try 
					    { 
						 	// Create the new file
						 	File root = android.os.Environment.getExternalStorageDirectory(); 
						    File dir = new File (root.getAbsolutePath() + "/EasyManage");
						    dir.mkdirs();
						    File file = new File(dir, name);
						    
					        FileOutputStream stream = new FileOutputStream(file); 
					        stream.write(data); 
					        
							if (!file.exists()) {
							    file.createNewFile();
							}
							
							stream.close();
							
							// Open the file
							Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
					        
					        openFileIntent.setDataAndType(Uri.fromFile(file), mimeType);
					        try {
					             v.getContext().startActivity(openFileIntent);
					        } catch (ActivityNotFoundException e2) {

					        	Toast.makeText(v.getContext(), "Error: " + e2.getMessage(),
					        			Toast.LENGTH_LONG).show();
					        }
					        
					    } catch (FileNotFoundException e1) 
					    { 
					    	
					    	Toast.makeText(v.getContext(), "Error: " + e1.getMessage(),
				        			Toast.LENGTH_LONG).show();
					    	
					    } catch (IOException e1) {
							
					    	Toast.makeText(v.getContext(), "Error: " + e1.getMessage(),
				        			Toast.LENGTH_LONG).show();
						} 
				}
				else
				{
					Toast.makeText(v.getContext(), "" + e.getMessage(),
		        			Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
