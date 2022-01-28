package com.example.mhapp.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.mhapp.Constants;
import com.example.mhapp.R;
import com.example.mhapp.UserProfileData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotificationService extends Service {

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat formatForDay = new SimpleDateFormat("d");
    private SimpleDateFormat formatFullTime = new SimpleDateFormat("d HH:mm");
    private String stringifyNotifyTimeForWater;
    private String stringifyNotifyTimeForBreakfast;
    private String stringifyNotifyTimeForLunch;
    private String stringifyNotifyTimeForDinner;
    private String stringifyNotifyTimeForSnack;
    private boolean isNotifyAboutWaterEnabled;
    private boolean isNotifyAboutFoodEnabled;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DEBUG", "NOTIFICATION_SERVICE::ON_CREATE");
        stringifyNotifyTimeForWater = "";
        stringifyNotifyTimeForBreakfast = "";
        stringifyNotifyTimeForLunch = "";
        stringifyNotifyTimeForDinner = "";
        stringifyNotifyTimeForSnack = "";
        isNotifyAboutWaterEnabled = false;
        isNotifyAboutFoodEnabled = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle args = intent.getExtras();
            if (args != null) {
                UserProfileData userData = (UserProfileData) args.getSerializable("user_data");
                if (userData != null) {
                    int countOfMinutesForWater = Integer.parseInt(userData.getTimeOfWater());
                    String timeForBreakfast = userData.getTimeOfBreakfast();
                    String timeForLunch = userData.getTimeOfLunch();
                    String timeForDinner = userData.getTimeOfDinner();
                    String timeForSnack = userData.getTimeOfSnack();
                    isNotifyAboutWaterEnabled = userData.isInfoAboutWater();
                    isNotifyAboutFoodEnabled = userData.isInfoAboutFood();

                    // расчёт времени уведомления
                    Date currentTime = new Date();
                    Calendar calendar = Calendar.getInstance();

                    calendar.setTime(currentTime);
                    calendar.add(Calendar.MINUTE, countOfMinutesForWater);
                    stringifyNotifyTimeForWater = format.format(calendar.getTime());
                    stringifyNotifyTimeForBreakfast = String.format("%d %s", currentTime.getDate(), timeForBreakfast);
                    calendar.setTime(currentTime);
                    stringifyNotifyTimeForLunch = String.format("%d %s", currentTime.getDate(), timeForLunch);
                    calendar.setTime(currentTime);
                    stringifyNotifyTimeForDinner = String.format("%d %s", currentTime.getDate(), timeForLunch);
                    calendar.setTime(currentTime);
                    stringifyNotifyTimeForSnack = String.format("%d %s", currentTime.getDate(), timeForSnack);

                    startNotificationHandler(countOfMinutesForWater, timeForBreakfast, timeForLunch, timeForDinner, timeForSnack);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DEBUG", "NOTIFICATION_SERVICE::ON_DESTROY");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startNotificationHandler(int minutesForWater, String timeForBreakfast, String timeForLunch, String timeForDinner, String timeForSnack) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (
                            !stringifyNotifyTimeForWater.equals("") &&
                                    !stringifyNotifyTimeForBreakfast.equals("") &&
                                    !stringifyNotifyTimeForLunch.equals("") &&
                                    !stringifyNotifyTimeForDinner.equals("") &&
                                    !stringifyNotifyTimeForSnack.equals("")
                    ) {
                        String stringifyCurrentTime = format.format(new Date());
                        String stringifyFullCurrentTime = formatFullTime.format(new Date());

                        // уведоиление о воде
                        if (isNotifyAboutWaterEnabled) {
                            if (stringifyNotifyTimeForWater.equals(stringifyCurrentTime)) {
                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATIONS_CHANNEL_ID)
                                        .setSmallIcon(R.drawable.notification_bell)
                                        .setContentTitle(Constants.APP_NAME)
                                        .setContentText("Пора пить воду");
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Constants.CreateNotificationChannel(manager);
                                manager.notify(Constants.WATER_NOTIFICATION_ID, notificationBuilder.build());

                                // рассчёт следующего времени
                                Date currentTime = new Date();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(currentTime);
                                calendar.add(Calendar.MINUTE, minutesForWater);
                                stringifyNotifyTimeForWater = format.format(calendar.getTime());
                            }
                        }

                        if (isNotifyAboutFoodEnabled) {
                            // уведоиление о завтраке
                            if (stringifyNotifyTimeForBreakfast.equals(stringifyFullCurrentTime)) {
                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATIONS_CHANNEL_ID)
                                        .setSmallIcon(R.drawable.notification_bell)
                                        .setContentTitle(Constants.APP_NAME)
                                        .setContentText("Пора завтракать");
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Constants.CreateNotificationChannel(manager);
                                manager.notify(Constants.BREAKFAST_NOTIFICATION_ID, notificationBuilder.build());

                                // рассчёт следующего времени
                                Date currentTime = new Date();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(currentTime);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                stringifyNotifyTimeForBreakfast = String.format("%s %s", formatForDay.format(calendar.getTime()), timeForBreakfast);
                            }
                            // уведоиление об обеде
                            if (stringifyNotifyTimeForLunch.equals(stringifyFullCurrentTime)) {
                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATIONS_CHANNEL_ID)
                                        .setSmallIcon(R.drawable.notification_bell)
                                        .setContentTitle(Constants.APP_NAME)
                                        .setContentText("Пора обедать");
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Constants.CreateNotificationChannel(manager);
                                manager.notify(Constants.LUNCH_NOTIFICATION_ID, notificationBuilder.build());

                                // рассчёт следующего времени
                                Date currentTime = new Date();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(currentTime);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                stringifyNotifyTimeForLunch = String.format("%s %s", formatForDay.format(calendar.getTime()), timeForLunch);
                            }
                            // уведоиление об ужине
                            if (stringifyNotifyTimeForDinner.equals(stringifyFullCurrentTime)) {
                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATIONS_CHANNEL_ID)
                                        .setSmallIcon(R.drawable.notification_bell)
                                        .setContentTitle(Constants.APP_NAME)
                                        .setContentText("Пора ужинать");
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Constants.CreateNotificationChannel(manager);
                                manager.notify(Constants.DINNER_NOTIFICATION_ID, notificationBuilder.build());

                                // рассчёт следующего времени
                                Date currentTime = new Date();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(currentTime);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                stringifyNotifyTimeForDinner = String.format("%s %s", formatForDay.format(calendar.getTime()), timeForDinner);
                            }
                            // уведоиление о перекусе
                            if (stringifyNotifyTimeForSnack.equals(stringifyFullCurrentTime)) {
                                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constants.NOTIFICATIONS_CHANNEL_ID)
                                        .setSmallIcon(R.drawable.notification_bell)
                                        .setContentTitle(Constants.APP_NAME)
                                        .setContentText("Пора перекусить");
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Constants.CreateNotificationChannel(manager);
                                manager.notify(Constants.SNACK_NOTIFICATION_ID, notificationBuilder.build());

                                // рассчёт следующего времени
                                Date currentTime = new Date();
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(currentTime);
                                calendar.add(Calendar.DAY_OF_MONTH, 1);
                                stringifyNotifyTimeForSnack = String.format("%s %s", formatForDay.format(calendar.getTime()), timeForSnack);
                            }
                        }
                    } else {
                        stringifyNotifyTimeForWater = "";
                        stringifyNotifyTimeForBreakfast = "";
                        stringifyNotifyTimeForLunch = "";
                        stringifyNotifyTimeForDinner = "";
                        stringifyNotifyTimeForSnack = "";
                    }
                }
            }
        }).start();
    }
}
