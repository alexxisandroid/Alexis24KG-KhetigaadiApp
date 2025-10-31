package com.khetigaadi.tractor.usedTractor.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;

public class UsedTractors extends MyResponse {
    @SerializedName(value = "data", alternate = {"old_tractor"})
    @Expose
    private List<UsedTractor> tractors = null;

    public List<UsedTractor> getTractors() {
        return tractors;
    }

    public void setTractors(List<UsedTractor> tractors) {
        this.tractors = tractors;
    }

}
