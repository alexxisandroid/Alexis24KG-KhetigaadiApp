package com.khetigaadi.tractor.compare;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.compare.model.CompTractResp;
import com.khetigaadi.tractor.compare.model.CompareTractorParam;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@HiltViewModel
public class CompareTractorViewModel extends ViewModel {
    private static final String TAG = "CompareTractorViewModel";
    private final KhetiAPIs khetiAPIs;
    private final CompareTractorParam tractorParam;

    private final StateMutableLiveData<CompTractResp> compareTractors = new StateMutableLiveData<>();

    @Inject
    CompareTractorViewModel(KhetiAPIs khetiAPIs,  SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        tractorParam = savedStateHandle.get("tractorParam");
    }

    public StateMutableLiveData<CompTractResp> getCompareTractors() {
        return compareTractors;
    }

    public StateMutableLiveData<CompTractResp> getCompare() {
        compareTractors.postLoading();
        khetiAPIs.compareTractor(tractorParam).enqueue(new Callback<CompTractResp>() {
            @Override
            public void onResponse(@NotNull Call<CompTractResp> call, @NotNull Response<CompTractResp> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().isError()) {
                            compareTractors.postSuccess(response.body());
                        } else {
                            compareTractors.postError(response.body().getMsg().get(0));
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<CompTractResp> call, @NotNull Throwable t) {
                Timber.e(t, "onFailure: ");
                compareTractors.postError(t.getMessage());
            }
        });
        return compareTractors;
    }

}
