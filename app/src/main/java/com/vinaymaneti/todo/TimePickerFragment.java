package com.vinaymaneti.todo;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Created by vinaymaneti on 9/23/16.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private onTimePickerSelectionListener mOnTimePickerSelectionListener;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current time as the default value for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minutes = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minutes, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//        String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        this.mOnTimePickerSelectionListener.onCompleteTimeSelected(String.valueOf(hourOfDay), String.valueOf(minute));
    }

    public interface onTimePickerSelectionListener {
        void onCompleteTimeSelected(String hours, String minutes);
    }

    @Override

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mOnTimePickerSelectionListener = (onTimePickerSelectionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement mOnTimePickerSelectionListener");
        }

    }
}
