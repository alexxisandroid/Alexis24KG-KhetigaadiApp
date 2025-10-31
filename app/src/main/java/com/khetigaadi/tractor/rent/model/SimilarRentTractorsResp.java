package com.khetigaadi.tractor.rent.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class SimilarRentTractorsResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<RentTractor> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<RentTractor> getData() {
        return data;
    }
    public void setData(List<RentTractor> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
