package org.mrpiglet.lovelypiglet.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import org.mrpiglet.lovelypiglet.R;

import java.util.Locale;

public final class AppLocalization {

    @SuppressWarnings("deprecation")  //!!!!!!!!
    public static void setAppLocale(String lang, AppCompatActivity activity) {
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        activity.setTitle(activity.getString(R.string.app_name));
        //Intent refresh = new Intent(this, MainActivity.class);
        //startActivity(refresh);
        //finish();
    }
}
