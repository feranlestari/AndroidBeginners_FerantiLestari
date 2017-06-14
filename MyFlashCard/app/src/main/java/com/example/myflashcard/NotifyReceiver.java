package com.example.myflashcard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by SRIN on 6/13/2017.
 */
public class NotifyReceiver extends BroadcastReceiver {
    private static String ACTION = "com.example.myflashcard.notif";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION.equals(intent.getAction())) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_widget)
                    .setContentTitle(context.getString(R.string.notificationTitle))
                    .setContentText(context.getString(R.string.notificationBody))
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, builder.build());
        }
    }
}
