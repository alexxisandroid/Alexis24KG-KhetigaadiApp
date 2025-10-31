package com.khetigaadi.user.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Dashboard {
    @SerializedName("uploaded_old_tractor_list")
    @Expose
    private int uploadedOldTractorList;
    @SerializedName("sold_old_tractor")
    @Expose
    private int soldOldTractor;
    @SerializedName("pending_sell_old_tractor")
    @Expose
    private int pendingSellOldTractor;
    @SerializedName("uploaded_rent_tractor")
    @Expose
    private int uploadedRentTractor;
    public int getUploadedOldTractorList() {
        return uploadedOldTractorList;
    }
    public void setUploadedOldTractorList(int uploadedOldTractorList) {
        this.uploadedOldTractorList = uploadedOldTractorList;
    }
    public int getSoldOldTractor() {
        return soldOldTractor;
    }
    public void setSoldOldTractor(int soldOldTractor) {
        this.soldOldTractor = soldOldTractor;
    }
    public int getPendingSellOldTractor() {
        return pendingSellOldTractor;
    }
    public void setPendingSellOldTractor(int pendingSellOldTractor) {
        this.pendingSellOldTractor = pendingSellOldTractor;
    }
    public int getUploadedRentTractor() {
        return uploadedRentTractor;
    }
    public void setUploadedRentTractor(int uploadedRentTractor) {
        this.uploadedRentTractor = uploadedRentTractor;
    }
    @Override
    public String toString() {
        return "Dashboard{" +
                "uploadedOldTractorList=" + uploadedOldTractorList +
                ", soldOldTractor=" + soldOldTractor +
                ", pendingSellOldTractor=" + pendingSellOldTractor +
                ", uploadedRentTractor=" + uploadedRentTractor +
                '}';
    }
}
