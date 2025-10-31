package com.khetigaadi.implement.newImplement.util;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class BrandTypeMaster implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("type_id")
    @Expose
    private int typeId;
    @SerializedName(value = "brand_id")
    @Expose
    private int brandId;
    public int getId() {
        return id;
    }
    public int getBrandId() {
        return brandId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }
    public int getTypeId() {
        return typeId;
    }
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
    @NonNull
    @Override
    public String toString() {
        return "BrandTypeMaster{" +
                ", brandId=" + brandId +
                ", typeId=" + typeId +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // dest.writeInt(this.id);
        dest.writeInt(this.brandId);
        dest.writeInt(this.typeId);
        //   dest.writeParcelable(this.implementType, flags);
    }
    public BrandTypeMaster() {
    }
    protected BrandTypeMaster(Parcel in) {
        //this.id = in.readInt();
        this.brandId = in.readInt();
        this.typeId = in.readInt();
        // this.implementType = in.readParcelable(Type.class.getClassLoader());
    }
    public static final Parcelable.Creator<BrandTypeMaster> CREATOR = new Parcelable.Creator<BrandTypeMaster>() {
        @Override
        public BrandTypeMaster createFromParcel(Parcel source) {
            return new BrandTypeMaster(source);
        }
        @Override
        public BrandTypeMaster[] newArray(int size) {
            return new BrandTypeMaster[size];
        }
    };
}
