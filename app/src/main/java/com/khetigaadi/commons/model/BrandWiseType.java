package com.khetigaadi.commons.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.implement.newImplement.util.BrandTypeMaster;

import java.util.List;
public class BrandWiseType implements Parcelable {
    @SerializedName("brandwise_type")
    @Expose
    private List<BrandTypeMaster> brandTypeMasters = null;
    @Expose
    @SerializedName("typePath")
    private String typePath;
    protected BrandWiseType(Parcel in) {
        brandTypeMasters = in.createTypedArrayList(BrandTypeMaster.CREATOR);
        typePath = in.readString();
    }
    public List<BrandTypeMaster> getBrandTypeMasters() {
        return brandTypeMasters;
    }
    public void setBrandTypeMasters(List<BrandTypeMaster> brandTypeMasters) {
        this.brandTypeMasters = brandTypeMasters;
    }
    public String getTypePath() {
        return typePath;
    }
    public void setTypePath(String typePath) {
        this.typePath = typePath;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(brandTypeMasters);
        dest.writeString(typePath);
    }
    public static final Creator<BrandWiseType> CREATOR = new Creator<BrandWiseType>() {
        @Override
        public BrandWiseType createFromParcel(Parcel in) {
            return new BrandWiseType(in);
        }

        @Override
        public BrandWiseType[] newArray(int size) {
            return new BrandWiseType[size];
        }
    };
}
