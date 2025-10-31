package com.khetigaadi.youtube;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import com.khetigaadi.base.model.youtube.ListItem;
import com.khetigaadi.base.model.youtube.VideoListResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
public class YouTubeRepository {
    private KhetiAPIs khetiAPIs;
    public MutableLiveData<List<ListItem>> videoList = new MutableLiveData();
    public YouTubeRepository(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }
    public void getVideoList(String channelID,String key,String pageToken) {
        khetiAPIs.getYouTubeVideoList(channelID,key,pageToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<VideoListResp>() {
                    @Override
                    public void onSuccess(@NonNull VideoListResp videoResp) {
                        videoList.postValue(videoResp.getItems());
                        Log.d("ytubeRep===",""+videoResp.getItems().size());
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e.getMessage(), "onFailure: ");
                        videoList.postValue(null);
                    }
                });
    }
}
