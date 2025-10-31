package com.khetigaadi.user.myfavorites;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
import com.khetigaadi.user.myfavorites.model.MyFavNewTractor;
import com.khetigaadi.user.myfavorites.model.MyFavRentTractors;
import com.khetigaadi.user.myfavorites.model.MyFavUsedTractor;
import com.khetigaadi.user.myfavorites.model.MyFavoritesResp;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@HiltViewModel
public class MyFavoritesViewModel extends ViewModel {
    private static final String TAG = "MyFavoritesViewModel";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private StateMutableLiveData<MyFavoritesResp> tractors;
    private final MutableLiveData<List<MyFavNewTractor>> newTractors = new MutableLiveData<>();
    private final MutableLiveData<List<MyFavRentTractors>> rentTractors = new MutableLiveData<>();
    private final MutableLiveData<List<MyFavUsedTractor>> usedTractors = new MutableLiveData<>();
    private final KhetiAPIs khetiAPIs;
    private StateMutableLiveData<MyResponse> favourite;

    @Inject
    public MyFavoritesViewModel(KhetiAPIs khetiAPIs) {
        this.khetiAPIs = khetiAPIs;
    }

    public MutableLiveData<List<MyFavNewTractor>> getNewTractors() {
        return newTractors;
    }

    public MutableLiveData<List<MyFavRentTractors>> getRentTractors() {
        return rentTractors;
    }

    public MutableLiveData<List<MyFavUsedTractor>> getUsedTractors() {
        return usedTractors;
    }

    public StateMutableLiveData<MyFavoritesResp> getTractors() {

        if (tractors != null)
            return tractors;
        else {
            tractors = new StateMutableLiveData<>();
            tractors.postLoading();
            disposable.add(khetiAPIs.getMyFavorites()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<MyFavoritesResp>() {
                        @Override
                        public void onSuccess(@NonNull MyFavoritesResp myFavoritesResp) {
                            if (!myFavoritesResp.isError()) {
                                tractors.postSuccess(myFavoritesResp);
                                newTractors.setValue(myFavoritesResp.getData().getNewTractors());
                                usedTractors.setValue(myFavoritesResp.getData().getUsedTractors());
                                rentTractors.setValue(myFavoritesResp.getData().getRentTractors());
                            } else
                                tractors.postError(myFavoritesResp.getMsg().get(0));
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.e(e, "onError: ");
                            tractors.postError(e.getMessage());

                        }
                    })
            );
            return tractors;
        }
    }

    public MutableLiveData<StateData<MyResponse>> setNewTractorFavorites(NewTractor tractor) {
        favourite = new StateMutableLiveData<>();
        favourite.postLoading();
        disposable.add(khetiAPIs.newTractorFavoriteStateChange(tractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        if (!myResponse.isError()) {
                            favourite.postSuccess(myResponse);
                            List<MyFavNewTractor> myFavNewTractors = newTractors.getValue();
                            if (myFavNewTractors != null)
                                newTractors.postValue(myFavNewTractors.stream().filter(item -> !item.getNewTractor().getId().equals(tractor.getId())).collect(Collectors.toList()));
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
                            List<MyFavUsedTractor> my = usedTractors.getValue();
                            if (my != null)
                                usedTractors.postValue(my.stream().filter(item -> !item.getUsedTractor().getId().equals(tractor.getId())).collect(Collectors.toList()));
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

    public MutableLiveData<StateData<MyResponse>> setRentTractorFavorites(RentTractor tractor) {
        favourite = new StateMutableLiveData<>();
        favourite.postLoading();
        disposable.add(khetiAPIs.rentTractorFavoriteStateChange(tractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        if (!myResponse.isError()) {
                            favourite.postSuccess(myResponse);
                            List<MyFavRentTractors> rentTractorsValue = rentTractors.getValue();
                            if (rentTractorsValue != null) {
                                rentTractors.postValue(rentTractorsValue.stream().filter(item -> !item.getRentTractor().getRentTractorId().equals(tractor.getRentTractorId())).collect(Collectors.toList()));
                            }
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
