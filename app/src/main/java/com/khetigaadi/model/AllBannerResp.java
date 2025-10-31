package com.khetigaadi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.commons.utils.MyResponse;

public class AllBannerResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private AllBanners banners = new AllBanners();

    @SerializedName("others")
    @Expose
    private Others others = new Others();

    public AllBanners getBanners() {
        return banners;
    }

    public void setBanners(AllBanners banners) {
        this.banners = banners;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "AllBannerResp{" +
                "banners=" + banners +
                '}';
    }
}
