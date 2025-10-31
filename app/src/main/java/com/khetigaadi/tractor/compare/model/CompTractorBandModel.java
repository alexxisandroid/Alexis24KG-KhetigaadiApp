package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class CompTractorBandModel {
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("model_id")
    @Expose
    private String modelId;
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getModelId() {
        return modelId;
    }
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
}
