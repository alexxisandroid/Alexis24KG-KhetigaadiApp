package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;

import java.util.List;
public class NewTractorOverviewResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<NewTractor> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<NewTractor> getData() {
        return data;
    }
    public void setData(List<NewTractor> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
