package com.khetigaadi;
import com.google.firebase.messaging.FirebaseMessaging;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.FCMParam;
import com.khetigaadi.user.retrofit.UserAPIs;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
public class FcmApi {
    private static final String TAG = "FcmApi";
    public static void registerFCMToken(UserAPIs apiInterface,SessionManager sessionManager,FcmApi.FCMCallback fcmCallback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Timber.tag(TAG).w(task.getException(), "Fetching FCM registration token failed");
                        return;
                    }
                    // Get new FCM registration token
                    sessionManager.setPushToken(task.getResult());
                    callApi(apiInterface, task.getResult(), fcmCallback);
                });
    }
    private static void callApi(UserAPIs apiInterface,String token,FCMCallback fcmCallback) {
        apiInterface.registerPushToken(new FCMParam(token)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call,@NotNull Response<MyResponse> response) {
                if (fcmCallback != null)
                    fcmCallback.onComplete();
            }
            @Override
            public void onFailure(@NotNull Call<MyResponse> call,@NotNull Throwable t) {
                if (fcmCallback != null)
                    fcmCallback.onComplete();
            }
        });
    }
    public interface FCMCallback {
        void onComplete();
    }
}
