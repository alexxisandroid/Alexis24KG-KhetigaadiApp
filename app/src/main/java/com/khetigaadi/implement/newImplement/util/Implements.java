package com.khetigaadi.implement.newImplement.util;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
public class Implements {
    @SerializedName("brand_id")
    private int brandId;
    @SerializedName("implement_type_id")
    private int typeId;
    @SerializedName("implement_model_id")
    private int modelId;
     @SerializedName("implement_id")
     private String id;
    @SerializedName("implement_name")
    private String name;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
