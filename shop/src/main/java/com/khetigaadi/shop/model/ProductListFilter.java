package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ProductListFilter implements Parcelable {
    @SerializedName("filter")
    @Expose
    private Filter filter;
    @SerializedName("perPage")
    @Expose
    private int perPage = 10;
    public Filter getFilter() {
        return filter;
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    public int getPerPage() {
        return perPage;
    }
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
    @NonNull
    @Override
    public String toString() {
        return "ProductListFilter{" +
                "filter=" + filter +
                ", perPage=" + perPage +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.filter, flags);
        dest.writeInt(this.perPage);
    }
    public ProductListFilter() {
    }
    protected ProductListFilter(Parcel in) {
        this.filter = in.readParcelable(Filter.class.getClassLoader());
        this.perPage = in.readInt();
    }
    public static final Creator<ProductListFilter> CREATOR = new Creator<ProductListFilter>() {
        @Override
        public ProductListFilter createFromParcel(Parcel source) {
            return new ProductListFilter(source);
        }
        @Override
        public ProductListFilter[] newArray(int size) {
            return new ProductListFilter[size];
        }
    };
}
