package com.easymanage.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.easymanage.activities.CreateOrEditTaskActivity;
import com.easymanage.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 
 * A custom built adapter to create, list and manipulate the
 * information received from an external call to Parse.
 * 
 * @author Adam Stevenson
 *
 */
public class ListAllTasksAdapter extends BaseAdapter {

	private Context							   context;
	private ArrayList<HashMap<String, String>> userTasks;
	private LayoutInflater 					   mInflater;
	
	//
    // JSON/key Node names that will be used for all of the task tags 
	//
	private static final String TAG_TASKNAME        = "taskName";
    private static final String TAG_TASKSTARTDATE   = "taskStartDate";
    private static final String TAG_TASKENDDATE     = "taskEndDate";
    private static final String TAG_TASKPRIORITY    = "taskPriority";
    private static final String TAG_TASKCATEGORY    = "taskCategory";
    private static final String TAG_TASKASSIGNEDTO  = "taskAssignedTo";
    private static final String TAG_TASKDESCRIPTION = "taskDescription";
    //private static final String TAG_TASKSTATUS      = "taskStatus";
    
    //
    // Default constructor
    //
    public ListAllTasksAdapter(Context context, ArrayList<HashMap<String, String>> userProjects){
		this.context 		= context;
		this.userTasks   = userProjects;
	}
    
	@Override
	public int getCount() {

		return userTasks.size();
	}

	@Override
	public Object getItem(int position) {

		return userTasks.get(position);
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
            convertView = mInflater.inflate(R.layout.adapter_all_tasks , parent, false);
        }
		
		TextView txtTaskName        = (TextView) convertView.findViewById(R.id.taskName);
		TextView txtTaskStartDate   = (TextView) convertView.findViewById(R.id.taskStartDate);
		TextView txtTaskEndDate     = (TextView) convertView.findViewById(R.id.taskEndDate);
		TextView txtTaskPriority    = (TextView) convertView.findViewById(R.id.taskPriority);
		TextView txtTaskCategory    = (TextView) convertView.findViewById(R.id.taskCategory);
		TextView txtTaskAssigned    = (TextView) convertView.findViewById(R.id.taskAssignedTo);
		TextView txtTaskDescription = (TextView) convertView.findViewById(R.id.taskDescription);
		TextView txtTaskStatus      = (TextView) convertView.findViewById(R.id.taskStatus);
		TextView imgEdit           = (TextView) convertView.findViewById(R.id.taskEdit);
		
		txtTaskName.setText(userTasks.get(position).get(TAG_TASKNAME).toString());
		txtTaskStartDate.setText("Started: " + userTasks.get(position).get(TAG_TASKSTARTDATE));
		txtTaskEndDate.setText("Ends: " + userTasks.get(position).get(TAG_TASKENDDATE));
		txtTaskPriority.setText(userTasks.get(position).get(TAG_TASKPRIORITY).toString());
		txtTaskCategory.setText("Category: " + userTasks.get(position).get(TAG_TASKCATEGORY).toString());
		txtTaskAssigned.setText("Assigned To: " + userTasks.get(position).get(TAG_TASKASSIGNEDTO).toString());
		txtTaskDescription.setText("Description: " + userTasks.get(position).get(TAG_TASKDESCRIPTION).toString());
		
		//
		// Set the status of the task
		//
		//String status = userTasks.get(position).get(TAG_TASKSTATUS).toString();
		try {
			
			long todaysDateLong  = Calendar.getInstance().getTime().getTime();
		
			SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
			Date endDate = sdf.parse(userTasks.get(position).get(TAG_TASKENDDATE));
		
			// Check to see if todays date is before or after the set end date for the task
			long endDateLong = endDate.getTime();
			
			Boolean isBefore;
			
			if(todaysDateLong < endDateLong) isBefore = true;
			else isBefore = false;
			
			if(isBefore) 
			{
				Drawable backgroundImage = parent.getResources().getDrawable(R.drawable.circle_green);
				txtTaskStatus.setBackground(backgroundImage);
			}
			else
			{
				Drawable backgroundImage = parent.getResources().getDrawable(R.drawable.circle_red);
				txtTaskStatus.setBackground(backgroundImage);
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
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditTaskActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("taskId", userTasks.get(innerPosition).get("taskId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		txtTaskDescription.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditTaskActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("taskId", userTasks.get(innerPosition).get("taskId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		txtTaskName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean editing = true; // set the editing tag to true
				
				Intent intent = new Intent(v.getContext(), CreateOrEditTaskActivity.class);
				intent.putExtra("editing", editing);
				intent.putExtra("taskId", userTasks.get(innerPosition).get("taskId"));
				intent.setFlags(268435456); // set new task flag - puts it on the back stack, why the long number I wonder...
				v.getContext().startActivity(intent);
				
			}
		});
		
		return convertView;
	}

}
