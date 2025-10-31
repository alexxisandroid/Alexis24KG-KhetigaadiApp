package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GetShortSpec extends CompareTractorSpecDatum {
    @SerializedName("tractor_spec")
    @Expose
    private TractorSpec tractorSpec;
    public TractorSpec getTractorSpec() {
        return tractorSpec;
    }
    public void setTractorSpec(TractorSpec tractorSpec) {
        this.tractorSpec = tractorSpec;
    }
}
