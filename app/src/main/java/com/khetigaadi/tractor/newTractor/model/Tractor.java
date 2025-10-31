package com.khetigaadi.tractor.newTractor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Tractor implements Parcelable {
    @SerializedName("tractor_id")
    private String id;

    protected Tractor(Parcel in) {
        id = in.readString();
    }

    public Tractor() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tractor> CREATOR = new Creator<Tractor>() {
        @Override
        public Tractor createFromParcel(Parcel in) {
            return new Tractor(in);
        }

        @Override
        public Tractor[] newArray(int size) {
            return new Tractor[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
