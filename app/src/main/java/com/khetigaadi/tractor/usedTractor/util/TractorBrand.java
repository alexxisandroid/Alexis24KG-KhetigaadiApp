package com.khetigaadi.tractor.usedTractor.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TractorBrand {
    @SerializedName("brand_id")
    @Expose
    private Integer brand_id;

    @SerializedName("brand_logo")
    @Expose
    private String brand_logo;

    @SerializedName("brand_name")
    @Expose
    private String brand_name;


    public Integer getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(Integer brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_logo() {
        return brand_logo;
    }

    public void setBrand_logo(String brand_logo) {
        this.brand_logo = brand_logo;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }
}
