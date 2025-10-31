package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;

public class UsedTractorResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private UsedTractor data;

    @SerializedName("others")
    @Expose
    private Others others;

    public UsedTractor getData() {
        return data;
    }

    public void setData(UsedTractor data) {
        this.data = data;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }
}
