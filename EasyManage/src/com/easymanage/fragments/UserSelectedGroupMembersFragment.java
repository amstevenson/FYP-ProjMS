package com.easymanage.fragments;

import com.easymanage.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * This activity lists and retrieves all of the members that have been
 * allocated to a group.
 * 
 * @author Adam Stevenson
 *
 */
public class UserSelectedGroupMembersFragment extends Fragment{

	private View view;
	//private String groupId = "";
	//private String userId  = "";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_user_selected_group_members, container, false);

		// retrieve the array containing the elements for the project
		//Bundle projectBundle      = getArguments();

		//
		// Get group and user Id
		//
		//groupId = projectBundle.getString("groupId");
		//userId  = projectBundle.getString("userId");
		
		

		
		return view;
	}
}
