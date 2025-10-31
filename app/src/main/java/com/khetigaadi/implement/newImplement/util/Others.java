package com.khetigaadi.implement.newImplement.util;
import com.google.gson.annotations.SerializedName;
public class Others {
    public String getBrandLogoPath() {
        return brandLogoPath;
    }
    public void setBrandLogoPath(String brandLogoPath) {
        this.brandLogoPath = brandLogoPath;
    }
    @SerializedName("brandLogoPath")
    private String brandLogoPath;
}
