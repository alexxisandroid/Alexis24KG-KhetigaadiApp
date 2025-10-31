package com.khetigaadi.commons.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandImagePath implements Parcelable {

    @SerializedName("brandLogoPath")
    @Expose
    private String brandBaseUrl;

    public String getBrandBaseUrl() {
        return brandBaseUrl;
    }

    public void setBrandBaseUrl(String brandBaseUrl) {
        this.brandBaseUrl = brandBaseUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.brandBaseUrl);
    }

    public BrandImagePath() {
    }

    protected BrandImagePath(Parcel in) {
        this.brandBaseUrl = in.readString();
    }

    public static final Creator<BrandImagePath> CREATOR = new Creator<BrandImagePath>() {
        @Override
        public BrandImagePath createFromParcel(Parcel source) {
            return new BrandImagePath(source);
        }

        @Override
        public BrandImagePath[] newArray(int size) {
            return new BrandImagePath[size];
        }
    };

    @Override
    public String toString() {
        return "BrandImagePath{" +
                "brandBaseUrl='" + brandBaseUrl + '\'' +
                '}';
    }
}

