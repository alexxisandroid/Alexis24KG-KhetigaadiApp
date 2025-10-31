package com.khetigaadi.tractor.newTractor;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorResp;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class NewTractorsListViewModel extends ViewModel {
    private static final String TAG = "NewTractorsListViewMode";
    private final KhetiAPIs khetiAPIs;
    private Filter filter;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StateMutableLiveData<NewTractorResp> tractors;
    private StateMutableLiveData<MyResponse> favourite;
    @Inject
    NewTractorsListViewModel(KhetiAPIs khetiAPIs,SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        filter = savedStateHandle.get("filter");
    }
    public Filter getFilter() {
        return filter;
    }
    public StateMutableLiveData<NewTractorResp> getNewTractors(int page) {
        TractorFilter tractorFilter = new TractorFilter(filter);
        tractorFilter.setFilter(filter);
        Log.d("vcbcvbcvbvbrtyey===",""+filter+"=="+filter.getBrands());
        tractors = new StateMutableLiveData<>();
        tractors.postLoading();
        disposable.add(khetiAPIs.getNewTractors(tractorFilter, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewTractorResp>() {
                    @Override
                    public void onSuccess(@NonNull NewTractorResp newTractorResp) {
                        if (newTractorResp.isError())
                            tractors.postError(newTractorResp.getMsg().get(0));
                        else {
                            tractors.postSuccess(newTractorResp);
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
    public MutableLiveData<StateData<MyResponse>> setMyFavorites(NewTractor tractor) {
        favourite = new StateMutableLiveData<>();
        favourite.postLoading();
        disposable.add(khetiAPIs.newTractorFavoriteStateChange(tractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        favourite.postSuccess(myResponse);
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
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
    public void setFilter(Filter filterValue) {
        filter = filterValue;
    }
}
