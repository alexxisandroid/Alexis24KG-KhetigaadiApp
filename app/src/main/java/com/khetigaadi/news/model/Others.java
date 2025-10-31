package com.khetigaadi.news.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others extends com.khetigaadi.base.model.user.Others {
    @SerializedName("newTractorImagePath")
    @Expose
    private String newTractorImage;
    @SerializedName("newsImagePath")
    @Expose
    private String newsImagePath;
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
}
