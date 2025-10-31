package com.khetigaadi.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others extends com.khetigaadi.shop.model.Others implements Parcelable {
    @SerializedName("brandLogoPath")
    @Expose
    private String brandLogoPath;
    @SerializedName(value = "tractorImagePath", alternate = {"usedTractorImagePath","newTractorPath","newTractorImagePath","oldTractorPath"})
    @Expose
    private String tractorImagePath;
    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;
    public String getDisclaimer() {
        return disclaimer;
    }
    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
    public String getBrandLogoPath() {
        return brandLogoPath;
    }
    public void setBrandLogoPath(String brandLogoPath) {
        this.brandLogoPath = brandLogoPath;
    }
    public String getTractorImagePath() {
        return tractorImagePath;
    }
    public void setTractorImagePath(String tractorImagePath) {
        this.tractorImagePath = tractorImagePath;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brandLogoPath);
        dest.writeString(this.tractorImagePath);
    }
    public Others() {
    }
    protected Others(Parcel in) {
        this.brandLogoPath = in.readString();
        this.tractorImagePath = in.readString();
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
    public void setShopCategoryImagePath(String stringValue) {
    }
    public void setShopProductImagePath(String stringValue) {
    }
}

