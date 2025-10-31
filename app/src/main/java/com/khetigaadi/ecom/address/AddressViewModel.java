package com.khetigaadi.ecom.address;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.shop.model.AddressListResp;
import com.khetigaadi.shop.model.AddressResp;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class AddressViewModel extends ViewModel {
    private static final String TAG = "AddressViewModel";
    private final ShopAPIs shopAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    AddressViewModel(ShopAPIs shopAPIs) {
        this.shopAPIs = shopAPIs;
    }
    public StateMutableLiveData<AddressListResp> getAddresses(String customerId) {
        StateMutableLiveData<AddressListResp> gardenTools = new StateMutableLiveData<>();
        gardenTools.postLoading();
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(customerId)) {
            params.put("customer_user_id", customerId);
        }
        disposable.add(shopAPIs.getAddresses(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AddressListResp>() {
                    @Override
                    public void onSuccess(@NonNull AddressListResp productListResp) {
                        if (productListResp.isError())
                            gardenTools.postError(productListResp.getMsg().get(0));
                        else
                            gardenTools.postSuccess(productListResp);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        gardenTools.postError(e.getMessage());
                    }
                }));
        return gardenTools;
    }

    public StateMutableLiveData<AddressResp> deleteAddress(String id) {
        StateMutableLiveData<AddressResp> gardenTools = new StateMutableLiveData<>();
        gardenTools.postLoading();
        disposable.add(shopAPIs.deleteAddress(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AddressResp>() {
                    @Override
                    public void onSuccess(@NonNull AddressResp productListResp) {
                        if (productListResp.isError())
                            gardenTools.postError(productListResp.getMsg().get(0));
                        else
                            gardenTools.postSuccess(productListResp);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        gardenTools.postError(e.getMessage());
                    }
                }));
        return gardenTools;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

    public StateMutableLiveData<AddressResp> setAsDefault(String id, String customerId) {
        StateMutableLiveData<AddressResp> gardenTools = new StateMutableLiveData<>();
        gardenTools.postLoading();
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(customerId)) {
            params.put("customer_user_id", customerId);
        }
        disposable.add(shopAPIs.setAsDefault(id, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AddressResp>() {
                    @Override
                    public void onSuccess(@NonNull AddressResp productListResp) {
                        if (productListResp.isError())
                            gardenTools.postError(productListResp.getMsg().get(0));
                        else
                            gardenTools.postSuccess(productListResp);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        gardenTools.postError(e.getMessage());
                    }
                }));
        return gardenTools;
    }
}
