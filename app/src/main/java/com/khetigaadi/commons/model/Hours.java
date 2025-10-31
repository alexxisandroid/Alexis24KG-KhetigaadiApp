package com.khetigaadi.commons.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Hours implements Parcelable {
    private final int id;
    private final String name;

    public Hours(int id, String name) {

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected Hours(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Hours> CREATOR = new Parcelable.Creator<Hours>() {
        @Override
        public Hours createFromParcel(Parcel source) {
            return new Hours(source);
        }

        @Override
        public Hours[] newArray(int size) {
            return new Hours[size];
        }
    };

    @Override
    public String toString() {
        return "Hours{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
