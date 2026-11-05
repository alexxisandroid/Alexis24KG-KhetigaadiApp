package com.khetigaadi.ecom.list;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.shop.model.Filter;
import com.khetigaadi.shop.model.ProductListFilter;
import com.khetigaadi.shop.model.ProductListResp;
import com.khetigaadi.user.SessionManager;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class ProductListViewModel extends ViewModel {
    private static final String TAG = "ProductListViewModel";
    private final CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Filter> filter;
    private final ShopAPIs shopAPIs;
    private SessionManager sessionManager;
    @Inject
    ProductListViewModel(ShopAPIs shopAPIs,SessionManager sessionManager,SavedStateHandle savedStateHandle) {
        this.shopAPIs = shopAPIs;
        this.sessionManager = sessionManager;
        filter = savedStateHandle.getLiveData(ProductListFilterActivity.FILTER);
    }
    public StateMutableLiveData<ProductListResp> getList(int page) {
        StateMutableLiveData<ProductListResp> gardenTools = new StateMutableLiveData<>();
        gardenTools.postLoading();
        ProductListFilter productListFilter = new ProductListFilter();
        productListFilter.setFilter(filter.getValue());
        disposable.add(shopAPIs.getProducts(productListFilter,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProductListResp>() {
                    @Override
                    public void onSuccess(@NonNull ProductListResp productListResp) {
                        if (productListResp.isError())
                            gardenTools.postError(""+productListResp.getMsg().get(0));
                        else {
                            gardenTools.postSuccess(productListResp);
                            sessionManager.setEcomOther(productListResp.getOthers());
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
    public Filter getFilter() {
        return filter.getValue();
    }
    public void setFilter(Filter filter) {
        this.filter.setValue(filter);
    }
}
