package com.khetigaadi.commons.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.implement.newImplement.util.Type;

import java.util.List;
public class TypeResp extends MyResponse implements Parcelable {
    @SerializedName(value = "data", alternate = "type")
    @Expose
    private List<Type> types = null;
    protected TypeResp(Parcel in) {
        types = in.createTypedArrayList(Type.CREATOR);
        brandWiseType = in.readParcelable(BrandWiseType.class.getClassLoader());
    }
    public static final Creator<TypeResp> CREATOR = new Creator<TypeResp>() {
        @Override
        public TypeResp createFromParcel(Parcel in) {
            return new TypeResp(in);
        }
        @Override
        public TypeResp[] newArray(int size) {
            return new TypeResp[size];
        }
    };
    public List<Type> getTypes() {
        return types;
    }
    public void setTypes(List<Type> types) {
        this.types = types;
    }
    @SerializedName("others")
    @Expose
    private BrandWiseType brandWiseType;
    public BrandWiseType getBrandWiseType() {
        return brandWiseType;
    }
    public void setBrandWiseType(BrandWiseType brandWiseType) {
        this.brandWiseType = brandWiseType;
    }
    @NonNull
    @Override
    public String toString() {
        return "TypeResp{" +
                "types=" + types +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeTypedList(types);
        dest.writeParcelable(brandWiseType, flags);
    }
}
