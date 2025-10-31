package com.khetigaadi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllBanners {
    @SerializedName("Home")
    @Expose
    private List<Banner> home = null;
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

    public List<Banner> getHome() {
        return home;
    }

    public void setHome(List<Banner> home) {
        this.home = home;
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
