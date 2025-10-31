package com.khetigaadi.tractor.rent.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
public class RentTractorResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private RentTractorData data;
    @SerializedName("others")
    @Expose
    private Others others;
    public RentTractorData getData() {
        return data;
    }
    public void setData(RentTractorData data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
