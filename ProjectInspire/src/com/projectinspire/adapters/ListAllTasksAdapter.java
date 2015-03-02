package com.projectinspire.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import com.projectinspire.R;
import com.projectinspire.activities.CreateOrEditTaskActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    private static final String TAG_TASKSTATUS      = "taskStatus";
    
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
		ImageView imgEdit           = (ImageView) convertView.findViewById(R.id.taskEdit);
		
		txtTaskName.setText(userTasks.get(position).get(TAG_TASKNAME).toString());
		txtTaskStartDate.setText("Started: " + userTasks.get(position).get(TAG_TASKSTARTDATE));
		txtTaskEndDate.setText("Ends: " + userTasks.get(position).get(TAG_TASKENDDATE));
		txtTaskPriority.setText(userTasks.get(position).get(TAG_TASKPRIORITY).toString());
		txtTaskCategory.setText("Category: " + userTasks.get(position).get(TAG_TASKCATEGORY).toString());
		txtTaskAssigned.setText("Assigned To: " + userTasks.get(position).get(TAG_TASKASSIGNEDTO).toString());
		txtTaskDescription.setText("Description: " + userTasks.get(position).get(TAG_TASKDESCRIPTION).toString());
		txtTaskStatus.setText("Status: " + userTasks.get(position).get(TAG_TASKSTATUS).toString());

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
