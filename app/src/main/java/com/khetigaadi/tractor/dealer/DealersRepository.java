package com.khetigaadi.tractor.dealer;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.dealer.model.DealerListResp;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
public class DealersRepository {
    private static final String TAG = "DealersRepository";
    private final KhetiAPIs khetiAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StateMutableLiveData<DealerListResp> dealers;
    public DealersRepository(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }
    public StateMutableLiveData<DealerListResp> getDealers(int page,TractorFilter usedTraFilter) {
        dealers = new StateMutableLiveData<>();
        dealers.postLoading();
        disposable.add(khetiAPIs.getDealers(usedTraFilter, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<DealerListResp>() {
                    @Override
                    public void onSuccess(@NonNull DealerListResp dealerListResp) {
                        if (dealerListResp.isError())
                            dealers.postError(dealerListResp.getMsg().get(0));
                        else {
                            dealers.postSuccess(dealerListResp);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        dealers.postError(e.getMessage());
                    }
                }));
        return dealers;
    }
    public void onCleared() {
        disposable.dispose();
    }
}
