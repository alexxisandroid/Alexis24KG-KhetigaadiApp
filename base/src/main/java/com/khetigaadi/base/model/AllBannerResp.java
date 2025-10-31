package com.khetigaadi.base.model;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AllBannerResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private AllBanners banners = new AllBanners();
    @SerializedName("others")
    @Expose
    private Disclaimer others = new Disclaimer();
    public AllBanners getBanners() {
        return banners;
    }
    public void setBanners(AllBanners banners) {
        this.banners = banners;
    }
    public Disclaimer getOthers() {
        return others;
    }
    public void setOthers(Disclaimer others) {
        this.others = others;
    }
    @NonNull
    @Override
    public String toString() {
        return "AllBannerResp{" +
                "banners=" + banners +
                '}';
    }
}
