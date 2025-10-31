package com.khetigaadi.implement.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.implement.newImplement.model.Others;
import com.khetigaadi.implement.newImplement.util.NewImp;

import java.util.List;
public class ImplementDetailsResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<NewImp> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<NewImp> getData() {
        return data;
    }
    public void setData(List<NewImp> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
