package com.khetigaadi.tractor.rent.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.util.TractorImage;
public class RentTractorTable {
    @SerializedName("tractor_id")
    @Expose
    private int tractorId;
    @SerializedName("tractor_model_id")
    @Expose
    private int tractorModelId;
    @SerializedName("tractor_name")
    @Expose
    private String tractorName;
    @SerializedName("is_bookmark")
    @Expose
    private boolean isBookmark;
    @SerializedName("tractor_image")
    @Expose
    private TractorImage tractorImage;
    public int getTractorId() {
        return tractorId;
    }
    public void setTractorId(int tractorId) {
        this.tractorId = tractorId;
    }
    public int getTractorModelId() {
        return tractorModelId;
    }
    public void setTractorModelId(int tractorModelId) {
        this.tractorModelId = tractorModelId;
    }
    public String getTractorName() {
        return tractorName;
    }
    public void setTractorName(String tractorName) {
        this.tractorName = tractorName;
    }
    public boolean isBookmark() {
        return isBookmark;
    }
    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
    public TractorImage getTractorImage() {
        return tractorImage;
    }
    public void setTractorImage(TractorImage tractorImage) {
        this.tractorImage = tractorImage;
    }
}
