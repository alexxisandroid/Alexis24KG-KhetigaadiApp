package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class NewTraSpec extends TractorSpec {
    @SerializedName("tractor_spec_data")
    @Expose
    private TractorSpecData tractorSpecData;
    public TractorSpecData getTractorSpecData() {
        return tractorSpecData;
    }
    public void setTractorSpecData(TractorSpecData tractorSpecData) {
        this.tractorSpecData = tractorSpecData;
    }
}
