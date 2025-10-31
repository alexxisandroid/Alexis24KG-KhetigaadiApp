package com.khetigaadi.shop.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

public class OrderResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private Orders data = null;
    @SerializedName("others")
    @Expose
    private Others others;

    public Orders getData() {
        return data;
    }
    public void setData(Orders data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    @NonNull
    @Override
    public String toString() {
        return "MyOrderResp{" +
                "data=" + data +
                '}';
    }
}
