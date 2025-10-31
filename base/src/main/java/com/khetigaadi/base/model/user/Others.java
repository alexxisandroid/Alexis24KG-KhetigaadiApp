package com.khetigaadi.base.model.user;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others implements Parcelable {
    @SerializedName("userImagePath")
    @Expose
    private String userPath;
    @SerializedName("testimonialsImagePath")
    @Expose
    private String testimonialsImagePath;
    @SerializedName("stateImagePath")
    @Expose
    private String stateImagePath;
    public String getUserPath() {
        return userPath;
    }
    public void setUserPath(String userPath) {
        this.userPath = userPath;
    }
    public String getTestimonialsImagePath() {
        return testimonialsImagePath;
    }
    public void setTestimonialsImagePath(String testimonialsImagePath) {
        this.testimonialsImagePath = testimonialsImagePath;
    }
    public String getStateImagePath() {
        return stateImagePath;
    }
    public void setStateImagePath(String stateImagePath) {
        this.stateImagePath = stateImagePath;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userPath);
        dest.writeString(this.testimonialsImagePath);
        dest.writeString(this.stateImagePath);
    }
    public Others() {
    }
    protected Others(Parcel in) {
        this.userPath = in.readString();
        this.testimonialsImagePath = in.readString();
        this.stateImagePath = in.readString();
    }
    public static final Parcelable.Creator<Others> CREATOR = new Parcelable.Creator<Others>() {
        @Override
        public Others createFromParcel(Parcel source) {
            return new Others(source);
        }
        @Override
        public Others[] newArray(int size) {
            return new Others[size];
        }
    };
}





//package com.khetigaadi.base.model.user;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import org.jetbrains.annotations.NotNull;
//public class Others implements Parcelable {
//    @SerializedName("userImagePath")
//    @Expose
//    private String userPath;
//    @NotNull
//    public String stateImagePath="";
//
//    public String getUserPath() {
//        return userPath;
//    }
//
//    public void setUserPath(String userPath) {
//        this.userPath = userPath;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.userPath);
//    }
//
//    public Others() {
//    }
//
//    protected Others(Parcel in) {
//        this.userPath = in.readString();
//    }
//
//    public static final Parcelable.Creator<Others> CREATOR = new Parcelable.Creator<Others>() {
//        @Override
//        public Others createFromParcel(Parcel source) {
//            return new Others(source);
//        }
//
//        @Override
//        public Others[] newArray(int size) {
//            return new Others[size];
//        }
//    };
//}
