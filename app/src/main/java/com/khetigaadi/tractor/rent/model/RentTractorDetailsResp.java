package com.khetigaadi.tractor.rent.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class RentTractorDetailsResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private RentTractor data;
    @SerializedName("others")
    @Expose
    private Others others;
    public RentTractor getData() {
        return data;
    }
    public void setData(RentTractor data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
