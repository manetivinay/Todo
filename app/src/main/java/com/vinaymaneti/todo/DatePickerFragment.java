package com.vinaymaneti.todo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by vinaymaneti on 9/23/16.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private onDatePickerSelectionListener mOnDatePickerSelectionListener;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        //creating a new instance of DatPickerDialog and we are returning the above values
        return datePickerDialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String date = view.getDayOfMonth() + "/" + view.getMonth() + "/" + view.getYear();
        this.mOnDatePickerSelectionListener.onCompleteDateSelected(date);
    }


    public interface onDatePickerSelectionListener {
        void onCompleteDateSelected(String date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mOnDatePickerSelectionListener = (onDatePickerSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnDatePickerSelectionListener");
        }
    }
}
