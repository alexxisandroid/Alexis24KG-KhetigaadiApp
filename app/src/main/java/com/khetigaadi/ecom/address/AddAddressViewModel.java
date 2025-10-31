package com.khetigaadi.ecom.address;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.shop.model.Address;
import com.khetigaadi.shop.model.AddressResp;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class AddAddressViewModel extends ViewModel {
    private static final String TAG = "AddAddressViewModel";
    private final CompositeDisposable disposable;
    private final Address address;
    private final ShopAPIs shopAPIs;
    @Inject
    AddAddressViewModel(ShopAPIs shopAPIs, CompositeDisposable disposable, SavedStateHandle savedStateHandle) {
        this.shopAPIs = shopAPIs;
        this.disposable = disposable;
        address = savedStateHandle.contains("address") ? savedStateHandle.get("address") : new Address();
    }
    public Address getAddress() {
        return address;
    }
    public StateMutableLiveData<AddressResp> addUpdateAddress(Address address) {
        StateMutableLiveData<AddressResp> gardenTools = new StateMutableLiveData<>();
        gardenTools.postLoading();
        disposable.add(shopAPIs.addUpdateAddress(address)
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
