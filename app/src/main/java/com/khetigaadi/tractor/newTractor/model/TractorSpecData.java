package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class TractorSpecData {
    @SerializedName("data_id")
    @Expose
    private int dataId;
    @SerializedName("tractor_spec_id")
    @Expose
    private int tractorSpecId;
    @SerializedName("spec_data")
    @Expose
    private String specData;
    @SerializedName("tractor_id")
    @Expose
    private int tractorId;
    public int getDataId() {
        return dataId;
    }
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }
    public int getTractorSpecId() {
        return tractorSpecId;
    }
    public void setTractorSpecId(int tractorSpecId) {
        this.tractorSpecId = tractorSpecId;
    }
    public String getSpecData() {
        return specData;
    }
    public void setSpecData(String specData) {
        this.specData = specData;
    }
    public int getTractorId() {
        return tractorId;
    }
    public void setTractorId(int tractorId) {
        this.tractorId = tractorId;
    }
}
