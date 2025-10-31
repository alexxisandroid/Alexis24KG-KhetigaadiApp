package com.khetigaadi.shop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class AddressListResp  extends MyResponse {
    public List<Address> getData() {
        return data;
    }
    public void setData(List<Address> data) {
        this.data = data;
    }
    @SerializedName("data")
    @Expose
    private List<Address> data = null;
//    public boolean isError() {
//        return error;
//    }
//    public void setError(boolean error) {
//        this.error = error;
//    }
   // private boolean error;
//    public List<String> getMsg() {
//        return msg;
//    }
//    public void setMsg(List<String> msg) {
//        this.msg = msg;
//    }
//    private List<String> msg;
}
