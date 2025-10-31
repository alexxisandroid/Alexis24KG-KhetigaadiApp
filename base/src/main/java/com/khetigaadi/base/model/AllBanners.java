package com.khetigaadi.base.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllBanners {
    @SerializedName("HomePageVideo")
    @Expose
    private List<Banner> homePageVideo = null;
    @SerializedName("Home")
    @Expose
    private List<Banner> home = null;
    @SerializedName("Shop")
    @Expose
    private List<Banner> shop = null;
    @SerializedName("HomeOther")
    @Expose
    private List<Banner> HomeOther = null;
    @SerializedName("ambassador")
    @Expose
    private List<Banner> ambassador = null;
    @SerializedName("OldTractor")
    @Expose
    private List<Banner> oldTractor = null;
    @SerializedName("NewImplement")
    @Expose
    private List<Banner> newImplement = null;
    @SerializedName("NewTractor")
    @Expose
    private List<Banner> newTractor = null;
    @SerializedName("Splash")
    @Expose
    private List<Banner> splash = null;

    @SerializedName("splash_khetiguru")
    @Expose
    private List<Banner> splashKhetiGuru = null;

    @SerializedName("onRoadPrice")
    @Expose
    private List<Banner> onRoadPrice = null;
    @SerializedName("Harvester")
    @Expose
    private List<Banner> harvester = null;

    public List<Banner> getHomePageVideo() {
        return homePageVideo;
    }

    public void setHomePageVideo(List<Banner> homePageVideo) {
        this.homePageVideo = homePageVideo;
    }

    public List<Banner> getHome() {
        return home;
    }

    public void setHome(List<Banner> home) {
        this.home = home;
    }

    public List<Banner> getShop() {
        return shop;
    }

    public void setShop(List<Banner> shop) {
        this.shop = shop;
    }

    public List<Banner> getHomeOther() {
        return HomeOther;
    }

    public void setHomeOther(List<Banner> homeOther) {
        HomeOther = homeOther;
    }

    public List<Banner> getAmbassador() {
        return ambassador;
    }

    public void setAmbassador(List<Banner> ambassador) {
        this.ambassador = ambassador;
    }

    public List<Banner> getOldTractor() {
        return oldTractor;
    }

    public void setOldTractor(List<Banner> oldTractor) {
        this.oldTractor = oldTractor;
    }

    public List<Banner> getNewTractor() {
        return newTractor;
    }

    public void setNewTractor(List<Banner> newTractor) {
        this.newTractor = newTractor;
    }

    public List<Banner> getNewImplement() {
        return newImplement;
    }

    public void setNewImplement(List<Banner> newImplement) {
        this.newImplement = newImplement;
    }

    public List<Banner> getSplash() {
        return splash;
    }

    public void setSplash(List<Banner> splash) {
        this.splash = splash;
    }

    public List<Banner> getSplashKhetiGuru() {
        return splashKhetiGuru;
    }

    public void setSplashKhetiGuru(List<Banner> splashKhetiGuru) {
        this.splashKhetiGuru = splashKhetiGuru;
    }

    public List<Banner> getOnRoadPrice() {
        return onRoadPrice;
    }

    public void setOnRoadPrice(List<Banner> onRoadPrice) {
        this.onRoadPrice = onRoadPrice;
    }

    public List<Banner> getHarvester() {
        return harvester;
    }

    public void setHarvester(List<Banner> harvester) {
        this.harvester = harvester;
    }

    @NonNull
    @Override
    public String toString() {
        return "AllBanners{" +
                "home=" + home +
                ", oldTractor=" + oldTractor +
                ", newTractor=" + newTractor +
                ", newImplement=" + newImplement +
                ", splash=" + splash +
                // ", disclaimer=" + others +
                '}';
    }
}
