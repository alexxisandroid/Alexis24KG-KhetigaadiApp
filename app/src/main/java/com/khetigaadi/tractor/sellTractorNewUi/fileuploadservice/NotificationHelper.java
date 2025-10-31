package com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.khetigaadi.R;
public class NotificationHelper extends ContextWrapper {
    private NotificationManager manager;
    public static final String WAVE_CHANNEL = "default";
    public NotificationHelper(Context mContext) {
        super(mContext);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(WAVE_CHANNEL,getString(R.string.app_name),NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setLightColor(Color.GREEN);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(mChannel);
        }
    }
    public NotificationCompat.Builder getNotification(String title, String body, int progress) {
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), WAVE_CHANNEL);
        mBuilder.setSmallIcon(getSmallIcon());
        mBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        mBuilder.setContentTitle(title)
                .setContentText(body)
                .setOngoing(true)
                //.setContentIntent(resultPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setVibrate(new long[]{0L});
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mBuilder.setProgress(100, progress, false);
        if (progress == 100) {
            mBuilder.setProgress(0, 0, false);
            mBuilder.setContentText(body);
        }
        return mBuilder;
    }
    public NotificationCompat.Builder getNotification(String title, String body, PendingIntent resultPendingIntent) {
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), WAVE_CHANNEL);
        mBuilder.setSmallIcon(getSmallIcon());
        mBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
        mBuilder.setContentTitle(title)
                .setContentText(body)
                .setContentIntent(resultPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setVibrate(new long[]{0L});
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        return mBuilder;
    }
    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }
    private int getSmallIcon() {
        return android.R.drawable.stat_notify_sync;
    }
    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
    public void cancelNotification(int notificationId) {
        getManager().cancel(notificationId);
    }
}
