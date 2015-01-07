package com.projectinspire.fragments;

import com.projectinspire.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class UserContactsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View view = inflater.inflate(R.layout.fragment_user_contacts, container, false);
		
		return view;
	}

	public void setText(String item) {
		TextView view = (TextView) getView().findViewById(R.id.txtFragContactsTitle);
		view.setText(item);
	}
}