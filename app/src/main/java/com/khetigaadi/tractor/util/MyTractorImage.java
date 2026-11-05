package com.khetigaadi.tractor.util;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;
@Entity
public class MyTractorImage implements Parcelable {
    @SerializedName("id")
    private String tractor_id;
    protected MyTractorImage(Parcel in) {
        tractor_id = in.readString();
        tractorOwnerId = in.readString();
        name = in.readString();
    }
    public static final Creator<MyTractorImage> CREATOR = new Creator<MyTractorImage>() {
        @Override
        public MyTractorImage createFromParcel(Parcel in) {
            return new MyTractorImage(in);
        }
        @Override
        public MyTractorImage[] newArray(int size) {
            return new MyTractorImage[size];
        }
    };

    public MyTractorImage() {

    }

    public String getTractor_id() {
        return tractor_id;
    }
    public void setTractor_id(String tractor_id) {
        this.tractor_id = tractor_id;
    }
    public String getTractorOwnerId() {
        return tractorOwnerId;
    }
    public void setTractorOwnerId(String tractorOwnerId) {
        this.tractorOwnerId = tractorOwnerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @SerializedName("tractor_owner_id")
    private String tractorOwnerId;
    @SerializedName("name")
    private String name;
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(tractor_id);
        dest.writeString(tractorOwnerId);
        dest.writeString(name);
    }
    @Override
    public String toString() {
        return "MyTractorImage{" +
                "tractor_id='" + tractor_id + '\'' +
                ", tractorOwnerId='" + tractorOwnerId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
