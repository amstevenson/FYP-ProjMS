package com.easymanage.navslider.model;

/**
 * 
 * This class is used to create an object that is used to create and display
 * elements on the navigation drawer. 
 * 
 * @author Android 
 *
 */
public class NavigationDrawerItem {
	
	private String title;
	private String count = "0";
	private boolean isCounterVisible = false;
	private int icon;
	
	public NavigationDrawerItem(){}

	public NavigationDrawerItem(String title, int icon){
		this.title = title;
		this.icon = icon;
	}
	
	public NavigationDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
