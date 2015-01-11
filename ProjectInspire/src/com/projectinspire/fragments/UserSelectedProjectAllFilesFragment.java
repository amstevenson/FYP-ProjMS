package com.projectinspire.fragments;

import com.projectinspire.R;
import com.projectinspire.activities.CreateOrEditTaskActivity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class UserSelectedProjectAllFilesFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final View view = inflater.inflate(R.layout.fragment_user_selected_project_all_files, container, false);

		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		ImageView imageViewCreateFile = (ImageView)view.findViewById(R.id.imageViewCreateFile);
					
		//*******************************************************************************************//
		//									Create view listeners									 //
		//*******************************************************************************************//
		//
		// If we are creating a new task
		//
		imageViewCreateFile.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
						
						
						
				Intent createTask = new Intent(view.getContext(), CreateOrEditTaskActivity.class);
				startActivity(createTask);
						
			}
		});
		    	
		//*******************************************************************************************//
		//									Create and assign the images							 //
		//*******************************************************************************************//
	    //
		// If we do not have a image stored for the user, create the default user image drawable
		//
	    Drawable plusImage = getResources().getDrawable(R.drawable.icon_add_48dp_white_circle);

		//
		// Assign the drawable to the ImageView
		//
		imageViewCreateFile.setImageDrawable(plusImage);
				
		return view;
	}

	public void setText(String item) {
		//TextView view = (TextView) getView().findViewById(R.id.txtProjectAllFiles);
		//view.setText(item);
	}
}
