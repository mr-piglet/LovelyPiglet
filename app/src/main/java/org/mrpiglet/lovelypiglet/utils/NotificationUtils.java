package org.mrpiglet.lovelypiglet.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import org.mrpiglet.lovelypiglet.MainActivity;
import org.mrpiglet.lovelypiglet.R;
import org.mrpiglet.lovelypiglet.sync.CheckReminderIntentService;
import org.mrpiglet.lovelypiglet.sync.CheckingTasks;

/**
 * Created by mrpiglet on 7.12.17..
 */

public class NotificationUtils {
    private static final int CHECK_REMINDER_NOTIFICATION_ID = 1212;
    private static final int CHECK_REMINDER_PENDING_INTENT_ID = 2424;

    //Oreo specific notification channel id
    private static final String CHECK_REMINDER_NOTIFICATION_CHANNEL_ID = "check_notification_channel";

    //id constants for actions that will be displayed on notification
    private static final int ACTION_CHECK_NOW_PENDING_INTENT_ID = 12;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 24;

    //method for clearing all notifications
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
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

        //constructor with (context,String) is available only in 26.0.1+ version of library
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHECK_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.check_now_notification_title))
                .setContentText(context.getString(R.string.check_now_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        context.getString(R.string.check_now_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                //.addAction(getCheckNowAction(context))
                //.addAction(getIgnoreCheckAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        notificationManager.notify(CHECK_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    /*private static NotificationCompat.Action getIgnoreCheckAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, CheckReminderIntentService.class);
        ignoreReminderIntent.setAction(CheckingTasks.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.mipmap.ic_launcher,
                context.getString(R.string.ignore_notification),
                ignoreReminderPendingIntent);
        return ignoreReminderAction;
    }*/

    /*private static NotificationCompat.Action getCheckNowAction(Context context) {
        Intent checkNowIntent = new Intent(context, CheckReminderIntentService.class);
        checkNowIntent.setAction(CheckingTasks.ACTION_OPEN_APP);
        PendingIntent checkNowPendingIntent = PendingIntent.getService(
                context,
                ACTION_CHECK_NOW_PENDING_INTENT_ID,
                checkNowIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Action checkNowAction = new NotificationCompat.Action(R.mipmap.ic_launcher,
                context.getString(R.string.check_now_notification),
                checkNowPendingIntent);
        return checkNowAction;
    }*/

    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                CHECK_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
