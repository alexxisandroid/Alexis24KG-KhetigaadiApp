package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;

public class UsedTractorListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private UsedTractorData data;

    @SerializedName("others")
    @Expose
    private Others others;

    public UsedTractorData getData() {
        return data;
    }

    public void setData(UsedTractorData data) {
        this.data = data;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }
}
