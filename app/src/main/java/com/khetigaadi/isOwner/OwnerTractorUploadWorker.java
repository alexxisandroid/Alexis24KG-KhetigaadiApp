package com.khetigaadi.isOwner;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.khetigaadi.R;
import com.khetigaadi.base.retrofit.MultiPartHelper;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice.NotificationHelper;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltWorker
public class OwnerTractorUploadWorker extends Worker {
    private static final String TAG = "OwnerTractorUploadWorke";
    public static String ID="id";
    public static String FRONT_SIDE_IMAGE="frontSideImage";
    public static String RIGHT_IMAGE="rightSideImage";
    private final Context context;
    private final KhetiAPIs khetiAPIs;
    private final NotificationHelper notificationHelper;
    private final MultiPartHelper multiPartHelper;
    private Disposable disposable;
    @AssistedInject
    public OwnerTractorUploadWorker(@Assisted @NonNull Context context,@Assisted @NonNull WorkerParameters params,KhetiAPIs khetiAPIs)
    {
        super(context, params);
        this.context = context;
        this.khetiAPIs = khetiAPIs;
        notificationHelper = new NotificationHelper(context);
        multiPartHelper = new MultiPartHelper();
    }
    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        setForegroundAsync(createForegroundInfo());
        @SuppressLint("CheckResult") Flowable<Double> fileObservable = Flowable.create(emitter -> {
            khetiAPIs.insertOwnerTractorImage(multiPartHelper.createRequestBodyFromText(String.valueOf(inputData.getString(ID))),
                            TextUtils.isEmpty(inputData.getString(FRONT_SIDE_IMAGE)) ? null : multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(FRONT_SIDE_IMAGE)),"front_side_image",emitter),
                            TextUtils.isEmpty(inputData.getString(RIGHT_IMAGE)) ? null : multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(RIGHT_IMAGE)),"right_image",emitter)).
                    blockingGet();
            emitter.onComplete();
        }, BackpressureStrategy.LATEST);
        disposable = fileObservable.subscribeOn(Schedulers.computation()).observeOn(Schedulers.io()).subscribe(this::onProgress, this::onErrors, this::onSuccess);
        return Result.success();
    }
    private void onSuccess() {
        Intent intent1 = new Intent(TAG);
        intent1.putExtra("isSuccess", true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }
    private void onErrors(Throwable throwable) {
        Intent intent1 = new Intent(TAG);
        intent1.putExtra("Error", throwable.getMessage());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }
    private void onProgress(Double progress) {
        Timber.e("onErrors: %s", progress);
        Intent intent1 = new Intent(TAG);
        intent1.putExtra("Progress", progress);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
    }
    @NonNull
    private ForegroundInfo createForegroundInfo() {
        // Build a notification using bytesRead and contentLength
        Context context = getApplicationContext();
        String title = context.getString(R.string.app_name);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context).createCancelPendingIntent(getId());
        return new ForegroundInfo(2, notificationHelper.getNotification(title, title, intent).build());
    }
}
