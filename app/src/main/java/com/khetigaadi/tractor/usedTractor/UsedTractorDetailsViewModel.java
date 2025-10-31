package com.khetigaadi.tractor.usedTractor;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.usedTractor.model.SimilarUsedTractorsResp;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class UsedTractorDetailsViewModel extends ViewModel {
    private static final String TAG = "UsedTractorDetailsViewM";
    private final KhetiAPIs khetiAPIs;
    private final StateMutableLiveData<SimilarUsedTractorsResp> similarTractors = new StateMutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    public UsedTractorDetailsViewModel(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }
    public StateMutableLiveData<SimilarUsedTractorsResp> getSimilarTractors(UsedTractor tractor) {
        similarTractors.postLoading();
        disposable.add(khetiAPIs.getSimilarUsedTractors(tractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimilarUsedTractorsResp>() {
                    @Override
                    public void onSuccess(@NonNull SimilarUsedTractorsResp newTractorResp) {
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
    public MutableLiveData<StateData<MyResponse>> setUsedTractorFavorites(UsedTractor tractor) {
        StateMutableLiveData<MyResponse> favourite = new StateMutableLiveData<>();
        favourite.postLoading();
        disposable.add(khetiAPIs.usedTractorFavoriteStateChange(tractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        if (!myResponse.isError()) {
                            favourite.postSuccess(myResponse);
                        } else
                            favourite.postError(myResponse.getMsg().get(0));
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        favourite.postError(e.getMessage());
                    }
                })
        );
        return favourite;
    }
}
