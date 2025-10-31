package com.khetigaadi.tractor.usedTractor.util;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sarads8 on 10-Nov-17.
 */

public class SearchTrByName {
    @SerializedName("tractor_id")
    public String tractorId;

    @SerializedName("brand_id")
    public String brandId;

    @SerializedName("tractor_model_id")
    public String usedTractModelId;

    @SerializedName("tractor_name")
    public String name;


    public String getTractorId() {
        return tractorId;
    }

    public void setTractorId(String tractorId) {
        this.tractorId = tractorId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getModelId() {
        return usedTractModelId;
    }

    public void setModelId(String UsedTractorModelId) {
        this.usedTractModelId = usedTractModelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
