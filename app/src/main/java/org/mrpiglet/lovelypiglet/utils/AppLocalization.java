package org.mrpiglet.lovelypiglet.utils;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.DisplayMetrics;

import org.mrpiglet.lovelypiglet.R;

import java.util.Locale;

public final class AppLocalization {

    public static void setLanguageFromPreferences(AppCompatActivity activity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String key = activity.getString(R.string.pref_lang_key);
        String defaultValue = activity.getString(R.string.pref_reminder_default_value);
        String language = sharedPreferences.getString(key, defaultValue);
        AppLocalization.setAppLocale(language, activity);
    }

    @SuppressWarnings("deprecation")  //!!!!!!!!
    private static void setAppLocale(String lang, AppCompatActivity activity) {
        Locale myLocale = null;
        if (lang.equals("sr") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //API 21+
            myLocale = new Locale.Builder().setLanguage("sr").setRegion("RS").setScript("Latn").build();
        } else {
            myLocale = new Locale(lang);
        }
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        activity.setTitle(activity.getString(R.string.app_name));
    }
}
