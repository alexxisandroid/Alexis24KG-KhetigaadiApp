package com.khetigaadi.commons.utils;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
public class Year implements Parcelable {
    private int id;
    private String name;
    private boolean isSelected;
    public Year(int id, String name, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.isSelected = isSelected;
    }
    public Year(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @NonNull
    @Override
    public String toString() {
        return "Year{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", isSelected=" + isSelected +
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
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }
    protected Year(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }
    public static final Parcelable.Creator<Year> CREATOR = new Parcelable.Creator<Year>() {
        @Override
        public Year createFromParcel(Parcel source) {
            return new Year(source);
        }
        @Override
        public Year[] newArray(int size) {
            return new Year[size];
        }
    };
}
