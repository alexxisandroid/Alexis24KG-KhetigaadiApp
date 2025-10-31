package com.khetigaadi.commons.utils;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.implement.newImplement.model.Others;
public class HomeListResp extends MyResponse implements Parcelable {
    @SerializedName("data")
    @Expose
    private HomeData homeData;
    @SerializedName("others")
    @Expose
    private Others others;
    protected HomeListResp(Parcel in) {
        others = in.readParcelable(Others.class.getClassLoader());
    }
    public static final Creator<HomeListResp> CREATOR = new Creator<HomeListResp>() {
        @Override
        public HomeListResp createFromParcel(Parcel in) {
            return new HomeListResp(in);
        }

        @Override
        public HomeListResp[] newArray(int size) {
            return new HomeListResp[size];
        }
    };
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    public HomeData getHomeData() {
        return homeData;
    }
    public void setHomeData(HomeData homeData) {
        this.homeData = homeData;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(others, flags);
    }

    @NonNull
    @Override
    public String toString() {
        return "HomeListResp{" +
                "homeData=" + homeData +
                ", others=" + others +
                '}';
    }
}