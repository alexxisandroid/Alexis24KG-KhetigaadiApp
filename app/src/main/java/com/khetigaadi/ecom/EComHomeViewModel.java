package com.khetigaadi.ecom;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.room.repository.CategoryRepository;
import com.khetigaadi.shop.model.HomeProductResp;
import com.khetigaadi.user.SessionManager;
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
public class EComHomeViewModel extends ViewModel {
    private static final String TAG = "EComHomeViewModel";
    private final ShopAPIs shopAPIs;
    private final CategoryRepository categoryRepository;
    private final SessionManager sessionManager;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    public EComHomeViewModel(ShopAPIs shopAPIs,CategoryRepository categoryRepository,SessionManager sessionManager) {
        this.shopAPIs = shopAPIs;
        this.categoryRepository = categoryRepository;
        this.sessionManager = sessionManager;
    }
    public StateMutableLiveData<HomeProductResp> getHomeProducts() {
        StateMutableLiveData<HomeProductResp> gardenTools = new StateMutableLiveData<>();
        gardenTools.postLoading();
        disposable.add(shopAPIs.getHomeList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HomeProductResp>() {
                    @Override
                    public void onSuccess(@NonNull HomeProductResp productListResp) {
                        if (productListResp.isError())
                            gardenTools.postError(productListResp.getMsg().get(0).toString());
                        else {
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
    public void onBannerClicked() {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", sessionManager.getUser().getMobile());
        shopAPIs.sendBannerClickEnq(params)
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .observeOn(io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.observers.DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull MyResponse myResponse) {
                        Timber.e("onSuccess: %s",myResponse.getMsg());
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e("onError: %s",e.getMessage());
                    }
                });
    }
}
