package com.easymanage.activities;

import java.util.ArrayList;

import com.easymanage.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class ChooseGroupMemberActivity extends ListActivity {

    private ArrayList<String> contactIds;
    private ArrayList<String> contactNames;
    private String			  groupId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_group_member);
		
		// Set the title
		setTitle("Add a member");
				
		//
		// Initiate HashMap
		//
		contactIds = new ArrayList<String>();
		contactNames = new ArrayList<String>();
				
		//
		// Get extras
		//
		Intent intent = getIntent();
		contactIds    = intent.getStringArrayListExtra("contactUserIds");
		contactNames  = intent.getStringArrayListExtra("contactNames");
		groupId 	  = intent.getStringExtra("groupId");
				
		Log.d("GroupID:" ,"Choose group members: Group Id = " + groupId);
				
		if(contactNames.size() > 0)
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, contactNames ));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_group_member, menu);
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
