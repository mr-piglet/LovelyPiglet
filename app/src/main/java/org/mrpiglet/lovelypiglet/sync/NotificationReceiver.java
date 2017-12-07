package org.mrpiglet.lovelypiglet.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.mrpiglet.lovelypiglet.utils.NotificationUtils;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationUtils.sendCheckNowNotification(context);
    }
}
