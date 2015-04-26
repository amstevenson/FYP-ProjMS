package com.easymanage.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class UtilitiesPickers extends Activity{

	private static EditText passedEditText;
	private static int count = 0;			
									
	
	//*******************************************************************************************//
	//									DATE PICKER												 //
	//*******************************************************************************************//
	public void showDatePickerDialog(Activity activity, EditText toEdit) {
	    
		//
		// Set the view that will be edited. 
		//
		passedEditText = toEdit;
		
		//
		// Get the activity from the passed parameter, create a fragment manager
		// and show the date picker for the activity where this method was invoked. 
		//
		DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(activity.getFragmentManager(), "Please choose a date");
	}

	public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
		
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
			
		public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
			
			month += 1; // So we have an accurate month date...as 0 is not a month (as far as I know).

			//
			// Check chosen date to make sure it isnt before todays date
			//
			SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
			String dateInString = "" + day + "/" + "" + month + "/" + year;

			Date checkedDate;
			boolean validDate = false;
			
			try {
				checkedDate = sdf.parse(dateInString);

				long todaysDate  = Calendar.getInstance().getTime().getTime();
				long toCheckDate = checkedDate.getTime();
				
				// checking - debug purposes
				Log.d("Todays Date(ms)",      "" + todaysDate);
				Log.d("The checked date(ms)", "" + toCheckDate);
				
				if(toCheckDate >= todaysDate) validDate = true;
				else validDate = false;
			
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			if(validDate == true)
				//
				// If everything is fine, then set the date.
				//
				// The view needs to belong to the context that invoked the date picker
				// Or in other words, the context/view that created this dialog. 
				// (Hence the static global declaration of the EditText view).
				//
				passedEditText.setText("" + day + "/" + "" + month + "/" + year);
			else
			{
				// to only make it appear once...the focus and click event of the
				// object that invokes this class, each have a seperate instance of this
				if(count == 0)
				{
					Toast toast = Toast.makeText(getActivity(), 
							"Invalid date: Start/end date seems to be before today.", Toast.LENGTH_LONG);
					toast.show();
					
					passedEditText.setText("");
				}
			}
			
			
		}

	}
	
	//*******************************************************************************************//
	//									TIME PICKER												 //
	//*******************************************************************************************//
	public void showTimePickerDialog(Activity activity, EditText toEdit) {
		//
		// Set the view that will be edited. 
		//
		passedEditText = toEdit;
		
		//
		// Get the activity from the passed parameter, create a fragment manager
		// and show the date picker for the activity where this method was invoked. 
		//
		DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(activity.getFragmentManager(), "Please choose a time");
	}
	
	public static class TimePickerFragment extends DialogFragment
		implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			
			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
			DateFormat.is24HourFormat(getActivity()));
		}
		
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		
			//
			// Set the time.
			// The view needs to belong to the context that invoked the date picker
			// Or in other words, the context/view that created this dialog. 
			// (Hence the static global declaration of the EditText view).
			//
			if(minute > 9)
				passedEditText.setText("" + hourOfDay + ":" + minute);
			else
				passedEditText.setText("" + hourOfDay + ":" + "0" + minute);
		}

	}
	
	/*
	 * 
	 * @return true if the first date is less than the second.
	 * @return false if the first date proceeds the second 
	 */
	public boolean compareDateTimes (String firstDate, String secondDate) throws Exception
	{
		boolean firstBeforeSecond = false;
		
		//
		// Get dates
		// 
		// First string + start Date
		SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
		
		Date startDate = sdf.parse(firstDate);
		
		//
		// Second string + end date
		//
		Date endDate = sdf.parse(secondDate);
		
		//
		// Compare the two, and determine if the first is before the second
		//
		long longOfStartDate = startDate.getTime();
 		long longOfEndDate   = endDate.getTime();
		
 		if(longOfStartDate < longOfEndDate) firstBeforeSecond = true;
 		else 								firstBeforeSecond = false;
 		
		return firstBeforeSecond;
	}

}
