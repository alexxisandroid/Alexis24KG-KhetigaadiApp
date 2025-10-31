package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Price implements Parcelable {
    @SerializedName("price_to")
    @Expose
    private int priceTo;
    @SerializedName("price_from")
    @Expose
    private int priceFrom;
    public Price(int priceFrom, int priceTo) {
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
    }
    public int getPriceTo() {
        return priceTo;
    }
    public void setPriceTo(int priceTo) {
        this.priceTo = priceTo;
    }
    public int getPriceFrom() {
        return priceFrom;
    }
    public void setPriceFrom(int priceFrom) {
        this.priceFrom = priceFrom;
    }
    @NonNull
    @Override
    public String toString() {
        return "Price{" +
                "priceTo=" + priceTo +
                ", priceFrom=" + priceFrom +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeInt(this.priceTo);
        dest.writeInt(this.priceFrom);
    }
    public Price() {
    }
    protected Price(Parcel in) {
        this.priceTo = in.readInt();
        this.priceFrom = in.readInt();
    }
    public static final Parcelable.Creator<Price> CREATOR = new Parcelable.Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel source) {
            return new Price(source);
        }
        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };
}
