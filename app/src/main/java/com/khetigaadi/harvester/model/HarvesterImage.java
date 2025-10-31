package com.khetigaadi.harvester.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class HarvesterImage {
    @SerializedName("id")
    @Expose
    private int harvesterImageId;
    @SerializedName("harvester_id")
    @Expose
    private int harvesterId;
    @SerializedName("name")
    @Expose
    private String name;
    public int getHarvesterImageId() {
        return harvesterImageId;
    }
    public void setHarvesterImageId(int harvesterImageId) {
        this.harvesterImageId = harvesterImageId;
    }
    public int getHarvesterId() {
        return harvesterId;
    }
    public void setHarvesterId(int harvesterId) {
        this.harvesterId = harvesterId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
