package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Taluka implements Parcelable {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    private int id;
    @SerializedName("city_id")
    private int cityId;
    @SerializedName("name")
    @Expose
    private String name;

    public Taluka() {
    }
    @Ignore
    protected Taluka(Parcel in) {
        id = in.readInt();
        cityId = in.readInt();
        name = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(cityId);
        dest.writeString(name);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Taluka> CREATOR = new Creator<Taluka>() {
        @Override
        public Taluka createFromParcel(Parcel in) {
            return new Taluka(in);
        }
        @Override
        public Taluka[] newArray(int size) {
            return new Taluka[size];
        }
    };
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
