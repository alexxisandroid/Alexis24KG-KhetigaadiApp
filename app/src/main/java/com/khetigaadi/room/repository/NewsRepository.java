package com.khetigaadi.room.repository;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.commons.utils.NewsDetailResp;
import com.khetigaadi.news.model.NewsListResp;
import com.khetigaadi.retrofit.KhetiAPIs;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
public class NewsRepository {
    private static final String TAG = "NewsRepository";
    private final KhetiAPIs khetiAPIs;
    public NewsRepository(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }
    public StateMutableLiveData<NewsDetailResp> getNewsDetails(String id) {
        StateMutableLiveData<NewsDetailResp> newsResp = new StateMutableLiveData<>();
        newsResp.postLoading();
        khetiAPIs.getNewsDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsResp::postSuccess,
                        error -> {
                            Timber.e(error.getMessage(), "onFailure: ");
                            newsResp.postError(error.getMessage());
                        });
        return newsResp;
    }
    public StateMutableLiveData<NewsListResp> getNews(int page) {
        StateMutableLiveData<NewsListResp> newsResp = new StateMutableLiveData<>();
        newsResp.postLoading();
        Map<String, String> params = new HashMap<>();
        params.put("perPage", "10");
        khetiAPIs.getNews(params, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsResp::postSuccess,
                        error -> {
                            Timber.e(error.getMessage(), "onFailure: ");
                            newsResp.postError(error.getMessage());
                        });
        return newsResp;
    }
}
