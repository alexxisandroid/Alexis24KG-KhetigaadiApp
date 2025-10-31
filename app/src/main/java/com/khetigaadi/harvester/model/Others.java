package com.khetigaadi.harvester.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others {
    @SerializedName("brandLogoPath")
    @Expose
    private String brandLogoPath;
    @SerializedName("harvesterImagePath")
    @Expose
    private String harvesterImagePath;
    public String getBrandLogoPath() {
        return brandLogoPath;
    }
    public void setBrandLogoPath(String brandLogoPath) {
        this.brandLogoPath = brandLogoPath;
    }
    public String getHarvesterImagePath() {
        return harvesterImagePath;
    }
    public void setHarvesterImagePath(String harvesterImagePath) {
        this.harvesterImagePath = harvesterImagePath;
    }
}
