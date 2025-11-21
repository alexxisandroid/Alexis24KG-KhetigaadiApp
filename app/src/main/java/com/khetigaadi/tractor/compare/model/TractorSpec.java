package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class TractorSpec {
    @SerializedName("data_id")
    @Expose
    private int dataId;
    @SerializedName("tractor_spec_id")
    @Expose
    private String tractorSpecId;
    @SerializedName("spec_data")
    @Expose
    private String specData;
    @SerializedName("tractor_id")
    @Expose
    private int tractorId;
    @SerializedName("tractor_spec")
    @Expose
    private TractorShortSpec tractorSpec;
    @SerializedName("tractor_unit")
    @Expose
    private TractorUnit tractorUnit;
    public TractorUnit getTractorUnit() {
        return tractorUnit;
    }

    public void setTractorUnit(TractorUnit tractorUnit) {
        this.tractorUnit = tractorUnit;
    }


    public int getDataId() {
        return dataId;
    }
    public void setDataId(int dataId) {
        this.dataId = dataId;
    }
    public String getTractorSpecId() {
        return tractorSpecId;
    }
    public void setTractorSpecId(String tractorSpecId) {
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
    public TractorShortSpec getTractorSpec() {
        return tractorSpec;
    }
    public void setTractorSpec(TractorShortSpec tractorSpec) {
        this.tractorSpec = tractorSpec;
    }
}
