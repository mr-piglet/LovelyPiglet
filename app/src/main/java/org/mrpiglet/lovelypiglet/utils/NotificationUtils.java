package org.mrpiglet.lovelypiglet.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import org.mrpiglet.lovelypiglet.MainActivity;
import org.mrpiglet.lovelypiglet.R;
import org.mrpiglet.lovelypiglet.sync.NotificationReceiver;

import java.util.Calendar;


public class NotificationUtils {
    private static final int CHECK_REMINDER_NOTIFICATION_ID = 1212;
    //intent used by notification to start app
    private static final int CHECK_REMINDER_PENDING_INTENT_ID = 2424;
    //intent called by alarm manager
    private static final int SCHEDULER_REMINDER_PENDING_INTENT_ID = 3636;

    //Oreo specific notification channel id
    private static final String CHECK_REMINDER_NOTIFICATION_CHANNEL_ID = "check_notification_channel";

    //method for clearing all notifications
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void scheduleCheckNowNotification(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String time = sharedPreferences.getString(context.getString(R.string.pref_reminder_key), context.getString(R.string.pref_reminder_default_value));
        int hour;
        int minute;
        try {
            String[] timeValues = time.split(":");
            hour = Integer.parseInt(timeValues[0]);
            minute = Integer.parseInt(timeValues[1]);
            Log.v("Alarm set", "Time : " + time);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0); //exact minute

            Intent intent = new Intent(context.getApplicationContext(), NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                            context.getApplicationContext(),
                                            SCHEDULER_REMINDER_PENDING_INTENT_ID,
                                            intent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, //trigger even if device is in sleep mode
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        } catch (Exception e) {
            Log.v("String parse failed", "String preference time not in hh:mm format");
        }
    }

    //show "Check Now" notification
    public static void sendCheckNowNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Oreo uses notification channels !!!
            NotificationChannel mChannel = new NotificationChannel(
                    CHECK_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //constructor with (context,String) is available only in 26.0.1+ version of library
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHECK_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(largeIcon(context)) //svg vectors cause exception to be thrown
                .setContentTitle(context.getString(R.string.check_now_notification_title))
                .setContentText(context.getString(R.string.check_now_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.check_now_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(alarmSound)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        notificationManager.notify(CHECK_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//this flag replaces current activity, if shown
        return PendingIntent.getActivity(
                context,
                CHECK_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
