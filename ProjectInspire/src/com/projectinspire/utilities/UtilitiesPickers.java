package com.projectinspire.utilities;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

public class UtilitiesPickers extends Activity{

	static EditText passedEditText; // the view where the date will be shown
	
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
			// Set the date.
			// The view needs to belong to the context that invoked the date picker
			// Or in other words, the context/view that created this dialog. 
			// (Hence the static global declaration of the EditText view).
			//
			passedEditText.setText("" + day + "/" + "" + month + "/" + year);
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
			passedEditText.setText("" + hourOfDay + ":" + minute);
		}

	}
}
