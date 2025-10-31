package com.khetigaadi.tractor.rent.rentTractorNewUi.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class RentTractorImplement implements Parcelable {
    @SerializedName("rent_type_id")
    @Expose
    private int rentTypeId;
    @SerializedName("rent_tractor_id")
    @Expose
    private int rentTractorId;
    @SerializedName("implement_type_id")
    @Expose
    private int implementTypeId;
    @SerializedName("unit_id")
    @Expose
    private int unitId;
    @SerializedName("rate")
    @Expose
    private int rate;
    @SerializedName("unit")
    @Expose
    private RentUnit unit;
    @SerializedName("image")
    private String image;
    @SerializedName("implement_type")
    @Expose
    private ImplementType implementType;

    protected RentTractorImplement(Parcel in) {
        rentTypeId = in.readInt();
        rentTractorId = in.readInt();
        implementTypeId = in.readInt();
        unitId = in.readInt();
        rate = in.readInt();
        unit = in.readParcelable(RentUnit.class.getClassLoader());
        image = in.readString();
        implementType = in.readParcelable(ImplementType.class.getClassLoader());
    }

    public static final Creator<RentTractorImplement> CREATOR = new Creator<RentTractorImplement>() {
        @Override
        public RentTractorImplement createFromParcel(Parcel in) {
            return new RentTractorImplement(in);
        }

        @Override
        public RentTractorImplement[] newArray(int size) {
            return new RentTractorImplement[size];
        }
    };

    public int getRentTypeId() {
        return rentTypeId;
    }
    public void setRentTypeId(int rentTypeId) {
        this.rentTypeId = rentTypeId;
    }
    public int getRentTractorId() {
        return rentTractorId;
    }
    public void setRentTractorId(int rentTractorId) {
        this.rentTractorId = rentTractorId;
    }
    public int getImplementTypeId() {
        return implementTypeId;
    }
    public void setImplementTypeId(int implementTypeId) {
        this.implementTypeId = implementTypeId;
    }
    public int getUnitId() {
        return unitId;
    }
    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getRate() {
        return rate;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    public RentUnit getUnit() {
        return unit;
    }
    public void setUnit(RentUnit unit) {
        this.unit = unit;
    }
    public ImplementType getImplementType() {
        return implementType;
    }
    public void setImplementType(ImplementType implementType) {
        this.implementType = implementType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(rentTypeId);
        dest.writeInt(rentTractorId);
        dest.writeInt(implementTypeId);
        dest.writeInt(unitId);
        dest.writeInt(rate);
        dest.writeParcelable(unit, flags);
        dest.writeString(image);
        dest.writeParcelable(implementType, flags);
    }
}
