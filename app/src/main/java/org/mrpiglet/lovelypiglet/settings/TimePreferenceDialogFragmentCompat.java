package org.mrpiglet.lovelypiglet.settings;

import android.content.Context;
import android.os.Build;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat
        implements DialogPreference.TargetFragment {
    TimePicker timePicker = null;

    @Override
    protected View onCreateDialogView(Context context) {
        timePicker = new TimePicker(context);
        return timePicker;
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        timePicker.setIs24HourView(true);
        TimePreference pref = (TimePreference) getPreference();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(pref.getHour());
            timePicker.setMinute(pref.getMinute());
        } else {
            timePicker.setCurrentHour(pref.getHour());
            timePicker.setCurrentMinute(pref.getMinute());
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            TimePreference pref = (TimePreference) getPreference();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pref.setHour(timePicker.getHour());
                pref.setMinute(timePicker.getMinute());
            } else {
                pref.setHour(timePicker.getCurrentHour());
                pref.setMinute(timePicker.getCurrentMinute());
            }

            pref.setHour( timePicker.getCurrentHour() );
            pref.setMinute( timePicker.getCurrentMinute() );

            String value = TimePreference.timeToString(pref.getHour(), pref.getMinute());
            if (pref.callChangeListener(value)) pref.persistStringValue(value);
        }
    }

    @Override
    public Preference findPreference(CharSequence charSequence) {
        return getPreference();
    }
}
