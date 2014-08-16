package com.rhsquashclub.arhscbook.view;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class RHSCDatePickerDialog extends DatePickerDialog {

	public RHSCDatePickerDialog(Context context, OnDateSetListener callBack,
			Calendar curDate, Calendar minDate, Calendar maxDate) {
		super(context, callBack, curDate.get(Calendar.YEAR), 
				curDate.get(Calendar.MONTH), curDate.get(Calendar.DAY_OF_MONTH));
		// TODO Auto-generated constructor stub
		this.getDatePicker().setMinDate(minDate.getTimeInMillis());
		this.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
		this.getDatePicker().setCalendarViewShown(false);
	}

	public RHSCDatePickerDialog(Context context, int theme,
			OnDateSetListener callBack, Calendar curDate, Calendar minDate, 
			Calendar maxDate) {
		super(context, theme, callBack, curDate.get(Calendar.YEAR), 
				curDate.get(Calendar.MONTH), curDate.get(Calendar.DAY_OF_MONTH));
		// TODO Auto-generated constructor stub
		this.getDatePicker().setCalendarViewShown(false);
	}

}
