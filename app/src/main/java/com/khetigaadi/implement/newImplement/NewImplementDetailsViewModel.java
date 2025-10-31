package com.khetigaadi.implement.newImplement;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.implement.model.ImplementDetailsResp;
import com.khetigaadi.implement.model.SimilarImplementListResp;
import com.khetigaadi.implement.newImplement.model.NewImpParams;
import com.khetigaadi.implement.newImplement.util.NewImp;
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
public class NewImplementDetailsViewModel extends ViewModel {
    private static final String TAG = "NewImplementDetailsView";
    private final KhetiAPIs khetiAPIs;
    private final StateMutableLiveData<ImplementDetailsResp> implement = new StateMutableLiveData<>();
    private final StateMutableLiveData<SimilarImplementListResp> similarImps = new StateMutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public NewImplementDetailsViewModel(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }

    public StateMutableLiveData<ImplementDetailsResp> getNewImplementDetails(NewImpParams params) {
        implement.postLoading();
        disposable.add(khetiAPIs.getNewImplementDetails(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ImplementDetailsResp>() {
                    @Override
                    public void onSuccess(@NonNull ImplementDetailsResp newTractorResp) {
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

    public StateMutableLiveData<SimilarImplementListResp> getSimilarNewImplements(NewImp params) {
        similarImps.postLoading();
        disposable.add(khetiAPIs.getSimilarNewImplements(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimilarImplementListResp>() {
                    @Override
                    public void onSuccess(@NonNull SimilarImplementListResp newTractorResp) {
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
