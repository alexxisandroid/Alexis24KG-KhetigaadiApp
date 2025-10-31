package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class NewTractorResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private NewTractorData data;
    @SerializedName("others")
    @Expose
    private NewTractorOthers others;
    public NewTractorData getData() {
        return data;
    }
    public void setData(NewTractorData data) {
        this.data = data;
    }
    public NewTractorOthers getOthers() {
        return others;
    }
    public void setOthers(NewTractorOthers others) {
        this.others = others;
    }
}
