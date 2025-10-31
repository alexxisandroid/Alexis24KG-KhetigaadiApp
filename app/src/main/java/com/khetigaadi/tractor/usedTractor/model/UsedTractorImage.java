package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsedTractorImage {
    @SerializedName("ut_image_id")
    @Expose
    private int utImageId;
    @SerializedName("used_tractor_image")
    @Expose
    private String usedTractorImage;
    @SerializedName("default")
    @Expose
    private String _default;

    public String getUsedTractorImage() {
        return usedTractorImage;
    }

    public void setUsedTractorImage(String usedTractorImage) {
        this.usedTractorImage = usedTractorImage;
    }

    public int getUtImageId() {
        return utImageId;
    }

    public void setUtImageId(int utImageId) {
        this.utImageId = utImageId;
    }

    public String get_default() {
        return _default;
    }

    public void set_default(String _default) {
        this._default = _default;
    }
}