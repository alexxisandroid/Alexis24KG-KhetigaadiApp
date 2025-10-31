package com.khetigaadi.implement.newImplement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImplementModel {
    @SerializedName("implement_model_id")
    @Expose
    String implementModelId;

    @SerializedName("implement_type_id")
    @Expose
    String implementTypeId;

    @SerializedName("implement_model_name")
    @Expose
    String implementModelName;

    public String getImplementModelId() {
        return implementModelId;
    }

    public void setImplementModelId(String implementModelId) {
        this.implementModelId = implementModelId;
    }

    public String getImplementTypeId() {
        return implementTypeId;
    }

    public void setImplementTypeId(String implementTypeId) {
        this.implementTypeId = implementTypeId;
    }

    public String getImplementModelName() {
        return implementModelName;
    }

    public void setImplementModelName(String implementModelName) {
        this.implementModelName = implementModelName;
    }
}
