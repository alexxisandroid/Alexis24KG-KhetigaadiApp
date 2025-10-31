package com.khetigaadi.shop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

public class AddressResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private Address data;
    public Address getData() {
        return data;
    }
    public void setData(Address data) {
        this.data = data;
    }
    // public List<String> getMsg() {
    //     return msg;
    // }
    //public void setMsg(List<String> msg) {
    //    this.msg = msg;
    // }
    //private List<String> msg;
    // public boolean isError() {
    //     return error;
    // }
    // public void setError(boolean error) {
    //     this.error = error;
    //  }
    // private boolean error;
}
