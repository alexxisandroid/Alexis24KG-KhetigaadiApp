package com.khetigaadi.commons.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class Brand implements Parcelable {
    @PrimaryKey
    @SerializedName("hp_id")
    @Expose
    @NonNull
    @ColumnInfo(defaultValue = "hp_id")
    private String hpId;
    @SerializedName("hp_range")
    @Expose
    @ColumnInfo(defaultValue = "hp_range")
    private String hpRange;
    @Ignore
    private boolean isSelected;
    public Brand() {
    }
    @Ignore
    public Brand(@NonNull String id,String name) {
        this.hpId = id;
        this.hpRange = name;
    }
    @NonNull
    public String getHpId() {
        return hpId;
    }
    public void setHpId(@NonNull String hpId) {
        this.hpId = hpId;
    }
    public String getHpRange() {
        return hpRange;
    }
    public void setHpRange(String hpRange) {
        this.hpRange = hpRange;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.hpId);
        dest.writeString(this.hpRange);
    }
    protected Brand(Parcel in) {
        this.hpId = in.readString();
        this.hpRange = in.readString();
    }
    public static final Creator<Hp> CREATOR = new Creator<Hp>() {
        @Override
        public Hp createFromParcel(Parcel source) {
            return new Hp(source);
        }

        @Override
        public Hp[] newArray(int size) {
            return new Hp[size];
        }
    };
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hp hp = (Hp) o;

        return hpId.equals(hp.getHpId());
    }
    @Override
    public int hashCode() {
        return hpId.hashCode();
    }

    @Override
    public String toString() {
        return "Brand{" +
                "hpId='" + hpId + '\'' +
                ", hpRange='" + hpRange + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}