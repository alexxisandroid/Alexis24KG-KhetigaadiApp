package com.khetigaadi.user.retrofit;
import androidx.lifecycle.LiveData;
import com.khetigaadi.base.model.CityResp;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.model.StateResp;
import com.khetigaadi.base.model.TalukaResp;
import com.khetigaadi.base.model.agri.Crops;
import com.khetigaadi.base.room.repository.utils.ApiResponse;
import com.khetigaadi.user.model.FCMParam;
import com.khetigaadi.user.model.OtpResp;
import com.khetigaadi.user.model.UpdateProfileImageResp;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.model.UserResponse;
import java.util.Map;
//import io.reactivex.Single;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import com.khetigaadi.user.model.UserTypes;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
public interface UserAPIs {
 @POST("mobile/api/v1/fcm-token")
 Call<MyResponse> registerPushToken(@Body FCMParam fcmParam);
 @DELETE("mobile/api/v1/fcm-token")
 Call<MyResponse> deregisterPushToken();
 @PUT("mobile/api/v3/user/profile")
 Single<UserResponse> updateUser(@Body User user);
 @POST("mobile/api/v1/user/otp")
 Call<OtpResp> getOTP(@Body User user);
 @POST("mobile/api/v2/user/login")
 Call<UserResponse> loginWithOTP(@Body User user);
 @GET("mobile/api/v2/user/profile/{id}")
 Single<UserResponse> getUser(@Path("id") String id);
 @Multipart
 @POST("mobile/api/v1/uploadUserImage")
 Call<UpdateProfileImageResp> updateUserImage(@Part MultipartBody.Part image);
 @GET("commonApi/talukas/{id}")
 Single<TalukaResp> getTalukas(@Path("id") int id);
 @GET("mobile/api/v1/crops")
 Single<Crops> getCrops();
 @GET("commonApi/state")
 @NonNull
 Single<StateResp> getStates();
 @GET("commonApi/city")
 LiveData<ApiResponse<CityResp>> getCities();
 @GET("commonApi/state/city/{id}")
 Single<CityResp> getCitiesByState(@Path("id") int stateId);
 @POST("mobile/api/v1/user/share")
 Single<MyResponse> shareUser(@Body Map<String, String> params);
 @POST("mobile/api/v1/user/follow")
 Single<MyResponse> followUser(@Body Map<String, String> params);
 //followUser
 @POST("mobile/api/v1/user/like")
 Single<MyResponse> likeUser(@Body User params);
 @GET("mobile/api/v1/userLevel")
 Single<UserTypes> getUserType();
}


//public interface UserAPIs {
//      // @GET("/mobile/api/v1/banner")
//      // Single<List<State>> states();
//       @GET("mobile/api/v1/cities")
//       LiveData<ApiResponse<CityResp>> cities();
//       //states
//       @GET("mobile/api/v1/states")
//       LiveData<ApiResponse<StateResp>> states();
//       @NotNull
//       void shareUser(Map<String,String> params);
//       @NotNull
//       Single<User> getUser(@Query("paramUserId") String paramUserId);
//        @NotNull
//        Single<Completable> updateUser(User userLoggedIn);
//        @NotNull
//        Single<User> followUser(Map<String,String> params);
//        @NotNull
//        Single<User> likeUser(@Body User secondUser);
//        @NotNull
//        Call<UpdateProfileImageResp> updateUserImage(MultipartBody.Part filePart);
//        @GET("/mobile/api/v1/banner")
//        Single<User> getUserType();
//       Call<OtpResp> getOTP(User userParam);
//        Call<UserResponse> loginWithOTP(User userParam);
//        Call<MyResponse> registerPushToken(FCMParam fcmParam);
//        @NotNull
//        Call<MyResponse> deregisterPushToken();
//        @NotNull
//        LiveData<ApiResponse<TalukaResp>> getTalukas(int cityId);
//        Single<Crops> getCrops();
//        @NotNull
//        LiveData<ApiResponse<CityResp>> getCitiesByState(int stateId);
//}
