package com.khetigaadi.tractor.rent.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others extends com.khetigaadi.user.myfavorites.model.Others {
    @SerializedName("implementTypeImagePath")
    @Expose
    private String implementTypeImagePath;
    public String getImplementTypeImagePath() {
        return implementTypeImagePath;
    }
    public void setImplementTypeImagePath(String implementTypeImagePath) {
        this.implementTypeImagePath = implementTypeImagePath;
    }
}
