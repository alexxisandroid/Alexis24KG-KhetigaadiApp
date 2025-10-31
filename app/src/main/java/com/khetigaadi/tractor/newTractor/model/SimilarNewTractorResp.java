package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class SimilarNewTractorResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<NewTractor> data = null;
    @SerializedName("others")
    @Expose
    private NewTractorOthers others;
    public List<NewTractor> getData() {
        return data;
    }
    public void setData(List<NewTractor> data) {
        this.data = data;
    }
    public NewTractorOthers getOthers() {
        return others;
    }
    public void setOthers(NewTractorOthers others) {
        this.others = others;
    }
}
