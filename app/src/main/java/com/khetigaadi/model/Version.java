package com.khetigaadi.model;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Version {
    @SerializedName("minimumVersion")
    @Expose
    private int minimumVersion;
    @SerializedName("versionCode")
    @Expose
    private int currentVersion;
    @SerializedName("google_play_url")
    @Expose
    private String googlePlayUrl;
    @SerializedName("whats_new")
    @Expose
    private String whatsNew;
    public int getMinimumVersion() {
        return minimumVersion;
    }
    public void setMinimumVersion(int minimumVersion) {
        this.minimumVersion = minimumVersion;
    }
    public int getCurrentVersion() {
        return currentVersion;
    }
    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }
    public String getGooglePlayUrl() {
        return googlePlayUrl;
    }
    public void setGooglePlayUrl(String googlePlayUrl) {
        this.googlePlayUrl = googlePlayUrl;
    }
    public String getWhatsNew() {
        return whatsNew;
    }
    public void setWhatsNew(String whatsNew) {
        this.whatsNew = whatsNew;
    }
    @NonNull
    @Override
    public String toString() {
        return "Version{" +
                "minimumVersion=" + minimumVersion +
                ", currentVersion=" + currentVersion +
                ", googlePlayUrl='" + googlePlayUrl + '\'' +
                ", whatsNew='" + whatsNew + '\'' +
                '}';
    }
}
