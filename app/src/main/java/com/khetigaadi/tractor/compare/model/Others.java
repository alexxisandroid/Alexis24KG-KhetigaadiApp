package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.newTractor.model.NewTractor;

import java.util.List;
public class Others extends com.khetigaadi.model.Others {
    @SerializedName("tractor_data")
    @Expose
    private List<NewTractor> tractorData = null;
    public List<NewTractor> getTractorData() {
        return tractorData;
    }
    public void setTractorData(List<NewTractor> tractorData) {
        this.tractorData = tractorData;
    }
}
