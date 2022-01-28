package com.example.mhapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "NotesNotifyChannel")
                .setSmallIcon(R.drawable.ic_add_water)
                .setContentTitle("MariannaHealth")
                .setContentText("Пора пить воду!");
        NotificationManager managerCompat = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("NotesNotifyChannel", "NotesNotifyChannel", NotificationManager.IMPORTANCE_DEFAULT);
            managerCompat.createNotificationChannel(channel);
        }
        managerCompat.notify(1, builder.build());
    }
}

