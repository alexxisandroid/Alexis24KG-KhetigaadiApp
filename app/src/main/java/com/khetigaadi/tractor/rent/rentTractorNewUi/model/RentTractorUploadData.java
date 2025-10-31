package com.khetigaadi.tractor.rent.rentTractorNewUi.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class RentTractorUploadData {
    @SerializedName("rent_tractor_id")
    @Expose
    private int rentTractorId;
    @SerializedName("tractor_model_id")
    @Expose
    private int tractorModelId;
    @SerializedName("tractor_year")
    @Expose
    private int tractorYear;
    @SerializedName("tractor_month")
    @Expose
    private String tractorMonth;
    @SerializedName("hours")
    @Expose
    private String hours;
    @SerializedName("working_radius")
    @Expose
    private String workingRadius;
    @SerializedName("exact_hp")
    @Expose
    private int exactHp;
    @SerializedName("user_views")
    @Expose
    private int userViews;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("upload_from")
    @Expose
    private String uploadFrom;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_bookmark")
    @Expose
    private boolean isBookmark;
    @SerializedName("rent_tractor_implements")
    @Expose
    private List<RentTractorImplement> rentTractorImplements = null;
    public int getRentTractorId() {
        return rentTractorId;
    }
    public void setRentTractorId(int rentTractorId) {
        this.rentTractorId = rentTractorId;
    }
    public int getTractorModelId() {
        return tractorModelId;
    }
    public void setTractorModelId(int tractorModelId) {
        this.tractorModelId = tractorModelId;
    }
    public int getTractorYear() {
        return tractorYear;
    }
    public void setTractorYear(int tractorYear) {
        this.tractorYear = tractorYear;
    }
    public String getTractorMonth() {
        return tractorMonth;
    }
    public void setTractorMonth(String tractorMonth) {
        this.tractorMonth = tractorMonth;
    }
    public String getHours() {
        return hours;
    }
    public void setHours(String hours) {
        this.hours = hours;
    }
    public String getWorkingRadius() {
        return workingRadius;
    }
    public void setWorkingRadius(String workingRadius) {
        this.workingRadius = workingRadius;
    }
    public int getExactHp() {
        return exactHp;
    }
    public void setExactHp(int exactHp) {
        this.exactHp = exactHp;
    }
    public int getUserViews() {
        return userViews;
    }
    public void setUserViews(int userViews) {
        this.userViews = userViews;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getUploadFrom() {
        return uploadFrom;
    }
    public void setUploadFrom(String uploadFrom) {
        this.uploadFrom = uploadFrom;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public boolean isBookmark() {
        return isBookmark;
    }
    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
    public List<RentTractorImplement> getRentTractorImplements() {
        return rentTractorImplements;
    }
    public void setRentTractorImplements(List<RentTractorImplement> rentTractorImplements) {
        this.rentTractorImplements = rentTractorImplements;
    }
}
