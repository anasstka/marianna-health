package com.example.mhapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import java.text.ParseException;
import java.util.Date;

public class Constants {
    public static final String APP_NAME = "Marianna Health";
    public static final String NOTIFICATIONS_CHANNEL_ID = "NOTIFICATIONS_CHANNEL";
    public static final int WATER_NOTIFICATION_ID = 1000;
    public static final int BREAKFAST_NOTIFICATION_ID = 1001;
    public static final int LUNCH_NOTIFICATION_ID = 1002;
    public static final int DINNER_NOTIFICATION_ID = 1003;
    public static final int SNACK_NOTIFICATION_ID = 1004;

    public static void CreateNotificationChannel(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(NOTIFICATIONS_CHANNEL_ID, NOTIFICATIONS_CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
    }
}
