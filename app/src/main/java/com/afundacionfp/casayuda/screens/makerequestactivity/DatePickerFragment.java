package com.afundacionfp.casayuda.screens.makerequestactivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.afundacionfp.casayuda.R;

import java.util.Calendar;

/**
 * Date selection screen fragment
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    @NonNull
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

    /**
     * Sets the Activity date TextView with the selected data.
     */
    @SuppressLint("DefaultLocale")
    public void onDateSet(DatePicker view, int year, int month, int day) {
        final TextView date = getActivity().findViewById(R.id.calendarRequestView);
        // API states months on DatePicker starts with 0, so we add a +1 to reflect correct date.
        date.setText(String.format("%04d-%02d-%02d", view.getYear(), view.getMonth()+1,
                view.getDayOfMonth())) ;
    }
}

