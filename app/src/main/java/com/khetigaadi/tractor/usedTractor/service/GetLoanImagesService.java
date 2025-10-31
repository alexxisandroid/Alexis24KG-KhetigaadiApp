package com.khetigaadi.tractor.usedTractor.service;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.retrofit.MultiPartHelper;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice.FileProgressReceiver;
import com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice.NotificationHelper;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import timber.log.Timber;
@HiltWorker
public class GetLoanImagesService extends Worker {
    private final MultiPartHelper multiPartHelper;
    Disposable mDisposable;
    public static final int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_RETRY_ID = 2;
    NotificationHelper mNotificationHelper;
    private String id;
    public static final String ID = "id";
    public static final String FRONT = "front";
    public static final String BACK = "back";
    private final Context context;
    private final KhetiAPIs khetiAPIs;
    @AssistedInject
    public GetLoanImagesService(@Assisted @NonNull Context context,@Assisted @NonNull WorkerParameters workerParams,KhetiAPIs khetiAPIs) {
        super(context, workerParams);
        this.context = context;
        this.khetiAPIs = khetiAPIs;
        mNotificationHelper = new NotificationHelper(context);
        multiPartHelper = new MultiPartHelper();
    }

    public GetLoanImagesService(@NonNull Context context, @NonNull WorkerParameters workerParams, MultiPartHelper multiPartHelper, Context context1, KhetiAPIs khetiAPIs) {
        super(context, workerParams);
        this.multiPartHelper = multiPartHelper;
        this.context = context1;
        this.khetiAPIs = khetiAPIs;
    }

    @NonNull
    @Override
    public Result doWork() {
        Timber.d("onHandleWork: ");
        Data inputData = getInputData();
        // get file file here
        id = inputData.getString("id");
        if (TextUtils.isEmpty(id)) {
            Timber.e("onHandleWork: Invalid file URI");
            return Result.failure();
        }
        @SuppressLint("CheckResult") Flowable<Double> fileObservable = Flowable.create(emitter -> {
            khetiAPIs.uploadGetLoanImages(multiPartHelper.createRequestBodyFromText(String.valueOf(inputData.getString("id"))),
                    (MultipartBody.Part) multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(FRONT)),"adhar_front",emitter),
                    (MultipartBody.Part) multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(BACK)),"adhar_back",emitter)).blockingGet();
            emitter.onComplete();
        }, BackpressureStrategy.LATEST);
        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(GetLoanImagesService.this::onProgress,GetLoanImagesService.this::onErrors,GetLoanImagesService.this::onSuccess);
        return Result.success();
    }
    private void onErrors(Throwable throwable) {
        Timber.e(throwable, "onErrors: ");
        Intent successIntent = new Intent(FileProgressReceiver.ACTION_CLEAR_NOTIFICATION);
        successIntent.putExtra("notificationId", NOTIFICATION_ID);
        context.sendBroadcast(successIntent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        Intent retryIntent = new Intent(context, GetLoanRetryJobReceiver.class);
        retryIntent.putExtra("notificationId", NOTIFICATION_RETRY_ID);
        retryIntent.putExtra(ID, getInputData().getString(ID));
        retryIntent.putExtra(FRONT, getInputData().getString(FRONT));
        retryIntent.putExtra(BACK, getInputData().getString(BACK));
        retryIntent.setAction(GetLoanRetryJobReceiver.ACTION_RETRY);
        Intent clearIntent = new Intent(context, GetLoanRetryJobReceiver.class);
        clearIntent.putExtra("notificationId", NOTIFICATION_RETRY_ID);
        clearIntent.putExtra(ID, getInputData().getString(ID));
        clearIntent.putExtra(FRONT, getInputData().getString(FRONT));
        clearIntent.putExtra(BACK, getInputData().getString(BACK));
        clearIntent.setAction(GetLoanRetryJobReceiver.ACTION_CLEAR);
        PendingIntent retryPendingIntent = PendingIntent.getBroadcast(context,0,retryIntent,PendingIntent.FLAG_IMMUTABLE);
        PendingIntent clearPendingIntent = PendingIntent.getBroadcast(context,0,clearIntent,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder mBuilder = mNotificationHelper.getNotification(context.getString(R.string.aadhar_upload_failed),context.getString(R.string.aadhar_message_upload_failed),resultPendingIntent);
        // attached Retry action in notification
        mBuilder.addAction(android.R.drawable.ic_menu_revert,context.getString(R.string.btn_retry_not),retryPendingIntent);
        // attached Cancel action in notification
        mBuilder.addAction(android.R.drawable.ic_menu_revert,context.getString(R.string.cancel),clearPendingIntent);
        // Notify notification
        mNotificationHelper.notify(NOTIFICATION_RETRY_ID,mBuilder);
    }
    private void onProgress(Double progress) {
        Intent progressIntent = new Intent(context,FileProgressReceiver.class);
        progressIntent.setAction(FileProgressReceiver.ACTION_PROGRESS_NOTIFICATION);
        progressIntent.putExtra("notificationId",NOTIFICATION_ID);
        progressIntent.putExtra("progress",(int) (100 * progress));
        context.sendBroadcast(progressIntent);
    }
    private void onSuccess() {
        Intent successIntent = new Intent(context,FileProgressReceiver.class);
        successIntent.setAction(FileProgressReceiver.ACTION_UPLOADED);
        successIntent.putExtra("notificationId",NOTIFICATION_ID);
        successIntent.putExtra("progress",100);
        context.sendBroadcast(successIntent);
    }
}
