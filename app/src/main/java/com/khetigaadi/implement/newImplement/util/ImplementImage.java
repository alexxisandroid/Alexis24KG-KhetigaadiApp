package com.khetigaadi.implement.newImplement.util;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImplementImage {
    @SerializedName("implement_id")
    @Expose
    private Integer id;
    @SerializedName("implement_image")
    @Expose
    private String image;
    @SerializedName("implement_image_id")
    @Expose
    private Integer implementImageId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getImplementImageId() {
        return implementImageId;
    }

    public void setImplementImageId(Integer implementImageId) {
        this.implementImageId = implementImageId;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewImp{" +
                "id='" + id + '\'' +
                ", brandId='" + image + '\'' +
                ", brandName='" + implementImageId + '\'' +
                '}';
    }
}
