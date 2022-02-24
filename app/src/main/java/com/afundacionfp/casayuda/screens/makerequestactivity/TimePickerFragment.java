package com.afundacionfp.casayuda.screens.makerequestactivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import com.afundacionfp.casayuda.R;

/**
 * Time selection screen fragment
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current time as the default values for the time picker.
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * Sets the Activity hour TextView with the selected data.
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void onTimeSet(TimePicker view, int i, int i1) {
        final TextView hour = getActivity().findViewById(R.id.current_time_text);
        hour.setText(String.format("%02d:%02d", view.getHour(), view.getMinute()));
    }
}

