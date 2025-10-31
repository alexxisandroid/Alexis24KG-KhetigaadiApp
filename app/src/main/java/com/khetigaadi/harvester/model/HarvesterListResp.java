package com.khetigaadi.harvester.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

public class HarvesterListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private HarvesterData data;

    @SerializedName("others")
    @Expose
    private Others others;

    public HarvesterData getData() {
        return data;
    }

    public void setData(HarvesterData data) {
        this.data = data;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }
}
