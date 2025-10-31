package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class CompTractData {
    @SerializedName("first_tractor")
    @Expose
    private List<CompTractTractor> firstTractor = null;
    @SerializedName("secound_tractor")
    @Expose
    private List<CompTractTractor> secondTractor = null;
    public List<CompTractTractor> getFirstTractor() {
        return firstTractor;
    }
    public void setFirstTractor(List<CompTractTractor> firstTractor) {
        this.firstTractor = firstTractor;
    }
    public List<CompTractTractor> getSecondTractor() {
        return secondTractor;
    }
    public void setSecondTractor(List<CompTractTractor> secondTractor) {
        this.secondTractor = secondTractor;
    }
}
