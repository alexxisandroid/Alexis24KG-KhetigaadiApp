package com.khetigaadi.tractor.dealer.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
public class DealerListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private DealerListData data;
    @SerializedName("others")
    @Expose
    private Others others;
    public DealerListData getData() {
        return data;
    }
    public void setData(DealerListData data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
