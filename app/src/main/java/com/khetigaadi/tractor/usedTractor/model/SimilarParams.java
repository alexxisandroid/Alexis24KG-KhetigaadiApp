package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.City;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;

public class SimilarParams extends UsedTractor {

    @SerializedName("city")
    @Expose
    private City city;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
