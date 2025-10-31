package com.khetigaadi.tractor.rent.rentTractorNewUi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
import com.khetigaadi.room.repository.RentTractorRepository;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImpImageResp;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImplement;
import com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice.NotificationHelper;

import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
//@HiltWorker
public class RentTractorImpUploadWorker extends Worker {
    public static final String TAG = "RentTractorImpUploadWor";
    public static final String IMAGE = "image";
    public static final String IMP_TYPE = "impType";
    public static final String IMP_HOUR = "impHour";
    public static final String IMP_PER_RENT = "ImpPerRent";
    public static String ID = "id";
    private final Context context;
    private final KhetiAPIs khetiAPIs;
    private final NotificationHelper notificationHelper;
    private final RentTractorRepository repository;
    private final MultiPartHelper multiPartHelper;
    //    private Disposable disposable;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @AssistedInject
    public RentTractorImpUploadWorker(@Assisted @NonNull Context context,@Assisted @NonNull WorkerParameters params,KhetiAPIs khetiAPIs,RentTractorRepository repository)
    {
        super(context, params);
        this.context = context;
        this.khetiAPIs = khetiAPIs;
        notificationHelper = new NotificationHelper(context);
        this.repository = repository;
        multiPartHelper = new MultiPartHelper();
    }
    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();
        setForegroundAsync(createForegroundInfo());
        List<RentImplement> list = repository.getImplementsAsLive().getValue();
        assert list != null;
        disposable.add(khetiAPIs.insertRentTractorImage(
                        multiPartHelper.createRequestBodyFromText(inputData.getString(ID)),
                        multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(0).getType().getId())),
                        multiPartHelper.createRequestBodyFromText(list.get(0).getRentAmt()),
                        multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(0).getUnit().getUnitId())),
                        multiPartHelper.createMultipartBody(Uri.parse(list.get(0).getImage()),"image1",null),

                        list.size() > 1 ? multiPartHelper.createRequestBodyFromText("" + list.get(1).getType().getId()) : null,
                        list.size() > 1 ? multiPartHelper.createRequestBodyFromText(list.get(1).getRentAmt()) : null,
                        list.size() > 1 ? multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(1).getUnit().getUnitId())) : null,
                        list.size() > 1 ? multiPartHelper.createMultipartBody(Uri.parse(list.get(1).getImage()),"image2",null) : null,

                        list.size() > 2 ? multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(2).getType().getId())) : null,
                        list.size() > 2 ? multiPartHelper.createRequestBodyFromText(list.get(2).getRentAmt()) : null,
                        list.size() > 2 ? multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(2).getUnit().getUnitId())) : null,
                        list.size() > 2 ? multiPartHelper.createMultipartBody(Uri.parse(list.get(2).getImage()),"image3",null) : null,

                        list.size() > 3 ? multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(3).getType().getId())) : null,
                        list.size() > 3 ? multiPartHelper.createRequestBodyFromText(list.get(3).getRentAmt()) : null,
                        list.size() > 3 ? multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(3).getUnit().getUnitId())) : null,
                        list.size() > 3 ? multiPartHelper.createMultipartBody(Uri.parse(list.get(3).getImage()),"image4",null) : null,

                        list.size() > 4 ? multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(4).getType().getId())) : null,
                        list.size() > 4 ? multiPartHelper.createRequestBodyFromText(list.get(4).getRentAmt()) : null,
                        list.size() > 4 ? multiPartHelper.createRequestBodyFromText(String.valueOf(list.get(4).getUnit().getUnitId())) : null,
                        list.size() > 4 ? multiPartHelper.createMultipartBody(Uri.parse(list.get(4).getImage()),"image5",null) : null)


                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RentImpImageResp>() {
                    @Override
                    public void onSuccess(@NonNull RentImpImageResp rentImpImageResp) {
                        repository.deleteAllRentImplement();
                        Intent intent1 = new Intent(TAG);
                        intent1.putExtra("result", rentImpImageResp);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        onErrors(e);
                    }
                }));
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