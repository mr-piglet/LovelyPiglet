package org.mrpiglet.lovelypiglet.sync;

import android.content.Context;

import org.mrpiglet.lovelypiglet.utils.NotificationUtils;

/**
 * Created by mrpiglet on 7.12.17..
 */

public class CheckingTasks {
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_OPEN_APP = "open-app";

    public static void executeTask(Context context, String action) {
        if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            NotificationUtils.clearAllNotifications(context);
        } else if (ACTION_OPEN_APP.equals(action)) {
            //TODO:OPEN ADDCHECKEDITEMACTIVITY WHEN THIS ACTION IS CHOSEN
        }
    }
}
