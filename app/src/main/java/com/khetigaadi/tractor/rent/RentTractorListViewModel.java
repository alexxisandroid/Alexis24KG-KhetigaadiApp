package com.khetigaadi.tractor.rent;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;
import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.tractor.rent.model.RentTractorResp;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class RentTractorListViewModel extends ViewModel {
    private static final String TAG = "RentTractorListViewMode";
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    private Filter filter;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StateMutableLiveData<RentTractorResp> tractors;
    private final KhetiAPIs khetiAPIs;
    @Inject
    public RentTractorListViewModel(KhetiAPIs khetiAPIs,  SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        filter = savedStateHandle.contains("filter") ? savedStateHandle.get("filter") : new Filter();
    }
    public Filter getFilter() {
        return filter;
    }
    public StateMutableLiveData<RentTractorResp> getRentTractors(int page) {
        TractorFilter tractorFilter = new TractorFilter(filter);
        tractorFilter.setFilter(filter);
        tractors = new StateMutableLiveData<>();
        tractors.postLoading();
        disposable.add(khetiAPIs.getRentTractors(tractorFilter, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<RentTractorResp>() {
                    @Override
                    public void onSuccess(@NonNull RentTractorResp rentTractorResp) {
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
    public MutableLiveData<StateData<MyResponse>> setRentTractorFavorites(RentTractor tractor) {
        StateMutableLiveData<MyResponse> favourite = new StateMutableLiveData<>();
        favourite.postLoading();
        disposable.add(khetiAPIs.rentTractorFavoriteStateChange(tractor)
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
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
