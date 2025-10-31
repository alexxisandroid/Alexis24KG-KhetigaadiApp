package com.khetigaadi.harvester.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class HarvesterResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private Harvester data;
    @SerializedName("others")
    @Expose
    private Others others;
    public Harvester getData() {
        return data;
    }
    public void setData(Harvester data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
