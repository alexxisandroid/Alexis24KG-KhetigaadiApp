package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
public class DeliveryCharges implements Parcelable {
    protected DeliveryCharges(Parcel in) {
    }
    public static final Creator<DeliveryCharges> CREATOR = new Creator<DeliveryCharges>() {
        @Override
        public DeliveryCharges createFromParcel(Parcel in) {
            return new DeliveryCharges(in);
        }
        @Override
        public DeliveryCharges[] newArray(int size) {
            return new DeliveryCharges[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
    }
}
