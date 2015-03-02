package com.projectinspire.activities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.projectinspire.R;
import com.projectinspire.utilities.Operations;
import com.projectinspire.utilities.RegularExpressions;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * @author Adam Stevenson
 */
public class RegisterAccountActivity extends Activity {

	//
	// Global variables for toast
	//
	private Context context;
	private String  text;
	private int     duration;  
	
	//
	// Variables for user profile image
	//
	private String  userImageFileName = "default.png"; // default if none is selected
	private ImageView imageUserProfile;
	private ByteArrayOutputStream out;
	
	//
	// Integer to define the type of activity result that will be
	// collected by the Intent that will allow a user to select an image
	// from a gallery. 
	//
	private static int RESULT_LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_account);

		ActionBar actionBar = getActionBar();
		
		actionBar.setTitle("Register Account");
		 
		//
		// define toast variables
		//
		context  = getApplicationContext();
		text     = "defined, but not used";
		duration = Toast.LENGTH_LONG;
		
		//
		// Define views associated with the process of creating an account
		//
		final EditText registerForename       = (EditText) findViewById(R.id.editRegisterForename);
		final EditText registerSurname        = (EditText) findViewById(R.id.editRegisterSurname);
		final EditText registerPassword       = (EditText) findViewById(R.id.editRegisterPassword);
		final EditText registerRepeatPassword = (EditText) findViewById(R.id.editRegisterRepeatPassword);
		final EditText registerEmail          = (EditText) findViewById(R.id.editRegisterEmail);
		final EditText registerRepeatEmail    = (EditText) findViewById(R.id.editRegisterRepeatEmail);
 		
		Button submitRegistration = (Button) findViewById(R.id.btnRegisterSubmit);
		Button submitChangeImage  = (Button) findViewById(R.id.btnRegisterChangeUserImage);
		
		//****************************************************************************//
		//							Save user image  			      				  //
		//****************************************************************************//
		//
		// If the user wants to use a non default image, allow them to choose their own
		// from a gallery - perhaps other methods later on
		//
		submitChangeImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
				
			}
		});
		
		//****************************************************************************//
		//							Create account button pressed   			      //
		//****************************************************************************//
		//
		// When clicked, try to create an account
		//
		submitRegistration.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//********************************************************************//
				//							Variables   			      			  //
				//********************************************************************//
				//
				// Views
				//
				final Editable forename = registerForename.getText();
				final Editable surname  = registerSurname.getText();
				final Editable password = registerPassword.getText();
				Editable repeatPassword = registerRepeatPassword.getText();
				final Editable email = registerEmail.getText();
				Editable repeatEmail = registerRepeatEmail.getText();
				
				//
				// Booleans
				//
				boolean passwordValidation = false;
				boolean emailValidation    = false;
								
				//********************************************************************//
				//							Create account   			      		  //
				//********************************************************************//
				//
				// Check parameters before creating account
				//
				RegularExpressions regularExpressions = new RegularExpressions();
				
				//
				// Check if password contains at least one number and letter
				// 
				if(regularExpressions.passwordNumsAndLetters(password.toString()) && 
						regularExpressions.passwordNumsAndLetters(repeatPassword.toString()))
					
					//
					// Check if both the passwords are identical
					//
					if(password.toString().equals(repeatPassword.toString()))
						//
						// Check length
						//
						if(password.length() >= 5) 
							passwordValidation = true;
						else
						{
							passwordValidation = false;
							
							text = "The chosen password needs to be atleast five characters long, please revise and try again";
							
							// Inform the user that they have to revise their password
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
					else
					{
						passwordValidation = false;
						
						text = "Oops! The passwords do not match, please make sure that they are identical and try again";
						
						// Inform the user that they have to revise their password
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
				else 
				{
					passwordValidation = false;
					
					text = "Both password fields need to have atleast one number and one letter, please revise and try again";
					
					// Inform the user that they have to revise their password
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				
				//
				// Email check
				//
				if(passwordValidation) // step 2
				{
					if(regularExpressions.validateEmail(email.toString()) &&
							regularExpressions.validateEmail(repeatEmail.toString()))
						
						if(email.toString().equals(repeatEmail.toString()))
							emailValidation = true;
						else
						{
							emailValidation = false;
							
							text = "Both of the email addresses need to be identical, please revise and try again";
							
							// Inform the user that they have to revise their email
							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
						}
					else
					{
						emailValidation = false;
						
						text = "Oops! Both of the emails need to be in the correct format (perhaps there was no @ symbol?)";
						
						// Inform the user that they have to revise their email
						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
					}
					
					//
					// Create the user - if both validation steps are true
					//
					if(emailValidation) // step 3
					{
						final ParseFile file;
						
						//
						// Create the file for the user profile image
						//
						
						// if no profile image is selected
						if(userImageFileName.equals("default.png"))
						{
							Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
									R.drawable.icon_person);
							
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							
							bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
							
							byte[] image = stream.toByteArray();
			 
							// Create the ParseFile
							file = new ParseFile(userImageFileName, image);
							
						}
						// if the profile image is not default
						else
						{
							byte[] image = out.toByteArray();
							
							//
							// Create the ParseFile
							//
							file = new ParseFile(userImageFileName, image);
							
						}
						
						// Progress Dialog
					    final ProgressDialog pDialog;
			            pDialog = new ProgressDialog(RegisterAccountActivity.this);
			            pDialog.setMessage("We are registering your account, you will be logged in automatically once this is completed.");
			            pDialog.setIndeterminate(false);
			            pDialog.setCancelable(true);
			            pDialog.show();
						
						//
						// Upload the image into Parse Cloud
						//
						file.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								
								//
								// once done, we can create and save the Parse user profile object
								//
								if(e == null){
									
									//
									// Insert new row into the relevant Parse database class
									// It is important to get the .toString for each Editable.
									//
									// In addition to this, we need to use the email for the user name and email address
									// this is because the user name is the login identifier, and the email address
									// is used by parse to send email's to users - the problem of using an email as a logging in
									// variable was identified a year ago by Parse users, and still has not been addressed. 
									//
									//
									// Starting with EditText data...
									//
								    ParseUser registerUser = new ParseUser();
								    registerUser.setUsername(email.toString());
								    registerUser.put("forename", forename.toString());
								    registerUser.put("surname", surname.toString());  
								    registerUser.setPassword(password.toString());
								    registerUser.setEmail(email.toString());
								    
								    //
								    // and then Image data...
								    //
									registerUser.put("userImageName", userImageFileName);
									registerUser.put("userImageFile", file);
								    
								    // Check if it was successful or not
								    registerUser.signUpInBackground(new SignUpCallback() {
										public void done(ParseException e) {
											if (e == null) {
												
												//
												// Log in using Parse Loginbackground
												//
												// Send data to Parse.com for verification
												ParseUser.logInInBackground(email.toString(), password.toString(),
														new LogInCallback() {
															public void done(ParseUser user, ParseException e) {
																if (user != null) { // if we have a user
																	
																	//
																	// If the user is stored in the database
																	//
																	Intent intent = new Intent(
																			getApplicationContext(),
																			UserDashboardActivity.class);
																	
																	intent.putExtra("userId", user.getObjectId()); // add the email for queries
																	intent.putExtra("justRegistered", true);
																	
																	if(pDialog.isShowing()) pDialog.dismiss();
																	
																	startActivity(intent);
																	finish();
																	
																} else {
																}
															}
												}); // end of LogInBackground
												
											} else {
												
												if(pDialog.isShowing()) pDialog.dismiss();
												
												Toast.makeText(context,
														"Error saving object :" + e.getMessage(), duration)
														.show();
											}
										}
									});
								}
								else
								{
									if(pDialog.isShowing()) pDialog.dismiss();
									
									Toast.makeText(context,
											"Error saving image file :" + e.getMessage(), duration)
											.show();
								}
								
								
							}
						});
						
						
					}
				} 
			} 
		});
	}
	
	/***
	 * 
	 * This method determines the file path for an image that gets selected
	 * From a gallery. This then sets a preview ImageView that will preview
	 * the users image, that will be shown every time they login, in the 
	 * dash board. 
	 * 
	 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
           
        	//
        	// Get the resource for the picture
        	//
        	Uri selectedImage = data.getData();
            
			//
			// Create an instance of a class, where the bitmap/drawable methods
			// are kept/can be used from. 
			//
			Operations operations = new Operations();
        	
			Cursor returnCursor =
					getContentResolver().query(selectedImage, null, null, null, null);
			    
			/*
			 * Get the column indexes of the data in the Cursor,
			 * move to the first row in the Cursor, get the data,
			 * and display it.
			 */
			int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
			int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
			returnCursor.moveToFirst();
			
            //
            // Save the file name to be used when creating the user profile image
            // in the database
            //
            userImageFileName = returnCursor.getString(nameIndex);
            
            if(Integer.parseInt(returnCursor.getString(sizeIndex)) < 100000)
            {
				try {
					
					//
					// Resize the image - which is useful for database purposes, as we do not want it
					// to be too big. In addition to this, it will make the compression go faster. 
					//
					Bitmap original = operations.getResizedBitmap(
							MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage)
							, 120, 120);
						
					out = new ByteArrayOutputStream();
						
					//
					// Compress the 'original' bitmap image and assign the new 'decoded'(compressed) bitmap to the ImageView
					//
					if(original.compress(Bitmap.CompressFormat.PNG, 100, out))
					{
						out.close(); // close output stream
						
						Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
							
				        imageUserProfile = (ImageView) findViewById(R.id.imgviewUserImage);
				        imageUserProfile.setImageBitmap(decoded);
				            	
				        //
				        // If successful
				        // 
				        Toast.makeText(context,
								"Profile Image has been changed", duration)
								.show();
							
					}
					else
					{
						Toast.makeText(context,
								"Compression of image has failed.", duration)
								.show();
					}
			
					
				} catch (IOException e) {
					
					Toast.makeText(context,
							"IO error detected.", duration)
							.show();
					
					e.printStackTrace();
				}
            }
            else
            {
				Toast.makeText(context,
						"Please select a smaller file, the current one is too large.", duration)
						.show();
            }
        }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_account, menu);
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
}
