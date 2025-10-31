package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsedTraFilter {
    @SerializedName("filter")
    @Expose
    private Filter filter;

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}

