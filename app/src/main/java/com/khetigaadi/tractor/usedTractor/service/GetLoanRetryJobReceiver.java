package com.khetigaadi.tractor.usedTractor.service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice.NotificationHelper;
public class GetLoanRetryJobReceiver extends BroadcastReceiver {
    public static final String ACTION_RETRY = "com.wave.ACTION_RETRY";
    public static final String ACTION_CLEAR = "com.wave.ACTION_CLEAR";
    NotificationHelper mNotificationHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationHelper = new NotificationHelper(context);
        int notificationId = intent.getIntExtra("notificationId", 0);
        switch (intent.getAction()) {
            case ACTION_RETRY -> {
                mNotificationHelper.cancelNotification(notificationId);
                WorkRequest worker = new OneTimeWorkRequest.Builder(GetLoanImagesService.class)
                        .setInputData(new Data.Builder()
                                .putString(GetLoanImagesService.ID, intent.getStringExtra(GetLoanImagesService.ID))
                                .putString(GetLoanImagesService.FRONT, intent.hasExtra(GetLoanImagesService.FRONT) && intent.getExtras() != null ? intent.getExtras().get(GetLoanImagesService.FRONT).toString() : null)
                                .putString(GetLoanImagesService.BACK, intent.hasExtra(GetLoanImagesService.BACK) && intent.getExtras() != null ? intent.getExtras().get(GetLoanImagesService.BACK).toString() : null)
                                .build())
                        .build();
                WorkManager
                        .getInstance(context)
                        .enqueue(worker);
            }
            case ACTION_CLEAR -> mNotificationHelper.cancelNotification(notificationId);
            default -> {
                mNotificationHelper.cancelNotification(notificationId);
            }
        }
    }
}
