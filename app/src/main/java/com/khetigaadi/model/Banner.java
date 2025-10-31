package com.khetigaadi.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Banner {
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
