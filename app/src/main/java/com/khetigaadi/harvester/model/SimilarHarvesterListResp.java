package com.khetigaadi.harvester.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class SimilarHarvesterListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<Harvester> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<Harvester> getData() {
        return data;
    }
    public void setData(List<Harvester> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
