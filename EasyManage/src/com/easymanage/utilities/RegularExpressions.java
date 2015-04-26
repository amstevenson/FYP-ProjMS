package com.easymanage.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressions {

	
	/**
	 *  A method to return whether a string contains numbers or symbols (allows spaces and '-' 's).
	 *  
	 *  @return True or false, dependent on whether the string meets
	 *  the regular expressions or not. 
	 *  
	 */
	public boolean sentenceOnlyLetters(String s){
		
		// Determine if numbers are used
		Pattern p = Pattern.compile("[a-zA-Z]+");
		Matcher m = p.matcher(s);
	    
		// if it does not match, there must by symbols
		// or numbers. 
		// Lets keep the "-" symbol however, and accept
		// a string with just that symbol, to pass through the check.
		if (!m.matches()) {
			
			// accept "-"'s - this may change. Delete if so.
			// Please note of course, that spaces are acceptable
			p = Pattern.compile("(([a-zA-Z]+[-| ])+[a-zA-Z]+)");
			m = p.matcher(s);
			
			// if we have found any "-" symbol(s), or not, permit.
			if(!m.matches()) return true;
			
			else return false;
		}
		else return false;
	}
	
	/***
	 * To determine if we have a password that contains both numbers and letters,
	 * the passed parameter is matched against a reular expression.
	 * 
	 * @param s is the string that is to be matched. 
	 * @return true or false, dependent on whether the password contains letters and numbers
	 * (true), or if it does not (false). 
	 */
	public boolean passwordNumsAndLetters(String s){
		
		//
		// Create the pattern and matcher, then check
		// Unfortunately, android patterns does not have a password method
		//
		Pattern pattern = Pattern.compile("[a-zA-Z]+[0-9]+"); // any amount of letters + numbers
		Matcher matcher = pattern.matcher(s);
	    
		if(matcher.matches()) return true; // if symbols and numbers
		
		else return false;
	}
	
	/**
	 * Email address validation. 
	 * Luckily android has a build in
	 * pattern
	 * 
	 * @return True or false, dependent on whether the string meets
	 * the regular expressions or not. 
	 */
	public boolean validateEmail(String s){
		
		if (s == null) return false;
	    else return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
	}
}
