package com.easymanage.navslider.adapter;

import java.util.ArrayList;

import com.easymanage.navslider.model.NavigationDrawerItem;
import com.easymanage.navslider.model.NavigationDrawerProfileItem;
import com.easymanage.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Created using the android tutorial: 
 * https://developer.android.com/training/implementing-navigation/nav-drawer.html
 * 
 * @author Adam Stevenson
 *
 */
public class NavigationDrawerListAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<NavigationDrawerProfileItem> navDrawerProfileItem;
	private ArrayList<NavigationDrawerItem> navDrawerItems;
	private LayoutInflater mInflater;
	private int toggleBackgroundColour = 0;
	
	public NavigationDrawerListAdapter(Context context, ArrayList<NavigationDrawerProfileItem> navDrawerProfileItem,
			ArrayList<NavigationDrawerItem> navDrawerItems, String activityName){
		this.context 			  = context;
		this.navDrawerProfileItem = navDrawerProfileItem;
		this.navDrawerItems       = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
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
            convertView = mInflater.inflate(R.layout.navigation_slider_list_item , parent, false);
        }
         
		// Nav item
        ImageView navIcon   = (ImageView) convertView.findViewById(R.id.icon);
        TextView navTitle   = (TextView) convertView.findViewById(R.id.title);
        TextView navCounter = (TextView) convertView.findViewById(R.id.counter);
		
		// User information
		ImageView profileBackground = (ImageView) convertView.findViewById(R.id.profileBackground);
		ImageView profilePicture    = (ImageView) convertView.findViewById(R.id.profilePicture);
		TextView profileName        = (TextView) convertView.findViewById(R.id.profileName);
		TextView profileEmail       = (TextView) convertView.findViewById(R.id.profileEmail);
		
        // Set user profile - if one is submitted
        if(position == 0)
        {
        	profileBackground.setImageBitmap(navDrawerProfileItem.get(0).getProfileBackgroundImage());
	        profilePicture.setImageBitmap(navDrawerProfileItem.get(0).getProfileImage());
	        profileName.setText(navDrawerProfileItem.get(0).getProfileName());
	        profileEmail.setText(navDrawerProfileItem.get(0).getProfileEmail());
        }
        else
        {
        	if(toggleBackgroundColour == 0)
        	{
        		convertView.setBackgroundResource(R.drawable.drawer_background_shape_one);
        		
	        	profileBackground.setVisibility(View.GONE);
	        	profilePicture.setVisibility(View.GONE);
	        	profileName.setVisibility(View.GONE);
	        	profileEmail.setVisibility(View.GONE);
	        	
	            navIcon.setImageResource(navDrawerItems.get(position).getIcon());        
	            navTitle.setText(navDrawerItems.get(position).getTitle());
	            
	            if(navDrawerItems.get(position).getCounterVisibility()){
	            	navCounter.setText(navDrawerItems.get(position).getCount());
	            }
	            else
	            {
	            	// hide the counter view
	            	navCounter.setVisibility(View.GONE);
	            }
	            
	            toggleBackgroundColour += 1;
        	}
        	else
        	{
        		convertView.setBackgroundResource(R.drawable.drawer_background_shape_two);
        		
	        	profileBackground.setVisibility(View.GONE);
	        	profilePicture.setVisibility(View.GONE);
	        	profileName.setVisibility(View.GONE);
	        	profileEmail.setVisibility(View.GONE);

	            navIcon.setImageResource(navDrawerItems.get(position).getIcon());        
	            navTitle.setText(navDrawerItems.get(position).getTitle());
	            
	            if(navDrawerItems.get(position).getCounterVisibility()){
	            	navCounter.setText(navDrawerItems.get(position).getCount());
	            }
	            else
	            {
	            	// hide the counter view
	            	navCounter.setVisibility(View.GONE);
	            }
	            
	            toggleBackgroundColour -= 1;
	            
        	}
        	
        }
        return convertView;
	}
}
