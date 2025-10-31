package com.khetigaadi.user.myUsedTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
import com.khetigaadi.user.util.MyUsedTractor;

import java.util.List;
public class MyUsedTractorResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<MyUsedTractor> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<MyUsedTractor> getData() {
        return data;
    }
    public void setData(List<MyUsedTractor> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
