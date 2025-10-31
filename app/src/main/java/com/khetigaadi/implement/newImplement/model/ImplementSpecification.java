package com.khetigaadi.implement.newImplement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImplementSpecification {
    @SerializedName("imp_spec_data_id")
    @Expose
    private int impSpecDataId;
    @SerializedName("implement_spec_id")
    @Expose
    private int implementSpecId;
    @SerializedName("spec_data")
    @Expose
    private String specData;
    @SerializedName("implement_id")
    @Expose
    private int implementId;
    @SerializedName("is_top")
    @Expose
    private int isTop;
    @SerializedName("position")
    @Expose
    private int position;
    @SerializedName("implement_harvester_specification_label")
    @Expose
    private ImplementHarvesterSpecificationLabel implementHarvesterSpecificationLabel;

    public int getImpSpecDataId() {
        return impSpecDataId;
    }

    public void setImpSpecDataId(int impSpecDataId) {
        this.impSpecDataId = impSpecDataId;
    }

    public int getImplementSpecId() {
        return implementSpecId;
    }

    public void setImplementSpecId(int implementSpecId) {
        this.implementSpecId = implementSpecId;
    }

    public String getSpecData() {
        return specData;
    }

    public void setSpecData(String specData) {
        this.specData = specData;
    }

    public int getImplementId() {
        return implementId;
    }

    public void setImplementId(int implementId) {
        this.implementId = implementId;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ImplementHarvesterSpecificationLabel getImplementHarvesterSpecificationLabel() {
        return implementHarvesterSpecificationLabel;
    }

    public void setImplementHarvesterSpecificationLabel(ImplementHarvesterSpecificationLabel implementHarvesterSpecificationLabel) {
        this.implementHarvesterSpecificationLabel = implementHarvesterSpecificationLabel;
    }
}
