package com.khetigaadi.commons.fragments;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.youtube.ListItem;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.harvester.HarvesterRepository;
import com.khetigaadi.harvester.model.HarvesterListResp;
import com.khetigaadi.implement.compare.model.CompareImplementListResp;
import com.khetigaadi.news.model.NewsListResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.NewsRepository;
import com.khetigaadi.tractor.compare.model.CompareTractorsListResp;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorListResp;
import com.khetigaadi.youtube.YouTubeRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class HomeFragmentViewModel extends ViewModel {
    private static final String TAG = "HomeFragmentViewModel";
    private final KhetiAPIs khetiAPIs;
    private final NewsRepository newsRepository;
    private final YouTubeRepository videoRepository;
    private HarvesterRepository harvesterRepository;
    private StateMutableLiveData<CompareTractorsListResp> tractors;
    private StateMutableLiveData<CompareImplementListResp> compareImplements;
    private final CompositeDisposable disposable = new CompositeDisposable();
    StateMutableLiveData<NewsListResp> news;
    public MutableLiveData<List<ListItem>> videoList;
    @Inject
    public HomeFragmentViewModel(KhetiAPIs khetiAPIs,NewsRepository newsRepository,HarvesterRepository harvesterRepository,SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        this.newsRepository = newsRepository;
        this.harvesterRepository = harvesterRepository;
        news = new StateMutableLiveData<>();
        this.videoRepository = new YouTubeRepository(khetiAPIs);
        videoList = videoRepository.videoList;
    }
    public StateMutableLiveData<CompareTractorsListResp> getCompareTractors(int page) {
        tractors = new StateMutableLiveData<>();
        tractors.postLoading();
        disposable.add(khetiAPIs.getCompareTractors(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CompareTractorsListResp>() {
                    @Override
                    public void onSuccess(@NonNull CompareTractorsListResp tractorsListResp) {
                        if (tractorsListResp.isError())
                            tractors.postError(tractorsListResp.getMsg().get(0));
                        else {
                            tractors.postSuccess(tractorsListResp);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        tractors.postError(e.getMessage());

                    }
                }));
        return tractors;
    }
    public StateMutableLiveData<CompareImplementListResp> getCompareImplements(int page) {
        compareImplements = new StateMutableLiveData<>();
        compareImplements.postLoading();
        disposable.add(khetiAPIs.getCompareImplements(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CompareImplementListResp>() {
                    @Override
                    public void onSuccess(@NonNull CompareImplementListResp tractorsListResp) {
                        if (tractorsListResp.isError())
                            compareImplements.postError(tractorsListResp.getMsg().get(0));
                        else {
                            compareImplements.postSuccess(tractorsListResp);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        compareImplements.postError(e.getMessage());

                    }
                }));
        return compareImplements;
    }
    public StateMutableLiveData<UsedTractorListResp> getUsedTractors(int currPageNo) {
        StateMutableLiveData<UsedTractorListResp> tractors = new StateMutableLiveData<>();
        tractors.postLoading();
        disposable.add(khetiAPIs.getUsedTractors(new TractorFilter(), currPageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UsedTractorListResp>() {
                    @Override
                    public void onSuccess(@NonNull UsedTractorListResp usedTractorListResp) {
                        tractors.postSuccess(usedTractorListResp);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        tractors.postError(e.getMessage());
                    }
                }));
        return tractors;
    }
    public void getVideos(String channelID,String key,String pageToken) {
        if (videoList != null && videoList.getValue() == null)
            videoRepository.getVideoList(channelID, key, pageToken);
    }
    public StateMutableLiveData<HarvesterListResp> getHarvesterList(int page) {
        TractorFilter tractorFilter = new TractorFilter();
        tractorFilter.setFilter(new Filter());
        return harvesterRepository.getHarvesterList(page,tractorFilter);
    }
}
