package com.khetigaadi.shop.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;

import java.util.List;
public class Orders extends ListData {
    @SerializedName("data")
    @Expose
    private List<Order> data = null;

    protected Orders(Parcel in) {
        super(in);
    }

    public List<Order> getData() {
        return data;
    }
    public void setData(List<Order> data) {
        this.data = data;
    }
}
