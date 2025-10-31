package com.khetigaadi.user.myRentTractor;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
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
public class MyRentTractorListViewModel extends ViewModel {
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StateMutableLiveData<MyRentTractorResp> tractors;
    private final KhetiAPIs khetiAPIs;
    @Inject
    public MyRentTractorListViewModel(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }
    public StateMutableLiveData<MyRentTractorResp> getRentTractors() {
        tractors = new StateMutableLiveData<>();
        tractors.postLoading();
        disposable.add(khetiAPIs.getMyRentTractors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyRentTractorResp>() {
                    @Override
                    public void onSuccess(@NonNull MyRentTractorResp rentTractorResp) {
                        tractors.postSuccess(rentTractorResp);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        tractors.postError(e.getMessage());
                    }
                }));
        return tractors;
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}

