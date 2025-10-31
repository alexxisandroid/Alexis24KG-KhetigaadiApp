package com.khetigaadi.harvester;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.harvester.model.HarvesterListResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
public class HarvesterRepository {
    private final KhetiAPIs khetiAPIs;
    private StateMutableLiveData<HarvesterListResp> implementsResp;
    private final CompositeDisposable disposable = new CompositeDisposable();
    public HarvesterRepository(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }
    public StateMutableLiveData<HarvesterListResp> getHarvesterList(int page,TractorFilter tractorFilter) {
        implementsResp = new StateMutableLiveData<>();
        implementsResp.postLoading();
        disposable.add(khetiAPIs.getHarvesters(tractorFilter, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HarvesterListResp>() {
                    @Override
                    public void onSuccess(@NonNull HarvesterListResp newTractorResp) {
                        if (newTractorResp.isError())
                            implementsResp.postError(newTractorResp.getMsg().get(0));
                        else {
                            implementsResp.postSuccess(newTractorResp);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        implementsResp.postError(e.getMessage());

                    }
                }));
        return implementsResp;
    }
    public void onCleared() {
        disposable.dispose();
    }
}
