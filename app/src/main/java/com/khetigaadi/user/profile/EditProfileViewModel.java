package com.khetigaadi.user.profile;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.khetigaadi.base.util.StateMutableLiveData;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.UpdateProfileImageResp;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.model.UserResponse;
import com.khetigaadi.user.retrofit.UserAPIs;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.UUID;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import timber.log.Timber;
@HiltViewModel
public class EditProfileViewModel extends ViewModel {
    private static final String TAG = "EditProfileViewModel";
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private final UserAPIs userAPIs;
    private final SessionManager sessionManager;
    private final StateMutableLiveData<User> userLiveData;
    private final StateMutableLiveData<UserResponse> updateUserStateLiveData;
    private final StateMutableLiveData<UpdateProfileImageResp> updateImageLiveData;
    @Inject
    CompositeDisposable disposable;
    @Inject
    EditProfileViewModel(UserAPIs userAPIs,SessionManager sessionManager) {
        this.userAPIs = userAPIs;
        this.sessionManager = sessionManager;
        user.postValue(sessionManager.getUser());
        userLiveData = new StateMutableLiveData<>();
        updateUserStateLiveData = new StateMutableLiveData<>();
        updateImageLiveData = new StateMutableLiveData<>();
    }
    public MutableLiveData<User> getUser() {
        return user;
    }

    public StateMutableLiveData<User> getProfile() {
        userLiveData.postLoading();
        disposable.add(userAPIs.getUser(sessionManager.getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(@NonNull UserResponse userResponse) {
                        if (!userResponse.isError()) {
                            sessionManager.setUserOthers(userResponse.getOthers());
                            sessionManager.setUser(userResponse.getUser());
                            user.postValue(userResponse.getUser());
                        } else {
                            userLiveData.postError(""+userResponse.getMsg().get(0));
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onFailure: ");
                        userLiveData.postError(e.getMessage());
                    }
                }));

//
        return userLiveData;
       // return null;
    }
    public StateMutableLiveData<UserResponse> updateUser(User user) {
        updateUserStateLiveData.postLoading();
        disposable.add(userAPIs.updateUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserResponse>() {
                    @Override
                    public void onSuccess(@NonNull UserResponse updatedUser) {
                        if (!updatedUser.isError()) {
                            updateUserStateLiveData.postSuccess(updatedUser);
                            sessionManager.setUser(updatedUser.getUser());
                        } else {
                            updateUserStateLiveData.postError(""+updatedUser.getMsg().get(0));
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onFailure: ");
                        updateUserStateLiveData.postError(e.getMessage());
                    }
                })
        );
        return updateUserStateLiveData;
       // return null;
    }
    public StateMutableLiveData<UpdateProfileImageResp> uploadProfileImage(Uri profileImage) {
        updateImageLiveData.postLoading();
        RequestBody requestFile = RequestBody.create(new File(profileImage.getPath()),MediaType.parse("image/png"));
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("user_image",UUID.randomUUID().toString()+".png",requestFile);
        userAPIs.updateUserImage(filePart).enqueue(new Callback<UpdateProfileImageResp>() {
            @Override
            public void onResponse(@NotNull Call<UpdateProfileImageResp> call, retrofit2.@NotNull Response<UpdateProfileImageResp> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (user.getValue() != null) {
                            user.getValue().setImage(response.body().getUpdateProfileImage().getImageName());
                            sessionManager.getUser().setImage(user.getValue().getImage());
                            updateImageLiveData.postSuccess(response.body());
                        }
                    } else {
                        updateImageLiveData.postError(response.message());
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<UpdateProfileImageResp> call,@NotNull Throwable t) {
                Timber.e(t,"onFailure: ");
                updateImageLiveData.postError(t.getMessage());
            }
        });
        return updateImageLiveData;
    }
}
