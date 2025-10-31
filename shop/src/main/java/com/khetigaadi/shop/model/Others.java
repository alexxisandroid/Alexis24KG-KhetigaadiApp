package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class Others extends com.khetigaadi.base.model.user.Others implements Parcelable {
    @SerializedName("shopProductImagePath")
    //@Expose
    private String shopProductImagePath;
    @SerializedName("shopCategoryImagePath")
   // @Expose
    private String shopCategoryImagePath;
    @SerializedName("trendingProducts")
    @Expose
    private List<Product> trendingProducts;
    public List<Product> getTrendingProducts() {
        return trendingProducts;
    }
    public void setTrendingProducts(List<Product> trendingProducts) {
        this.trendingProducts = trendingProducts;
    }
    public String getShopProductImagePath() {
        return shopProductImagePath;
    }
    public void setShopProductImagePath(String shopProductImagePath) {
        this.shopProductImagePath = shopProductImagePath;
    }
    public String getShopCategoryImagePath() {
        return shopCategoryImagePath;
    }
    public void setShopCategoryImagePath(String shopCategoryImagePath) {
        this.shopCategoryImagePath = shopCategoryImagePath;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.shopProductImagePath);
        dest.writeString(this.shopCategoryImagePath);
    }
    public Others() {
    }
    protected Others(Parcel in) {
        super(in);
        this.shopProductImagePath = in.readString();
        this.shopCategoryImagePath = in.readString();
    }
    public static final Creator<Others> CREATOR = new Creator<Others>() {
        @Override
        public Others createFromParcel(Parcel source) {
            return new Others(source);
        }
        @Override
        public Others[] newArray(int size) {
            return new Others[size];
        }
    };
}