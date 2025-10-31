package com.khetigaadi.user.myfavorites.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
public class MyFavUsedTractor {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("used_tractor_id")
    @Expose
    private int usedTractorId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("old_tractor")
    @Expose
    private UsedTractor usedTractor;
    public UsedTractor getUsedTractor() {
        return usedTractor;
    }
    public void setUsedTractor(UsedTractor usedTractor) {
        this.usedTractor = usedTractor;
    }
}
