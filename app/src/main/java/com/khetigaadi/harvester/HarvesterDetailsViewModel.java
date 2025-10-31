package com.khetigaadi.harvester;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.harvester.model.Harvester;
import com.khetigaadi.harvester.model.HarvesterResp;
import com.khetigaadi.harvester.model.SimilarHarvesterListResp;
import com.khetigaadi.retrofit.KhetiAPIs;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class HarvesterDetailsViewModel extends ViewModel {
    private static final String TAG = "NewImplementDetailsView";
    private final KhetiAPIs khetiAPIs;
    private final StateMutableLiveData<HarvesterResp> implement = new StateMutableLiveData<>();
    private final StateMutableLiveData<SimilarHarvesterListResp> similarImps = new StateMutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    public HarvesterDetailsViewModel(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }
    public StateMutableLiveData<HarvesterResp> getNewImplementDetails(String harvesterId) {
        implement.postLoading();
        disposable.add(khetiAPIs.getHarvester(harvesterId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HarvesterResp>() {
                    @Override
                    public void onSuccess(@NonNull HarvesterResp newTractorResp) {
                        if (!newTractorResp.isError()) {
                            implement.postSuccess(newTractorResp);
                        } else {
                            implement.postError(newTractorResp.getMsg().get(0));
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable t) {
                        Timber.e(t, "onFailure: ");
                        implement.postError(t.getMessage());
                    }
                }));
        return implement;
    }
    public StateMutableLiveData<SimilarHarvesterListResp> getSimilarNewImplements(Harvester params) {
        similarImps.postLoading();
        disposable.add(khetiAPIs.getSimilarHarvesters(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimilarHarvesterListResp>() {
                    @Override
                    public void onSuccess(@NonNull SimilarHarvesterListResp newTractorResp) {
                        if (!newTractorResp.isError()) {
                            similarImps.postSuccess(newTractorResp);
                        } else {
                            similarImps.postError(newTractorResp.getMsg().get(0));
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable t) {
                        Timber.e(t, "onFailure: ");
                        similarImps.postError(t.getMessage());
                    }
                }));
        return similarImps;
    }
}
