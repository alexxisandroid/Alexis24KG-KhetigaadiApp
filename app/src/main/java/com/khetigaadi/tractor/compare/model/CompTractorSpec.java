package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.newTractor.model.TractorSpec;

import java.util.List;
public class CompTractorSpec extends TractorSpec {
    @SerializedName("compare_tractor_spec_data")
    @Expose
    private List<CompareTractorSpecDatum> compareTractorSpecData = null;
    public List<CompareTractorSpecDatum> getCompareTractorSpecData() {
        return compareTractorSpecData;
    }
    public void setCompareTractorSpecData(List<CompareTractorSpecDatum> compareTractorSpecData) {
        this.compareTractorSpecData = compareTractorSpecData;
    }
}
