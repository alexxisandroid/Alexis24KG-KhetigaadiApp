package com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.base.retrofit.MultiPartHelper;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.UsedTractorRepository;
import javax.inject.Inject;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltWorker
public class FileUploadService extends Worker {
    private static final String TAG = "FileUploadService";
    private final Context context;
    private final MultiPartHelper multiPartHelper;
    Disposable mDisposable;
    public static final int NOTIFICATION_ID = 1;
    public static final int NOTIFICATION_RETRY_ID = 2;
    public static final String ID = "id";
    public static final String RIGHT = "right";
    public static final String FRONT_SIDE = "frontSide";
    public static final String LEFT = "left";
    public static final String BACK = "back";
    public static final String FRONT_TYRE = "frontTyre";
    public static final String BACK_TYRE = "backTyre";
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    UsedTractorRepository usedTractorRepository;
    private final NotificationHelper mNotificationHelper;
    @AssistedInject
    public FileUploadService(@Assisted @NonNull Context context,@Assisted @NonNull WorkerParameters params,KhetiAPIs khetiAPIs) {
        super(context, params);
        this.context = context;
        this.khetiAPIs = khetiAPIs;
        mNotificationHelper = new NotificationHelper(context);
        multiPartHelper = new MultiPartHelper();
    }

    public FileUploadService(@NonNull Context context, @NonNull WorkerParameters workerParams, Context context1, MultiPartHelper multiPartHelper, NotificationHelper mNotificationHelper) {
        super(context, workerParams);
        this.context = context1;
        this.multiPartHelper = multiPartHelper;
        this.mNotificationHelper = mNotificationHelper;
    }
    @NonNull
    @Override
    public Result doWork() {
        Timber.d("onHandleWork: ");
        // get file file here
        Data inputData = getInputData();
        setForegroundAsync(createForegroundInfo());
        int id = inputData.getInt(ID, 0);
        if (id == 0) {
            return Result.failure();
        }
        Timber.e("onHandleWork: Invalid file URI");
        @SuppressLint("CheckResult") Flowable<Double> fileObservable = Flowable.create(emitter -> {
            try {
                khetiAPIs.uploadUsedTraImages(multiPartHelper.createRequestBodyFromText(String.valueOf(id)),
                                multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(RIGHT)), "right_image", emitter),
                                multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(FRONT_SIDE)), "front_side_image", emitter),
                                inputData.getString(LEFT) != null ? multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(LEFT)), "left_image", emitter) : null,
                                inputData.getString(BACK) != null ? multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(BACK)), "back_side_image", emitter) : null,
                                inputData.getString(FRONT_TYRE) != null ? multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(FRONT_TYRE)), "front_tyre_image", emitter) : null,
                                inputData.getString(BACK_TYRE) != null ? multiPartHelper.createMultipartBody(Uri.parse(inputData.getString(BACK_TYRE)), "back_tyre_image", emitter) : null)
                        .blockingGet();
                emitter.onComplete();
            }
            catch(Exception e) {
                emitter.tryOnError(e);
            }
        },BackpressureStrategy.LATEST);
        mDisposable = fileObservable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(FileUploadService.this::onProgress,FileUploadService.this::onErrors,FileUploadService.this::onSuccess);
        return Result.success();
    }
    private void onErrors(Throwable throwable) {
        Timber.e(throwable, "onErrors: ");
        Intent successIntent = new Intent(FileProgressReceiver.ACTION_CLEAR_NOTIFICATION);
        successIntent.putExtra("notificationId",NOTIFICATION_ID);
        context.sendBroadcast(successIntent);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context,0,new Intent(context,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        Intent retryIntent = new Intent(context,RetryJobReceiver.class);
        retryIntent.putExtra("notificationId",NOTIFICATION_RETRY_ID);
        mapKepValues(retryIntent,getInputData());
        retryIntent.setAction(RetryJobReceiver.ACTION_RETRY);

        Intent clearIntent = new Intent(context,RetryJobReceiver.class);
        clearIntent.putExtra("notificationId",NOTIFICATION_RETRY_ID);
        mapKepValues(clearIntent,getInputData());
        clearIntent.setAction(RetryJobReceiver.ACTION_CLEAR);

        PendingIntent retryPendingIntent = PendingIntent.getBroadcast(context,0,retryIntent,PendingIntent.FLAG_IMMUTABLE);
        PendingIntent clearPendingIntent = PendingIntent.getBroadcast(context,0,clearIntent,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder mBuilder = mNotificationHelper.getNotification(context.getString(R.string.error_upload_failed), context.getString(R.string.message_upload_failed), resultPendingIntent);
        // attached Retry action in notification
        mBuilder.addAction(android.R.drawable.ic_menu_revert, context.getString(R.string.btn_retry_not), retryPendingIntent);
        // attached Cancel action in notification
        mBuilder.addAction(android.R.drawable.ic_menu_revert, context.getString(R.string.cancel), clearPendingIntent);
        // Notify notification
        mNotificationHelper.notify(NOTIFICATION_RETRY_ID, mBuilder);
    }
    private void mapKepValues(Intent intent,Data inputData) {
        intent.putExtra(ID,inputData.getInt(ID,0));
        intent.putExtra(RIGHT,inputData.getString(RIGHT));
        intent.putExtra(FRONT_SIDE,inputData.getString(FRONT_SIDE));
        intent.putExtra(LEFT,inputData.getString(LEFT));
        intent.putExtra(BACK,inputData.getString(BACK));
        intent.putExtra(FRONT_TYRE,inputData.getString(FRONT_TYRE));
        intent.putExtra(BACK_TYRE,inputData.getString(BACK_TYRE));
    }
    private void onProgress(Double progress) {
        Intent progressIntent = new Intent(context,FileProgressReceiver.class);
        progressIntent.setAction(FileProgressReceiver.ACTION_PROGRESS_NOTIFICATION);
        progressIntent.putExtra("notificationId",NOTIFICATION_ID);
        progressIntent.putExtra("progress",(int)(100 * progress));
        context.sendBroadcast(progressIntent);
    }
    private void onSuccess() {
        usedTractorRepository.deleteRecord(getInputData().getInt(ID,0));
        Intent successIntent = new Intent(context,FileProgressReceiver.class);
        successIntent.setAction(FileProgressReceiver.ACTION_UPLOADED);
        successIntent.putExtra("notificationId",NOTIFICATION_ID);
        successIntent.putExtra("progress",100);
        context.sendBroadcast(successIntent);
    }
    @NonNull
    private ForegroundInfo createForegroundInfo() {
        // Build a notification using bytesRead and contentLength
        Context context = getApplicationContext();
        String title = context.getString(R.string.app_name);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context).createCancelPendingIntent(getId());
        return new ForegroundInfo(2,mNotificationHelper.getNotification(title,title,intent).build());
    }
}
