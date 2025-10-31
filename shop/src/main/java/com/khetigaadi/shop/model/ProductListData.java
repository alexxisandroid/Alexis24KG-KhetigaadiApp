package com.khetigaadi.shop.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;

import java.util.List;
public class ProductListData extends ListData {
    @SerializedName("data")
    @Expose
    private List<Product> data = null;

    protected ProductListData(Parcel in) {
        super(in);
    }

    public List<Product> getData() {
        return data;
    }
    public void setData(List<Product> data) {
        this.data = data;
    }
}
