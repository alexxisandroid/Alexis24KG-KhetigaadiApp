package com.khetigaadi.tractor.rent;

import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.rent.model.RentTractorDetailsResp;
import com.khetigaadi.tractor.rent.model.SimilarRentTractorsResp;
import com.khetigaadi.tractor.usedTractor.model.SimilarParams;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class RentTractorDetailsViewModel extends ViewModel {
    private static final String TAG = "RentTractorDetailsViewM";
    private final KhetiAPIs khetiAPIs;
    private final StateMutableLiveData<RentTractorDetailsResp> rentTractor = new StateMutableLiveData<>();
    private final StateMutableLiveData<SimilarRentTractorsResp> similarTractors = new StateMutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public RentTractorDetailsViewModel(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }

    public StateMutableLiveData<RentTractorDetailsResp> getRentTractor(HashMap<String, String> params) {
        rentTractor.postLoading();
        disposable.add(khetiAPIs.getRentTractorDetails(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RentTractorDetailsResp>() {
                    @Override
                    public void onSuccess(@NonNull RentTractorDetailsResp newTractorResp) {
                        if (!newTractorResp.isError()) {
                            rentTractor.postSuccess(newTractorResp);
                        } else {
                            rentTractor.postError(newTractorResp.getMsg().get(0));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        Timber.e(t, "onFailure: ");
                        rentTractor.postError(t.getMessage());
                    }
                }));
        return rentTractor;
    }

    public StateMutableLiveData<SimilarRentTractorsResp> getSimilarRentTractors(SimilarParams rentTractor) {
        similarTractors.postLoading();
        disposable.add(khetiAPIs.getSimilarRentTractors(rentTractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimilarRentTractorsResp>() {
                    @Override
                    public void onSuccess(@NonNull SimilarRentTractorsResp newTractorResp) {
                        if (!newTractorResp.isError()) {
                            similarTractors.postSuccess(newTractorResp);
                        } else {
                            similarTractors.postError(newTractorResp.getMsg().get(0));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        Timber.e(t, "onFailure: ");
                        similarTractors.postError(t.getMessage());
                    }
                }));
        return similarTractors;
    }
}

