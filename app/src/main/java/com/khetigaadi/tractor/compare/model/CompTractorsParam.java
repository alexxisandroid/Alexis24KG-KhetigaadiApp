package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CompTractorsParam {
    @SerializedName("first_tractor")
    @Expose
    private CompTractor firstTractor;
    @SerializedName("secound_tractor")
    @Expose
    private CompTractor secondTractor;
    public CompTractor getFirstTractor() {
        return firstTractor;
    }
    public void setFirstTractor(CompTractor firstTractor) {
        this.firstTractor = firstTractor;
    }
    public CompTractor getSecondTractor() {
        return secondTractor;
    }
    public void setSecondTractor(CompTractor secondTractor) {
        this.secondTractor = secondTractor;
    }
}
