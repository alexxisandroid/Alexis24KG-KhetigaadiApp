package com.khetigaadi.ecom.order;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.shop.model.MyOrderResp;
import com.khetigaadi.user.SessionManager;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class MyOrderListViewModel extends ViewModel {
    private static final String TAG = "MyOrderListViewModel";
    private final ShopAPIs shopAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    public MyOrderListViewModel(ShopAPIs shopAPIs, SessionManager sessionManager, SavedStateHandle savedStateHandle) {
        this.shopAPIs = shopAPIs;
    }
    public StateMutableLiveData<MyOrderResp> getMyOrders() {
        StateMutableLiveData<MyOrderResp> myOrders = new StateMutableLiveData<>();
        myOrders.postLoading();
        disposable.add(shopAPIs.getMyOrders()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyOrderResp>() {
                    @Override
                    public void onSuccess(@NonNull MyOrderResp productListResp) {
                        if (productListResp.isError())
                            myOrders.postError(productListResp.getMsg().get(0));
                        else
                            myOrders.postSuccess(productListResp);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        myOrders.postError(e.getMessage());
                    }
                }));
        return myOrders;
    }
}
