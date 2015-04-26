package com.easymanage.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.easymanage.activities.CreateOrEditProjectActivity;
import com.easymanage.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAllProjectsAdapter extends BaseAdapter {

	private Context							   context;
	private ArrayList<HashMap<String, String>> userProjects;
	private LayoutInflater 					   mInflater;
	
	//
    // JSON/key Node names that will be used for all of the project tags 
	//
	private static final String TAG_PROJECTNAME        = "projectName";
    private static final String TAG_PROJECTSTARTDATE   = "projectStartDate";
    private static final String TAG_PROJECTENDDATE     = "projectEndDate";
    private static final String TAG_PROJECTDESCRIPTION = "projectDescription";
	
    //
    // Default constructor
    //
    public ListAllProjectsAdapter(Context context, ArrayList<HashMap<String, String>> userProjects){
		this.context 		= context;
		this.userProjects   = userProjects;
    }
    
	@Override
	public int getCount() {

		return userProjects.size();
	}

	@Override
	public Object getItem(int position) {

		return userProjects.get(position);
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
            convertView = mInflater.inflate(R.layout.adapter_all_projects , parent, false);
        }
		
		TextView txtProjectName        = (TextView ) convertView.findViewById(R.id.projectName);
		TextView txtProjectStartDate   = (TextView ) convertView.findViewById(R.id.projectStartDate);
		TextView txtProjectEndDate     = (TextView ) convertView.findViewById(R.id.projectEndDate);
		TextView txtProjectStatus      = (TextView ) convertView.findViewById(R.id.projectStatus);
		TextView txtProjectDescription = (TextView ) convertView.findViewById(R.id.projectDescription);
		TextView imgEdit			   = (TextView) convertView.findViewById(R.id.projectEdit);
		
		txtProjectName.setText(userProjects.get(position).get(TAG_PROJECTNAME).toString());
		txtProjectStartDate.setText("Started: " + userProjects.get(position).get(TAG_PROJECTSTARTDATE));
		txtProjectEndDate.setText("Ends: " + userProjects.get(position).get(TAG_PROJECTENDDATE));
		//txtProjectStatus.setText(userProjects.get(position).get(TAG_PROJECTSTATUS).toString());
		txtProjectDescription.setText("Description: " + userProjects.get(position).get(TAG_PROJECTDESCRIPTION).toString());

		
		try {
			
			long todaysDateLong  = Calendar.getInstance().getTime().getTime();
		
			SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
			Date endDate = sdf.parse(userProjects.get(position).get(TAG_PROJECTENDDATE));
		
			// Check to see if todays date is before or after the set end date for the task
			long endDateLong = endDate.getTime();
			
			Boolean isBefore;
			
			if(todaysDateLong < endDateLong) isBefore = true;
			else isBefore = false;
			
			if(isBefore) 
			{
				Drawable backgroundImage = parent.getResources().getDrawable(R.drawable.circle_green);
				txtProjectStatus.setBackground(backgroundImage);
			}
			else
			{
				Drawable backgroundImage = parent.getResources().getDrawable(R.drawable.circle_red);
				txtProjectStatus.setBackground(backgroundImage);
			}
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		final int innerPosition = position;
		
		//
		// If the edit button is clicked, send to edit page for project
		//
		imgEdit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sendToProjectActivity(v, innerPosition);
			}
		});
		
		txtProjectStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sendToProjectActivity(v, innerPosition);
			}
		});
		
		txtProjectEndDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sendToProjectActivity(v, innerPosition);
			}
		});
		
		return convertView;
	}
	
	public void sendToProjectActivity(View v, int innerPosition)
	{
		boolean editing = true; // set the editing tag to true
		
		Intent intent = new Intent(v.getContext(), CreateOrEditProjectActivity.class);
		intent.putExtra("editing", editing);
		intent.putExtra("projectId", userProjects.get(innerPosition).get("projectId"));
		intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
		v.getContext().startActivity(intent);
	}

}
