package com.khetigaadi.mandi;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.mandi.model.MondiPriceResp;
import com.khetigaadi.retrofit.KhetiAPIs;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class MandiPriceRepository {
    private static final String TAG = "MandiPriceRepository";
    private final KhetiAPIs khetiAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StateMutableLiveData<MondiPriceResp> dealers;

    public MandiPriceRepository(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }

    public StateMutableLiveData<MondiPriceResp> getMandiPrice(int offset, String state, String commodity) {
        dealers = new StateMutableLiveData<>();
        dealers.postLoading();
        disposable.add(khetiAPIs.getMandiPrice(offset, state, commodity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MondiPriceResp>() {
                    @Override
                    public void onSuccess(@NonNull MondiPriceResp dealerListResp) {
                        dealers.postSuccess(dealerListResp);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        dealers.postError(e.getMessage());

                    }
                }));
        return dealers;
    }
}
