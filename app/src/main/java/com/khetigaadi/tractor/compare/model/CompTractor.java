package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CompTractor {
    @SerializedName("first_tractor")
    @Expose
    private CompTractorBandModel firstTractor;
    @SerializedName("secound_tractor")
    @Expose
    private CompTractorBandModel secondTractor;
    public CompTractorBandModel getFirstTractor() {
        return firstTractor;
    }
    public void setFirstTractor(CompTractorBandModel firstTractor) {
        this.firstTractor = firstTractor;
    }
    public CompTractorBandModel getSecondTractor() {
        return secondTractor;
    }
    public void setSecondTractor(CompTractorBandModel secondTractor) {
        this.secondTractor = secondTractor;
    }
}
