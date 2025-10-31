package com.khetigaadi.tractor.usedTractor.util;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsedTractorModel {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tractor_model_id")
    @Expose
    private Integer tractorModelId;
    @SerializedName("tractor_model_name")
    @Expose
    private String tractorModelName;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTractorModelId() {
        return tractorModelId;
    }

    public void setTractorModelId(Integer tractorModelId) {
        this.tractorModelId = tractorModelId;
    }

    public String getTractorModelName() {
        return tractorModelName;
    }

    public void setTractorModelName(String tractorModelName) {
        this.tractorModelName = tractorModelName;
    }
}
