package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.compare.model.TractorUnit;
public class TractorSpec {
    @SerializedName("tractor_spec_id")
    @Expose
    private int tractorSpecId;
    @SerializedName("tractor_spec_name")
    @Expose
    private String tractorSpecName;
    @SerializedName("spec_position")
    @Expose
    private int specPosition;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("unit_id")
    @Expose
    private int unitId;
    @SerializedName("tractor_unit")
    @Expose
    private TractorUnit tractorUnit;
    public int getTractorSpecId() {
        return tractorSpecId;
    }
    public void setTractorSpecId(int tractorSpecId) {
        this.tractorSpecId = tractorSpecId;
    }
    public String getTractorSpecName() {
        return tractorSpecName;
    }
    public void setTractorSpecName(String tractorSpecName) {
        this.tractorSpecName = tractorSpecName;
    }
    public int getSpecPosition() {
        return specPosition;
    }
    public void setSpecPosition(int specPosition) {
        this.specPosition = specPosition;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getUnitId() {
        return unitId;
    }
    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
    public TractorUnit getTractorUnit() {
        return tractorUnit;
    }
    public void setTractorUnit(TractorUnit tractorUnit) {
        this.tractorUnit = tractorUnit;
    }
}
