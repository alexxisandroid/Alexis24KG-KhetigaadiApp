package com.khetigaadi.tractor.util;

import androidx.annotation.NonNull;

/**
 * Created by Irfan on 1/14/2016.
 */
public class NewsAndImage {
    private String id, name, image, views, brandImage;

    public NewsAndImage(String id, String name, String image, String views, String brandImage) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.views = views;
        this.brandImage = brandImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewsAndImage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", views='" + views + '\'' +
                ", brandImage='" + brandImage + '\'' +
                '}';
    }
}
