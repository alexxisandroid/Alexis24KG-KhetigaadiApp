package com.khetigaadi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagePath {
    @SerializedName("newTractorImagePath")
    @Expose
    private String newTractorImage;

    @SerializedName("newsImagePath")
    @Expose
    private String newsImagePath;

    @SerializedName("implementImagePath")
    @Expose
    private String implementImagePath;

    public String getNewsImagePath() {
        return newsImagePath;
    }

    public void setNewsImagePath(String newsImagePath) {
        this.newsImagePath = newsImagePath;
    }

    public String getNewTractorImage() {
        return newTractorImage;
    }

    public void setNewTractorImage(String newTractorImage) {
        this.newTractorImage = newTractorImage;
    }

    public String getImplementImagePath() {
        return implementImagePath;
    }

    public void setImplementImagePath(String implementImagePath) {
        this.implementImagePath = implementImagePath;
    }
}
