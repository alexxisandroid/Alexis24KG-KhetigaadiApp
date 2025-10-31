package com.khetigaadi.tractor.rent.rentTractorNewUi.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
public class RentTractorUploadResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private RentTractorUploadData data;
    @SerializedName("others")
    @Expose
    private Others others;
    public RentTractorUploadData getData() {
        return data;
    }
    public void setData(RentTractorUploadData data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
