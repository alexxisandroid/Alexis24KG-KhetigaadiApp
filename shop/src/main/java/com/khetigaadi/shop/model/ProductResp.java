package com.khetigaadi.shop.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class ProductResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private Product data;
    @SerializedName("others")
    @Expose
    private Others others;
//    @SerializedName("error")
//    @Expose
//    private String error;
//    @SerializedName("msg")
//    @Expose
//    private String msg;
    public Product getData() {
        return data;
    }
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
    public void setData(Product data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
