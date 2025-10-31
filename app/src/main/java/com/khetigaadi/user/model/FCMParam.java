package com.khetigaadi.user.model;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class FCMParam {
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType = "android";
    @SerializedName("notification_type")
    @Expose
    private String notificationType = "KhetiGaadi";
    public FCMParam(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    @NonNull
    @Override
    public String toString() {
        return "FCMParam{" +
                "fcmToken='" + fcmToken + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", notificationType='" + notificationType + '\'' +
                '}';
    }
}
