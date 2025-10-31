package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class CompTractResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<CompTracSpecific> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<CompTracSpecific> getData() {
        return data;
    }
    public void setData(List<CompTracSpecific> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
