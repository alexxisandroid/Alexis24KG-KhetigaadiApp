package com.khetigaadi.tractor.newTractor;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateData;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.implement.newImplement.ImplementRepository;
import com.khetigaadi.implement.newImplement.util.ImplementListResp;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.dealer.DealersRepository;
import com.khetigaadi.tractor.dealer.model.DealerListResp;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.NewTrSpecResp;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorOverviewResp;
import com.khetigaadi.tractor.newTractor.model.NewTractorPictureResp;
import com.khetigaadi.tractor.newTractor.model.SimilarNewTractorResp;
import com.khetigaadi.tractor.newTractor.model.TractorFilter;
import com.khetigaadi.tractor.usedTractor.model.SimilarParams;

import java.util.Collections;
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
public class NewTractorViewModel extends ViewModel {
    private static final String TAG = "NewTractorViewModel";
    private final KhetiAPIs khetiAPIs;
    private final StateMutableLiveData<SimilarNewTractorResp> similarTractors = new StateMutableLiveData<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final StateMutableLiveData<NewTractorOverviewResp> tractors = new StateMutableLiveData<>();
    private final ImplementRepository implementRepository;
    private final DealersRepository dealersRepository;
    private String tractorId;
    private int position = -1;
    private StateMutableLiveData<MyResponse> favourite;
    private StateMutableLiveData<NewTrSpecResp> specifications = new StateMutableLiveData<>();
    private final StateMutableLiveData<NewTractorPictureResp> picture = new StateMutableLiveData<>();
    public NewTractor getTractor() {
        return tractor;
    }
    private NewTractor tractor;
    @Inject
    public NewTractorViewModel(KhetiAPIs khetiAPIs, SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        if (savedStateHandle.contains("position")) {
            position = savedStateHandle.get("position");
        }
        if (savedStateHandle.contains(NewTractorDetails.newTractorId))
            tractorId = savedStateHandle.get(NewTractorDetails.newTractorId);
        implementRepository = new ImplementRepository(khetiAPIs);
        dealersRepository = new DealersRepository(khetiAPIs);
    }
    public void setTractorId(String tractorId) {
        this.tractorId = tractorId;
    }
    public String getTractorId() {
        return tractorId;
    }
    public int getPosition() {
        return position;
    }
    public StateMutableLiveData<NewTractorOverviewResp> getTractors() {
        return tractors;
    }
    public StateMutableLiveData<NewTractorOverviewResp> getOverViewDetails() {
        tractors.postLoading();
        Log.d("cxvcvxvcxv====",""+tractorId);
        disposable.add(khetiAPIs.getNewTractorOverView(tractorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewTractorOverviewResp>() {
                    @Override
                    public void onSuccess(@NonNull NewTractorOverviewResp newTractorOverviewResp) {
                        if (newTractorOverviewResp.isError())
                            tractors.postError(newTractorOverviewResp.getMsg().get(0));
                        else {
                            tractors.postSuccess(newTractorOverviewResp);
                            tractor = newTractorOverviewResp.getData().get(0);
                           // Log.d("xvcvxvcxvcv===",tractor.getImage().getTractor_img_name());
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
    public StateMutableLiveData<SimilarNewTractorResp> getSimilarTractors(SimilarParams similarParams) {
        similarTractors.postLoading();
        disposable.add(khetiAPIs.getSimilarNewTractors(similarParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SimilarNewTractorResp>() {
                    @Override
                    public void onSuccess(@NonNull SimilarNewTractorResp newTractorResp) {
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
    public MutableLiveData<StateData<MyResponse>> setMyFavorites(NewTractor tractor) {
        favourite = new StateMutableLiveData<>();
        favourite.postLoading();
        disposable.add(khetiAPIs.newTractorFavoriteStateChange(tractor)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        if (!myResponse.isError())
                            favourite.postSuccess(myResponse);
                        else
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
    public StateMutableLiveData<NewTrSpecResp> getSpecificationDetails() {
        HashMap<String, String> params1 = new HashMap<>();
        params1.put("tractor_id", tractorId);
        specifications = new StateMutableLiveData<>();
        specifications.postLoading();
        disposable.add(khetiAPIs.getNewTractorSpecifications(params1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewTrSpecResp>() {
                    @Override
                    public void onSuccess(@NonNull NewTrSpecResp newTrSpecResp) {
                        if (newTrSpecResp.isError())
                            specifications.postError(newTrSpecResp.getMessage());
                        else {
                            specifications.postSuccess(newTrSpecResp);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        tractors.postError(e.getMessage());
                    }
                }));
        return specifications;
    }
    public StateMutableLiveData<NewTractorPictureResp> getPictureDetails() {
        picture.postLoading();
        disposable.add(khetiAPIs.getNewTractorPicture(tractorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NewTractorPictureResp>() {
                    @Override
                    public void onSuccess(@NonNull NewTractorPictureResp pictureResp) {
                        if (pictureResp.isError())
                            picture.postError(pictureResp.getMsg().get(0));
                        else {
                            picture.postSuccess(pictureResp);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        picture.postError(e.getMessage());
                    }
                }));
        return picture;

    }
    public StateMutableLiveData<ImplementListResp> getImpList(int page) {
        TractorFilter tractorFilter = new TractorFilter();
        Filter filter = new Filter();
        if (tractor != null)
            filter.setHps(Collections.singletonList(tractor.getHp()));
        tractorFilter.setFilter(filter);
        return implementRepository.getImpList(page, tractorFilter);
    }
    public StateMutableLiveData<DealerListResp> getDealers(int page) {
        TractorFilter usedTraFilter = new TractorFilter();
        Filter filter = new Filter();
        if (tractor != null)
            filter.setBrands(Collections.singletonList(tractor.getBrand()));
        usedTraFilter.setFilter(filter);
        return dealersRepository.getDealers(page, usedTraFilter);
    }
}
