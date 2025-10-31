package com.khetigaadi.harvester.model;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.newImplement.util.Type;
public class Harvester {
    @SerializedName("id")
    @Expose
    private String harvesterId;
    @SerializedName("implement_type_id")
    @Expose
    private int implementTypeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("views")
    @Expose
    private int views;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("harvester_type")
    @Expose
    private Type harvesterType;
    @SerializedName("harvester_image")
    @Expose
    private HarvesterImage harvesterImage;
    @SerializedName("harvester_model")
    @Expose
    private HarvesterModel harvesterModel;
    public String getHarvesterId() {
        return harvesterId;
    }
    public void setHarvesterId(String harvesterId) {
        this.harvesterId = harvesterId;
    }
    public int getImplementTypeId() {
        return implementTypeId;
    }
    public void setImplementTypeId(int implementTypeId) {
        this.implementTypeId = implementTypeId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getViews() {
        return views;
    }
    public void setViews(int views) {
        this.views = views;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public Type getHarvesterType() {
        return harvesterType;
    }
    public void setHarvesterType(Type harvesterType) {
        this.harvesterType = harvesterType;
    }
    public HarvesterImage getHarvesterImage() {
        return harvesterImage;
    }
    public void setHarvesterImage(HarvesterImage harvesterImage) {
        this.harvesterImage = harvesterImage;
    }
    public HarvesterModel getHarvesterModel() {
        return harvesterModel;
    }
    public void setHarvesterModel(HarvesterModel harvesterModel) {
        this.harvesterModel = harvesterModel;
    }
    @NonNull
    @Override
    public String toString() {
        return "Harvester{" +
                "harvesterId=" + harvesterId +
                ", implementTypeId=" + implementTypeId +
                ", name='" + name + '\'' +
                ", views=" + views +
                ", brand=" + brand +
                ", harvesterType=" + harvesterType +
                ", harvesterImage=" + harvesterImage +
                ", harvesterModel=" + harvesterModel +
                '}';
    }
}
