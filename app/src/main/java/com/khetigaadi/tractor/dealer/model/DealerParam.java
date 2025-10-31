package com.khetigaadi.tractor.dealer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.City;

import java.util.List;

public class DealerParam {

    @SerializedName("brands")
    @Expose
    private List<Brand> brands = null;
    @SerializedName("city")
    @Expose
    private List<City> city = null;

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
    public List<City> getCity() {
        return city;
    }
    public void setCity(List<City> city) {
        this.city = city;
    }
}
