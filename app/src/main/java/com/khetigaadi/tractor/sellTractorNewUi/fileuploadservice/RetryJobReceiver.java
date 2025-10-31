package com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
public class RetryJobReceiver extends BroadcastReceiver {
    public static final String ACTION_RETRY = "com.wave.ACTION_RETRY";
    public static final String ACTION_CLEAR = "com.wave.ACTION_CLEAR";
    NotificationHelper mNotificationHelper;
    @Override
    public void onReceive(Context context,Intent intent) {
        mNotificationHelper = new NotificationHelper(context);
        int notificationId = intent.getIntExtra("notificationId",0);
        switch (intent.getAction()) {
            case ACTION_RETRY -> {
                mNotificationHelper.cancelNotification(notificationId);
                WorkRequest worker = new OneTimeWorkRequest.Builder(FileUploadService.class)
                        .setInputData(new Data.Builder()
                                .putInt(FileUploadService.ID,intent.getIntExtra(FileUploadService.ID, 0))
                                .putString(FileUploadService.RIGHT,intent.getStringExtra(FileUploadService.RIGHT))
                                .putString(FileUploadService.FRONT_SIDE,intent.getStringExtra(FileUploadService.FRONT_SIDE))
                                .putString(FileUploadService.LEFT,intent.getStringExtra(FileUploadService.LEFT))
                                .putString(FileUploadService.BACK,intent.getStringExtra(FileUploadService.BACK))
                                .putString(FileUploadService.FRONT_TYRE,intent.getStringExtra(FileUploadService.FRONT_TYRE))
                                .putString(FileUploadService.BACK_TYRE,intent.getStringExtra(FileUploadService.BACK_TYRE))
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
