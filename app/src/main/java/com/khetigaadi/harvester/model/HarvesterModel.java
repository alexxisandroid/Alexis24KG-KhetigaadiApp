package com.khetigaadi.harvester.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HarvesterModel {
    @SerializedName("id")
    @Expose
    private int harvesterModelId;
    @SerializedName("brand_id")
    @Expose
    private int brandId;
    @SerializedName("implement_type_id")
    @Expose
    private int implementTypeId;
    @SerializedName("name")
    @Expose
    private String name;

    public int getHarvesterModelId() {
        return harvesterModelId;
    }

    public void setHarvesterModelId(int harvesterModelId) {
        this.harvesterModelId = harvesterModelId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
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
}
