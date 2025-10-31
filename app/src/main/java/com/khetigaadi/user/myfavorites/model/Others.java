package com.khetigaadi.user.myfavorites.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others extends com.khetigaadi.base.model.user.Others {
    @SerializedName("brandImagePath")
    @Expose
    private String brandImagePath;
    @SerializedName("oldTractorImagePath")
    @Expose
    private String oldTractorImagePath;
    @SerializedName("newTractorImagePath")
    @Expose
    private String newTractorImagePath;
    public String getBrandImagePath() {
        return brandImagePath;
    }
    public void setBrandImagePath(String brandImagePath) {
        this.brandImagePath = brandImagePath;
    }
    public String getOldTractorImagePath() {
        return oldTractorImagePath;
    }
    public void setOldTractorImagePath(String oldTractorImagePath) {
        this.oldTractorImagePath = oldTractorImagePath;
    }
    public String getNewTractorImagePath() {
        return newTractorImagePath;
    }
    public void setNewTractorImagePath(String newTractorImagePath) {
        this.newTractorImagePath = newTractorImagePath;
    }
}
