package com.khetigaadi.tractor.compare.util;
import androidx.annotation.NonNull;
public class CompareTraImage {
    String image,image1;
    public CompareTraImage(String image, String image1) {
        this.image = image;
        this.image1 = image1;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getImage1() {
        return image1;
    }
    public void setImage1(String image1) {
        this.image1 = image1;
    }
    @NonNull
    @Override
    public String toString() {
        return "CompareTraImage{" +
                "image='" + image + '\'' +
                ", image1='" + image1 + '\'' +
                '}';
    }
}

