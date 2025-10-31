package com.khetigaadi.user.buyserEnquiry.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
import com.khetigaadi.user.util.BuyersEnq;

import java.util.List;
public class InterestedBuyersResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<BuyersEnq> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<BuyersEnq> getData() {
        return data;
    }
    public void setData(List<BuyersEnq> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
