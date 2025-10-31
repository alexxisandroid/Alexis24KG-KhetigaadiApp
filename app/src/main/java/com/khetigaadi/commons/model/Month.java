package com.khetigaadi.commons.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
public class Month implements Parcelable {
    private final int id;
    private final String name;
    public Month(int id,String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    @NonNull
    @Override
    public String toString() {
        return "Month{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
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
    protected Month(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }
    public static final Parcelable.Creator<Month> CREATOR = new Parcelable.Creator<Month>() {
        @Override
        public Month createFromParcel(Parcel source) {
            return new Month(source);
        }
        @Override
        public Month[] newArray(int size) {
            return new Month[size];
        }
    };
}
