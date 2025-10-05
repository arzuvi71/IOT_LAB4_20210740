package com.example.iot_lab4_20210740.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    
    private OnDateSelectedListener listener;
    
    public interface OnDateSelectedListener {
        void onDateSelected(int year, int month, int day);
    }
    
    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Usar fecha actual como fecha por defecto
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        

        DatePickerDialog dialog = new DatePickerDialog(requireActivity(), this, year, month, day);
        
        // Rnago de fecha mínima (1 año atrás) y máxima (1 año en el futuro)
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -1);
        dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.YEAR, 1);
        dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (listener != null) {
            listener.onDateSelected(year, month, day);
        }
    }
}
