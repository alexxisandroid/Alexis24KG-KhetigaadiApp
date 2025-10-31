package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
public class NewTractorPictureResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private NewTractor tractor;
    @SerializedName("others")
    @Expose
    private Others others;
    public NewTractor getTractor() {
        return tractor;
    }
    public void setTractor(NewTractor tractor) {
        this.tractor = tractor;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
