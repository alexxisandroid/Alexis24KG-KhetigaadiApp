package com.khetigaadi.user.util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
public class BuyersEnq {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("used_tractors")
    @Expose
    private UsedTractor usedTractor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public UsedTractor getUsedTractor() {
        return usedTractor;
    }

    public void setUsedTractor(UsedTractor usedTractor) {
        this.usedTractor = usedTractor;
    }
}
