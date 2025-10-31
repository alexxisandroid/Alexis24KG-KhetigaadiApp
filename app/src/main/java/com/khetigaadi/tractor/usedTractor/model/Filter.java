package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.Model;
import com.khetigaadi.base.model.Price;
import com.khetigaadi.commons.utils.Drive;

import java.util.List;

public class Filter {
    @SerializedName("brands")
    @Expose
    private List<Brand> brands = null;
    @SerializedName("models")
    @Expose
    private List<Model> models = null;
    @SerializedName("drives")
    @Expose
    private List<Object> drives = null;
    @SerializedName("years")
    @Expose
    private List<Object> years = null;
    @SerializedName("city")
    @Expose
    private List<City> city = null;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("driven")
    @Expose
    private List<Drive> driven = null;

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public List<Object> getDrives() {
        return drives;
    }

    public void setDrives(List<Object> drives) {
        this.drives = drives;
    }

    public List<Object> getYears() {
        return years;
    }

    public void setYears(List<Object> years) {
        this.years = years;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<Drive> getDriven() {
        return driven;
    }

    public void setDriven(List<Drive> driven) {
        this.driven = driven;
    }
}
