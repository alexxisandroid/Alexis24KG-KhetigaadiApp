package com.khetigaadi.tractor.rent.rentTractorNewUi.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class RentUnit implements Parcelable {
    @SerializedName("unit_id")
    @ColumnInfo(defaultValue = "id")
    //@Expose
    private int unitId;
    @SerializedName("unit_name")
    @ColumnInfo(defaultValue = "unit_name")
   // @Expose
    private String unitName;
    public RentUnit() {
    }
    protected RentUnit(Parcel in) {
        unitId = in.readInt();
        unitName = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(unitId);
        dest.writeString(unitName);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<RentUnit> CREATOR = new Creator<RentUnit>() {
        @Override
        public RentUnit createFromParcel(Parcel in) {
            return new RentUnit(in);
        }
        @Override
        public RentUnit[] newArray(int size) {
            return new RentUnit[size];
        }
    };
    public int getUnitId() {
        return unitId;
    }
    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
