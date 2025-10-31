package com.khetigaadi.weather.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ConditionImage implements Parcelable {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("code")
    @Expose
    private int code;
    protected ConditionImage(Parcel in) {
        text = in.readString();
        icon = in.readString();
        code = in.readInt();
    }
    public static final Creator<ConditionImage> CREATOR = new Creator<ConditionImage>() {
        @Override
        public ConditionImage createFromParcel(Parcel in) {
            return new ConditionImage(in);
        }
        @Override
        public ConditionImage[] newArray(int size) {
            return new ConditionImage[size];
        }
    };
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(icon);
        dest.writeInt(code);
    }
}
