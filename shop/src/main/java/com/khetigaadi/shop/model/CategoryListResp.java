package com.khetigaadi.shop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
import com.khetigaadi.shop.model.Category;
public class CategoryListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<Category> categories = null;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @SerializedName("others")
    @Expose
    private Others others;

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }
}
