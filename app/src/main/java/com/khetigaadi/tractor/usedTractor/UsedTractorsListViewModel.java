package com.khetigaadi.tractor.usedTractor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorListResp;
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
public class UsedTractorsListViewModel extends ViewModel {
    private static final String TAG = "UsedTractorsListViewMod";
    private final KhetiAPIs khetiAPIs;
    private Filter filter;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StateMutableLiveData<MyResponse> favourite;

    @Inject
    UsedTractorsListViewModel(KhetiAPIs khetiAPIs, SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        filter = savedStateHandle.get("filter");
    }

    public StateMutableLiveData<UsedTractorListResp> getUsedTractors(int currPageNo) {
        StateMutableLiveData<UsedTractorListResp> tractors = new StateMutableLiveData<>();
        tractors.postLoading();
        TractorFilter tractorFilter = new TractorFilter(filter);
        tractorFilter.setFilter(filter);
        disposable.add(khetiAPIs.getUsedTractors(tractorFilter, currPageNo)
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

    public MutableLiveData<StateData<MyResponse>> setUsedTractorFavorites(UsedTractor tractor) {
        favourite = new StateMutableLiveData<>();
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

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
