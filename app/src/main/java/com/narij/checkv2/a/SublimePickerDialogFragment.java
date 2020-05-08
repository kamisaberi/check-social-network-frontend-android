package com.narij.checkv2.a;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appeaser.sublimepickerlibrary.SublimePicker;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.narij.checkv2.R;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SublimePickerDialogFragment extends DialogFragment {


//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //return super.onCreateView(inflater, container, savedInstanceState);
//
//        SublimePicker sublimePicker = new SublimePicker(getContext());
//        SublimeOptions sublimeOptions = new SublimeOptions();
//        sublimeOptions.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);// I want the recurrence picker to show.
//        sublimeOptions.setDisplayOptions(SublimeOptions.ACTIVATE_TIME_PICKER
//                | SublimeOptions.ACTIVATE_RECURRENCE_PICKER); // I only want the recurrence picker, not the date/time pickers.
//        sublimePicker.initializePicker(sublimeOptions, new SublimeListenerAdapter() {
//            @Override
//            public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker, SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
//
//            }
//
//            @Override
//            public void onCancelled() {
//
//            }
//        });
//        return sublimePicker;
//
//    }

    DateFormat mDateFormatter, mTimeFormatter;

    // Picker
    SublimePicker mSublimePicker;

    // Callback to activity
    Callback mCallback;

    SublimeListenerAdapter mListener = new SublimeListenerAdapter() {
        @Override
        public void onCancelled() {
            if (mCallback!= null) {
                mCallback.onCancelled();
            }

            // Should actually be called by activity inside `Callback.onCancelled()`
            dismiss();
        }

        @Override
        public void onDateTimeRecurrenceSet(SublimePicker sublimeMaterialPicker,
                                            SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            if (mCallback != null) {
                mCallback.onDateTimeRecurrenceSet(selectedDate,
                        hourOfDay, minute, recurrenceOption, recurrenceRule);
            }

            // Should actually be called by activity inside `Callback.onCancelled()`
            dismiss();
        }
// You can also override 'formatDate(Date)' & 'formatTime(Date)'
        // to supply custom formatters.
    };

    public SublimePickerDialogFragment() {
        // Initialize formatters
        mDateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        mTimeFormatter = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());
        mTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT+0"));
    }

    // Set activity callback
    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*try {
            //getActivity().getLayoutInflater()
                    //.inflate(R.layout.sublime_recurrence_picker, new FrameLayout(getActivity()), true);
            getActivity().getLayoutInflater()
                    .inflate(R.layout.sublime_date_picker, new FrameLayout(getActivity()), true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }*/

        mSublimePicker = (SublimePicker) getActivity()
                .getLayoutInflater().inflate(R.layout.sublime_picker, container);

        // Retrieve SublimeOptions
        Bundle arguments = getArguments();
        SublimeOptions options = null;

        // Options can be null, in which case, default
        // options are used.
        if (arguments != null) {
            options = arguments.getParcelable("SUBLIME_OPTIONS");

        }

        options.setCanPickDateRange(true);
        mSublimePicker.initializePicker(options, mListener);

        return mSublimePicker;
    }

    // For communicating with the activity
    public interface Callback {
        void onCancelled();

        void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                     int hourOfDay, int minute,
                                     SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                     String recurrenceRule);
    }
}
