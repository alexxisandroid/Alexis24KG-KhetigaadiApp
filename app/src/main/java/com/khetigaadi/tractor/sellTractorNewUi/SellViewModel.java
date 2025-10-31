package com.khetigaadi.tractor.sellTractorNewUi;
import android.net.Uri;

import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.UsedTractorRepository;
import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorResp;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
@HiltViewModel
public class SellViewModel extends ViewModel {
    private static final String TAG = "SellViewModel";
    private UsedTractor usedTractor = new UsedTractor();
    TractorImages images = new TractorImages();
    private final KhetiAPIs khetiAPIs;
    UsedTractorRepository usedTractorRepository;
    private StateMutableLiveData<UsedTractorResp> uploadTractorLiveData;
    @Inject
    SellViewModel(KhetiAPIs khetiAPIs,UsedTractorRepository usedTractorRepository,SavedStateHandle savedStateHandle) {
        this.khetiAPIs = khetiAPIs;
        this.usedTractorRepository = usedTractorRepository;
    }
    public UsedTractor getUsedTractor() {
        return usedTractor;
    }
    public void setUsedTractor(UsedTractor usedTractor) {
        this.usedTractor = usedTractor;
    }
    public void setImage1(Uri image1) {
        images.setRightImage(image1);
    }
    public TractorImages getImages() {
        return images;
    }
    public void setImage2(Uri image) {
        images.setFrontSideImage(image);
    }
    public void setImage3(Uri image) {
        images.setLeftImage(image);
    }
    public void setImage4(Uri image) {
        images.setBackSideImage(image);
    }
    public void setImage5(Uri image) {
        images.setFrontTyreImage(image);
    }
    public void setImage6(Uri image) {
        images.setBackTyreImage(image);
    }
    public StateMutableLiveData<UsedTractorResp> interTractor() {
        uploadTractorLiveData = new StateMutableLiveData<>();
        uploadTractorLiveData.postLoading();
        khetiAPIs.insertTractor(usedTractor).enqueue(new Callback<UsedTractorResp>() {
            @Override
            public void onResponse(@NotNull Call<UsedTractorResp> call, @NotNull Response<UsedTractorResp> response) {
                if (response.isSuccessful())
                    if (response.body() != null) {
                        if (!response.body().isError())
                            uploadTractorLiveData.postSuccess(response.body());
                        else uploadTractorLiveData.postError(response.body().getMsg().get(0));
                    } else
                        uploadTractorLiveData.postError(response.message());
            }
            @Override
            public void onFailure(@NotNull Call<UsedTractorResp> call, @NotNull Throwable t) {
                Timber.e("onFailure: %s", t.getMessage());
                uploadTractorLiveData.postError(t.getMessage());
            }
        });
        return uploadTractorLiveData;
    }
}
