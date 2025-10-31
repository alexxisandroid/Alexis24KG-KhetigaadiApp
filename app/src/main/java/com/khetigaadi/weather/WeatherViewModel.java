package com.khetigaadi.weather;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.weather.model.WeatherResp;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@HiltViewModel
public class  WeatherViewModel extends ViewModel {
    private final KhetiAPIs khetiAPIs;
    private StateMutableLiveData<WeatherResp> tractors;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    public WeatherViewModel(KhetiAPIs khetiAPIs, SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
    }
    public StateMutableLiveData<WeatherResp> getWeather(String city) {
        tractors = new StateMutableLiveData<>();
        tractors.postLoading();
        disposable.add(khetiAPIs.getWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResp>() {
                    @Override
                    public void onSuccess(@NonNull WeatherResp tractorsListResp) {
                        tractors.postSuccess(tractorsListResp);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        tractors.postError(e.getMessage());
                    }
                }));
        return tractors;
    }
}
