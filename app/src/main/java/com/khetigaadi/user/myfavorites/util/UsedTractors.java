package com.khetigaadi.user.myfavorites.util;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
public class UsedTractors implements Parcelable {
    @SerializedName("used_tractor_id")
    private String id;
    @SerializedName("tractor_name")
    private String name;
    @SerializedName("tractor_image")
    private String image;
    @SerializedName("brand_id")
    private String brandId;
    @SerializedName("brand_image")
    private String brandImage;
    @SerializedName("seller_name")
    private String seller;
    @SerializedName("city")
    private String city;
    @SerializedName("model_month_year")
    private String year;
    @SerializedName("hours_driven")
    private String driven;
    @SerializedName("price")
    private String price;
    @SerializedName("user_view_status")
    private String userViews;
    @SerializedName("sold_unsold_status")
    private String soldStatus="0";
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_type")
    private String userType;
    private String favoritesStatus="1";
    private  boolean sponsor;
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

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDriven() {
        return driven;
    }

    public void setDriven(String driven) {
        this.driven = driven;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserViews() {
        return userViews;
    }

    public void setUserViews(String userViews) {
        this.userViews = userViews;
    }

    public String getSoldStatus() {
        return soldStatus;
    }

    public void setSoldStatus(String soldStatus) {
        this.soldStatus = soldStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFavoritesStatus() {
        return favoritesStatus;
    }
    public void setFavoritesStatus(String favoritesStatus) {
        this.favoritesStatus = favoritesStatus;
    }

    public boolean isSponsor() {
        return sponsor;
    }
    public void setSponsor(boolean sponsor) {
        this.sponsor = sponsor;
    }
    @NonNull
    @Override
    public String toString() {
        return "UsedTractors{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", brandId='" + brandId + '\'' +
                ", brandImage='" + brandImage + '\'' +
                ", seller='" + seller + '\'' +
                ", city='" + city + '\'' +
                ", year='" + year + '\'' +
                ", driven='" + driven + '\'' +
                ", price='" + price + '\'' +
                ", userViews='" + userViews + '\'' +
                ", soldStatus='" + soldStatus + '\'' +
                ", userId='" + userId + '\'' +
                ", userType='" + userType + '\'' +
                ", favoritesStatus='" + favoritesStatus + '\'' +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeString(this.brandId);
        dest.writeString(this.brandImage);
        dest.writeString(this.seller);
        dest.writeString(this.city);
        dest.writeString(this.year);
        dest.writeString(this.driven);
        dest.writeString(this.price);
        dest.writeString(this.userViews);
        dest.writeString(this.soldStatus);
        dest.writeString(this.userId);
        dest.writeString(this.userType);
        dest.writeString(this.favoritesStatus);
        dest.writeByte(this.sponsor ? (byte) 1 : (byte) 0);
    }
    public UsedTractors() {
    }
    protected UsedTractors(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.brandId = in.readString();
        this.brandImage = in.readString();
        this.seller = in.readString();
        this.city = in.readString();
        this.year = in.readString();
        this.driven = in.readString();
        this.price = in.readString();
        this.userViews = in.readString();
        this.soldStatus = in.readString();
        this.userId = in.readString();
        this.userType = in.readString();
        this.favoritesStatus = in.readString();
        this.sponsor = in.readByte() != 0;
    }
    public static final Parcelable.Creator<UsedTractors> CREATOR = new Parcelable.Creator<UsedTractors>() {
        @Override
        public UsedTractors createFromParcel(Parcel source) {
            return new UsedTractors(source);
        }

        @Override
        public UsedTractors[] newArray(int size) {
            return new UsedTractors[size];
        }
    };
}
