package com.AmiCity.Planner;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;


/*Class to solve Android Time picker bug: Thanks: @author davidcesarino@gmail.com*/
public class TimePickerDialogFragment extends DialogFragment {

    public static final String HOUR = "Hour";
    public static final String MINUTE = "Minute";

    private boolean isCancelled = false; //Added to handle cancel
    private TimePickerDialog.OnTimeSetListener mListener;

    //Added to handle parent listener
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (!isCancelled)
            {
                mListener.onTimeSet(view,hourOfDay,minute);
            }
        }
    };
    //
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mListener = (TimePickerDialog.OnTimeSetListener) activity;
    }

    @Override
    public void onDetach() {
        this.mListener = null;
        super.onDetach();
    }

    @TargetApi(11)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        int h = b.getInt(HOUR);
        int m = b.getInt(MINUTE);

        final TimePickerDialog picker = new TimePickerDialog(getActivity(), getConstructorListener(), h, m,DateFormat.is24HourFormat(getActivity()));

        //final TimePicker timePicker = new TimePicker(getBaseContext());
        if (hasJellyBeanAndAbove()) {
            picker.setButton(DialogInterface.BUTTON_POSITIVE,
                    getActivity().getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isCancelled = false; //Cancel flag, used in mTimeSetListener
                        }
                    });
            picker.setButton(DialogInterface.BUTTON_NEGATIVE,
                    getActivity().getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isCancelled = true; //Cancel flag, used in mTimeSetListener
                        }
                    });
        }
        return picker;
    }
    private boolean hasJellyBeanAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private TimePickerDialog.OnTimeSetListener getConstructorListener() {
        return hasJellyBeanAndAbove() ? mTimeSetListener : mListener; //instead of null, mTimeSetListener is returned.
    }
}
