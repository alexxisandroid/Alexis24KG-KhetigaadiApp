package com.khetigaadi.news.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class NewsImage {
    @SerializedName("object_id")
    @Expose
    private String imageId;
    @SerializedName("open_graph_image")
    @Expose
    private String image;
    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
