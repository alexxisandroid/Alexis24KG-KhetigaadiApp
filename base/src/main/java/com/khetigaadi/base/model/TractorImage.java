package com.khetigaadi.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TractorImage implements Parcelable {
    @SerializedName("tractor_id")
    private String tractor_id;

    @SerializedName("tractor_image_name")
    private String tractor_img_name;

    public String getTractor_id() {
        return tractor_id;
    }

    public void setTractor_id(String tractor_id) {
        this.tractor_id = tractor_id;
    }

    public String getTractor_img_name() {
        return tractor_img_name;
    }

    public void setTractor_img_name(String tractor_img_name) {
        this.tractor_img_name = tractor_img_name;
    }

    public TractorImage(){

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tractor_id);
        dest.writeString(this.tractor_img_name);

    }
    protected TractorImage(Parcel in) {
        this.tractor_id = in.readString();
        this.tractor_img_name = in.readString();
    }

    public static final Creator<TractorImage> CREATOR = new Creator<TractorImage>() {
        @Override
        public TractorImage createFromParcel(Parcel source) {
            return new TractorImage(source);
        }

        @Override
        public TractorImage[] newArray(int size) {
            return new TractorImage[size];
        }
    };

}
