package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
import com.khetigaadi.tractor.compare.util.CompareTractors;
public class CompareTractorsListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private CompareTractors data;
    @SerializedName("others")
    @Expose
    private Others others;
    public CompareTractors getData() {
        return data;
    }
    public void setData(CompareTractors data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
