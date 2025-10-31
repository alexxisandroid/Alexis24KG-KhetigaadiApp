package com.khetigaadi.tractor.newTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.model.Others;

import java.util.List;

public class NewTractorOthers extends Others {
    @SerializedName("new_tractor_sponsored")
    @Expose
    private List<NewTractorSponsored> newTractorSponsored = null;
    public List<NewTractorSponsored> getNewTractorSponsored() {
        return newTractorSponsored;
    }
    public void setNewTractorSponsored(List<NewTractorSponsored> newTractorSponsored) {
        this.newTractorSponsored = newTractorSponsored;
    }
}
