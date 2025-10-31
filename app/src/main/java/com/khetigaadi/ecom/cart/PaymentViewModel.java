package com.khetigaadi.ecom.cart;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.room.repository.CartRepository;
import com.khetigaadi.shop.model.Order;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class PaymentViewModel extends ViewModel {
    private static final String TAG = "PaymentViewModel";
    private final ShopAPIs shopAPIs;
    private final CartRepository cartRepository;
    private final SavedStateHandle savedStateHandle;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<Order> order;
    @Inject
    PaymentViewModel(ShopAPIs shopAPIs,CartRepository cartRepository,SavedStateHandle savedStateHandle) {
        this.shopAPIs = shopAPIs;
        this.cartRepository = cartRepository;
        this.savedStateHandle = savedStateHandle;
        order = savedStateHandle.getLiveData("order");
    }
    public StateMutableLiveData<MyResponse> insertOrder(String payuResponse) {
        Order order1 = order.getValue();
        if (order1 != null) {
            order1.setPayuResp(payuResponse);
        }
        order.setValue(order1);
        StateMutableLiveData<MyResponse> gardenTools = new StateMutableLiveData<>();
        gardenTools.postLoading();
        disposable.add(shopAPIs.placeOrder(order.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse productListResp) {
                        if (productListResp.isError())
                            gardenTools.postError(productListResp.getMsg().get(0));
                        else {
                            cartRepository.deleteProducts(order.getValue().getProducts());
                            gardenTools.postSuccess(productListResp);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        gardenTools.postError(e.getMessage());
                    }
                }));
        return gardenTools;
    }
    public MutableLiveData<Order> getOrder() {
        return order;
    }
    public void setPaymentTypeOnline(boolean isOnline) {
        Order order1 = order.getValue();
        if (order1 != null) {
            order1.setOnline(isOnline);
        }
        order.setValue(order1);
    }
}
