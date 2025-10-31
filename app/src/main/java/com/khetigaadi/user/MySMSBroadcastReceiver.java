package com.khetigaadi.user;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import timber.log.Timber;
public class MySMSBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MySMSBroadcastReceiver";
    private OTPReceiveListener otpReceiver = null;
    public void initOTPListener(OTPReceiveListener receiver) {
        this.otpReceiver = receiver;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    try {
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        Timber.e("onReceive: " + message);
                        if (otpReceiver != null)
                            otpReceiver.onOTPReceived(((message.split(" "))[0]).replace("<#>", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CommonStatusCodes.TIMEOUT:
                    // Waiting for SMS timed out (5 minutes)
                    // Handle the error ...
                    if (otpReceiver != null)
                        otpReceiver.onOTPTimeOut();
                    break;
            }
        }
    }
    public interface OTPReceiveListener {
        void onOTPReceived(String otp);
        void onOTPTimeOut();
    }
}