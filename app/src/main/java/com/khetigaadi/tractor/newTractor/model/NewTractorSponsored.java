package com.khetigaadi.tractor.newTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewTractorSponsored {
    @SerializedName("tractor_sponsor_id")
    @Expose
    private int tractorSponsorId;
    @SerializedName("new_tractor")
    @Expose
    private NewTractor newTractor;

    public int getTractorSponsorId() {
        return tractorSponsorId;
    }

    public void setTractorSponsorId(int tractorSponsorId) {
        this.tractorSponsorId = tractorSponsorId;
    }

    public NewTractor getNewTractor() {
        return newTractor;
    }

    public void setNewTractor(NewTractor newTractor) {
        this.newTractor = newTractor;
    }
}
