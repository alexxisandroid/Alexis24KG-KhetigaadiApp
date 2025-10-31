package com.khetigaadi.shop.model;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class CategoryProducts extends Category {
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    @SerializedName("products")
    @Expose
    private List<Product> products;
//    @SerializedName("image")
//    @Ignore
//    private String image;
}
