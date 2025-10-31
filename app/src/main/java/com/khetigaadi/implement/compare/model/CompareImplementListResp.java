package com.khetigaadi.implement.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.implement.newImplement.model.Others;
public class CompareImplementListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private CompareImplement data;
    @SerializedName("others")
    @Expose
    private Others others;
    public CompareImplement getData() {
        return data;
    }
    public void setData(CompareImplement data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
