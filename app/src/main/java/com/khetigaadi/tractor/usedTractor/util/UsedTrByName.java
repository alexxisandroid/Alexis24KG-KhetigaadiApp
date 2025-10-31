package com.khetigaadi.tractor.usedTractor.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarads8 on 10-Nov-17.
 */

public class UsedTrByName extends MyResponse {
    @SerializedName("data")
    @Expose
    public List<SearchTrByName> tractors;

    public List<SearchTrByName> getTractors() {
        return tractors;
    }

    public void setTractors(ArrayList<SearchTrByName> tractors) {
        this.tractors = tractors;
    }
}
