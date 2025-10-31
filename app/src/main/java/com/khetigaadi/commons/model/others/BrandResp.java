package com.khetigaadi.commons.model.others;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.model.BrandImagePath;

import java.util.List;
public class BrandResp<T> extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<Brand> brand = null;
    @SerializedName("others")
    @Expose
    private BrandImagePath brandBaseUrl;
    // private String brandBaseUrl;
    public List<Brand> getBrand() {
        return brand;
    }
    public void setBrand(List<Brand> brand) {
        this.brand = brand;
    }
    public BrandImagePath getBrandBaseUrl() {
        return brandBaseUrl;
    }
    public void setBrandBaseUrl(BrandImagePath brandBaseUrl) {
        this.brandBaseUrl = brandBaseUrl;
    }
    @NonNull
    @Override
    public String toString() {
        return "BrandResp{" +
                "brand=" + brand +
                '}';
    }
}
