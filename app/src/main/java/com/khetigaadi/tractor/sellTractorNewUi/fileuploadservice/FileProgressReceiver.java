package com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.khetigaadi.R;

import java.util.Objects;
public class FileProgressReceiver extends BroadcastReceiver {
    private static final String TAG = "FileProgressReceiver";
    public static final String ACTION_CLEAR_NOTIFICATION = "com.wave.ACTION_CLEAR_NOTIFICATION";
    public static final String ACTION_PROGRESS_NOTIFICATION = "com.wave.ACTION_PROGRESS_NOTIFICATION";
    public static final String ACTION_UPLOADED = "com.wave.ACTION_UPLOADED";
    NotificationHelper mNotificationHelper;
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder notification;
    @Override
    public void onReceive(Context mContext,Intent intent) {
        mNotificationHelper = new NotificationHelper(mContext);
        // Get notification id
        int notificationId = intent.getIntExtra("notificationId", 1);
        // Receive progress
        int progress = intent.getIntExtra("progress", 0);
        switch (Objects.requireNonNull(intent.getAction())) {
            case ACTION_PROGRESS_NOTIFICATION -> {
                notification = mNotificationHelper.getNotification(mContext.getString(R.string.uploading), mContext.getString(R.string.in_progress), progress);
                mNotificationHelper.notify(NOTIFICATION_ID, notification);
            }
            case ACTION_CLEAR_NOTIFICATION, ACTION_UPLOADED ->
                    mNotificationHelper.cancelNotification(notificationId);
            default -> {
                mNotificationHelper.cancelNotification(notificationId);
            }
        }
    }
}
