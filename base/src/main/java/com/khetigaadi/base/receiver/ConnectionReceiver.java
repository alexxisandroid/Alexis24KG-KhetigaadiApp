package com.khetigaadi.base.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.khetigaadi.base.util.ConnectionUtil;


public class ConnectionReceiver extends BroadcastReceiver {

    private Listener listener;

    public ConnectionReceiver() {
    }

    public ConnectionReceiver(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        boolean status = ConnectionUtil.isConnectionAvailable(context);
        if (listener != null)
            listener.noInternet(status);
    }

    public interface Listener {
        void noInternet(boolean isConnectionAvailable);
    }
}
