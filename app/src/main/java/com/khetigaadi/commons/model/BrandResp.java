package com.khetigaadi.commons.model;
import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.TypeConverters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.implement.newImplement.util.Others;
import com.khetigaadi.implement.newImplement.util.conv.OthersTypeConverter;
import java.util.List;
public class BrandResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<Brand> brand = null;
    @SerializedName("others")
    @Expose
    private Others others=null;
    @SerializedName("brandBaseUrl")
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
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    @NonNull
    @Override
    public String toString() {
        return "BrandResp{" +
                "brand=" + brand +
                '}';
    }
}
