package org.mrpiglet.lovelypiglet.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by mrpiglet on 7.12.17..
 */

public class CheckReminderIntentService extends IntentService {
    public CheckReminderIntentService() {
        super("WaterReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        CheckingTasks.executeTask(this, action);
    }

}
