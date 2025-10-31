package com.khetigaadi.implement.newImplement.model;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others extends com.khetigaadi.news.model.Others {
    @SerializedName("brandLogoPath")
    @Expose
    private String brandLogoPath;
    @SerializedName(value = "implementImagePath", alternate = "newImplementPath")
    @Expose
    private String implementImagePath;
//    @SerializedName("testimonialsImagePath")
//    @Ignore
//    private String testimonialsImagePath;
    public String getBrandLogoPath() {
        return brandLogoPath;
    }
    public void setBrandLogoPath(String brandLogoPath) {
        this.brandLogoPath = brandLogoPath;
    }
    public String getImplementImagePath() {
        return implementImagePath;
    }
    public void setImplementImagePath(String implementImagePath) {
        this.implementImagePath = implementImagePath;
    }
//    public String getTestimonialsImagePath() {
//        return testimonialsImagePath;
//    }
//    public void setTestimonialsImagePath(String testimonialsImagePath) {
//        this.testimonialsImagePath = testimonialsImagePath;
//    }
}
