package com.khetigaadi.base.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Disclaimer {
    @SerializedName("shopImagePath")
    @Expose
    private String shopImagePath;
    @SerializedName("shopAdStatus")
    @Expose
    private boolean shopAdStatus;
    @SerializedName("shopAdUrl")
    @Expose
    private String shopAdUrl;
    @SerializedName("bannerImagePath")
    @Expose
    private String bannerImagePath;
    @SerializedName("disclaimer")
    @Expose
    private String disclaimer;

    public String getShopImagePath() {
        return shopImagePath;
    }

    public void setShopImagePath(String shopImagePath) {
        this.shopImagePath = shopImagePath;
    }

    public boolean isShopAdStatus() {
        return shopAdStatus;
    }

    public void setShopAdStatus(boolean shopAdStatus) {
        this.shopAdStatus = shopAdStatus;
    }

    public String getShopAdUrl() {
        return shopAdUrl;
    }

    public void setShopAdUrl(String shopAdUrl) {
        this.shopAdUrl = shopAdUrl;
    }

    public String getBannerImagePath() {
        return bannerImagePath;
    }

    public void setBannerImagePath(String bannerImagePath) {
        this.bannerImagePath = bannerImagePath;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
