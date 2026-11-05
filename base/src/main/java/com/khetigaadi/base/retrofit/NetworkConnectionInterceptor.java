package com.khetigaadi.base.retrofit;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class NetworkConnectionInterceptor implements Interceptor {
    private final Context context;
    public NetworkConnectionInterceptor(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!isConnected(context)) {
            throw new NoConnectivityException(context);
            // Throwing our custom exception 'NoConnectivityException'
        }
        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
    private Boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            //noinspection deprecation
            return nwInfo != null && nwInfo.isConnected();
        }
    }
}
//
//
//package com.khetigaadi.base.retrofit;
//import android.content.Context;
//public class NetworkConnectionInterceptor {
//    public Context context;
//    public NetworkConnectionInterceptor(Context context) {
//        this.context = context;
//    }
//}
