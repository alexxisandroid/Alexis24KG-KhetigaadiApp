package com.khetigaadi.notification;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.khetigaadi.FcmApi;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.SplashActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.notification.model.Notification;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.retrofit.UserAPIs;
import com.khetigaadi.util.Utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Inject
    SessionManager sessionManager;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    UserAPIs userAPIs;
    private static int counter;
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Timber.e("onNewToken: %s", token);
        if (sessionManager.isLoggedIn()) {
            FcmApi.registerFCMToken(userAPIs, sessionManager, () -> {
            });
        }
        sessionManager.setPushToken(token);
    }
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("From: %s", remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Timber.d("Message data payloa  %s", remoteMessage.getData());
            Notification notification = new Notification();
            notification.setImageUrl(remoteMessage.getData().get("image"));
            notification.setTitle(remoteMessage.getData().get("title"));
            notification.setDescription(remoteMessage.getData().get("body"));
            if (remoteMessage.getData().containsKey("id"))
                notification.setId(remoteMessage.getData().get("id"));
            if (remoteMessage.getData().containsKey("type"))
                notification.setType(remoteMessage.getData().get("type"));
            if (notification.getImageUrl() != null) {
                new sendNotification(getApplicationContext()).execute(notification);
            } else {
                imageNotification(getApplicationContext(), notification, null);
            }
        }
    }
    private void imageNotification(Context mContext, Notification notification, Bitmap image) {
        NotificationManager mNotificationManager =
                (NotificationManager) MyFirebaseMessagingService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder nb;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("ID", getString(R.string.app_name), importance);
            mNotificationManager.createNotificationChannel(notificationChannel);
            nb = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
            nb.setSmallIcon(R.drawable.white_logo);
        } else {
            nb = new NotificationCompat.Builder(getApplicationContext());
            nb.setSmallIcon(R.mipmap.ic_launcher);
        }
        nb.setColor(ContextCompat.getColor(this, R.color.red));
        nb.setContentTitle(notification.getTitle());
        nb.setContentText(Utils.fromHtml(notification.getDescription()));
        nb.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                R.mipmap.ic_launcher));
        Intent resultIntent = new Intent(mContext, SplashActivity.class);
        if (image != null) {
            nb.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image));
        }
        resultIntent.putExtra(Constants.NOTIFICATION_INTENT_KEY, notification);
        TaskStackBuilder TSB = TaskStackBuilder.create(MyFirebaseMessagingService.this);
        TSB.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        TSB.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = TSB.getPendingIntent(Constants.NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.setContentIntent(resultPendingIntent);
        nb.setAutoCancel(true);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(++counter, nb.build());
    }
    @SuppressLint("StaticFieldLeak")
    private class sendNotification extends AsyncTask<Notification, Void, Notification> {
        Context ctx;
        public sendNotification(Context context) {
            super();
            this.ctx = context;
        }
        @Override
        protected Notification doInBackground(Notification... params) {
            InputStream in;
            try {
                URL url = new URL(params[0].getImageUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                params[0].setImage(myBitmap);
                return params[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Notification result) {
            super.onPostExecute(result);
            try {
                imageNotification(ctx, result, result.getImage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}