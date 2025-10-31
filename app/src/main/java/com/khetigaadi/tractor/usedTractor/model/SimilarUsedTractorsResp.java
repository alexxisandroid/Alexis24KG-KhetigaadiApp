package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.tractor.newTractor.model.NewTractorOthers;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;

import java.util.List;

public class SimilarUsedTractorsResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<UsedTractor> data = null;

    @SerializedName("others")
    @Expose
    private NewTractorOthers others;

    public List<UsedTractor> getData() {
        return data;
    }

    public void setData(List<UsedTractor> data) {
        this.data = data;
    }

    public NewTractorOthers getOthers() {
        return others;
    }

    public void setOthers(NewTractorOthers others) {
        this.others = others;
    }
}


