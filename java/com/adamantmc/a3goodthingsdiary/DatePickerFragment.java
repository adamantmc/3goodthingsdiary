package com.adamantmc.a3goodthingsdiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by Adamantios on 30/4/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String date = DayEdit.getDate();
        String dateArr[] = date.split("-");
        int year = Integer.parseInt(dateArr[0]);
        int month = Integer.parseInt(dateArr[1]) - 1;
        int day = Integer.parseInt(dateArr[2]);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        System.out.println("Date set! " + year + " " + month + " " + day);
        EditText date = (EditText) getActivity().findViewById(R.id.day_edit);
        String month_str, day_str;

        month++; //date picker returns a value from 0-11 for month
        month_str = Integer.toString(month);
        day_str = Integer.toString(day);

        if(day < 10) day_str = "0"+day_str;
        if(month < 10) month_str = "0"+month_str;

        String date_str = year+"-"+month_str+"-"+day_str;
        DayEdit.setDate(date_str);

        date.setText(Common.dateToFormattedString(date_str));
    }

}
