package com.khetigaadi.dagger.module;
import android.content.Context;
import android.text.TextUtils;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.khetigaadi.AppConfig;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.base.retrofit.NetworkConnectionInterceptor;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.retrofit.UserAPIs;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {
    private static final String TAG = "RetrofitModule";
    @Provides
    KhetiAPIs getKhetiApis(Retrofit retroFit) {
        return retroFit.create(KhetiAPIs.class);
    }
    @Provides
    UserAPIs getUserAPIs(Retrofit retroFit) {
        return retroFit.create(UserAPIs.class);
    }
    @Provides
    ShopAPIs getShopAPIs(Retrofit retroFit) {
        return retroFit.create(ShopAPIs.class);
    }
    @Provides
    Retrofit getRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
               // .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }
    @Provides
    @Singleton
    OkHttpClient getOkHttpClient(@ApplicationContext Context context,SessionManager sessionManager) {
        return new OkHttpClient.Builder()
                .addInterceptor(getHeaderInterceptor(sessionManager))
                .addInterceptor(getHttpLoggingInterceptor())
                .addInterceptor((Interceptor) new NetworkConnectionInterceptor(context))
                .addInterceptor(new ChuckerInterceptor(context))
                .authenticator(new TokenAuthenticator(context))
                .build();
    }
    Interceptor getHeaderInterceptor(SessionManager sessionManager) {
        return chain -> {
            Request original = chain.request();
            String isAuthorizable = original.headers().get("isAuthorizable");
            Request request;
            if (TextUtils.isEmpty(isAuthorizable))
                request = original.newBuilder()
                        .header("Content-Type", "application/json; charset=utf-8")
                        .header("Accept", "application/json")
                        .header("AppType", String.valueOf(BuildConfig.APP_TYPE))
                        .header("Authorization", sessionManager.getToken() == null ? " " : sessionManager.getToken())
                        .header("userId", sessionManager.getUser() == null ? " " : String.valueOf(sessionManager.getUser().getId()))
                        .method(original.method(), original.body())
                        .build();
            else
                request = original.newBuilder().build();
            return chain.proceed(request);
        };
    }
    HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }
}
