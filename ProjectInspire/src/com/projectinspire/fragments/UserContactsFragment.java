package com.projectinspire.fragments;

import com.projectinspire.R;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UserContactsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_user_contacts, container, false);
		
		//*******************************************************************************************//
		//									View variables											 //
		//*******************************************************************************************//
		ImageView imageViewCreateContact = (ImageView)view.findViewById(R.id.imageViewCreateContact);
		
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
		imageViewCreateContact.setImageDrawable(plusImage);
		
		return view;
	}

	public void setText(String item) {
		//TextView view = (TextView) getView().findViewById(R.id.txtFragContactsTitle);
		//view.setText(item);
	}
}
