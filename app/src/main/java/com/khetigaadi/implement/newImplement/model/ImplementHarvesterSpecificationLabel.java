package com.khetigaadi.implement.newImplement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImplementHarvesterSpecificationLabel {
    @SerializedName("implement_spec_id")
    @Expose
    private int implementSpecId;
    @SerializedName("implement_spec_name")
    @Expose
    private String implementSpecName;

    public int getImplementSpecId() {
        return implementSpecId;
    }

    public void setImplementSpecId(int implementSpecId) {
        this.implementSpecId = implementSpecId;
    }

    public String getImplementSpecName() {
        return implementSpecName;
    }

    public void setImplementSpecName(String implementSpecName) {
        this.implementSpecName = implementSpecName;
    }
}
