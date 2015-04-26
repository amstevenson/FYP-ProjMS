package com.easymanage.navslider.model;

import android.graphics.Bitmap;

public class NavigationDrawerProfileItem {

	private Bitmap profileBackgroundImage;
	private Bitmap profileImage;
	private String profileName;
	private String profileEmail;
	
	private boolean isCounterVisible = false;
	
	public NavigationDrawerProfileItem(Bitmap profileBackgroundImage, Bitmap profileImage, String profileName, String profileEmail){
		this.profileBackgroundImage = profileBackgroundImage;
		this.profileImage = profileImage;
		this.profileName = profileName;
		this.profileEmail = profileEmail;
	}
	
	public Bitmap getProfileBackgroundImage(){
		return this.profileBackgroundImage;
	}
	
	public Bitmap getProfileImage() {
		return this.profileImage;
	}
	
	public String getProfileName(){
		return this.profileName;
	}
	
	public String getProfileEmail(){
		return this.profileEmail;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setProfileBackgroundImage(Bitmap profileBackgroundImage){
		this.profileBackgroundImage = profileBackgroundImage;
	}
	
	public void setProfileImage(Bitmap profileImage){
		this.profileImage = profileImage;
	}
	
	public void setProfileName(String profileName){
		this.profileName = profileName;
	}
	
	public void setProfileEmail(String profileEmail){
		this.profileEmail = profileEmail;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
	
	
}
