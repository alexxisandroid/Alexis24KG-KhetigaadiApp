package com.khetigaadi.base.retrofit;
import android.content.Context;
import com.khetigaadi.base.R;
import java.io.IOException;
public class NoConnectivityException extends IOException {
    private final Context context;
    public NoConnectivityException(Context context) {

        this.context = context;
    }
    @Override
    public String getMessage() {
        return context.getString(R.string.no_internet_connection);
        // You can send any message whatever you want from here.
    }
}
