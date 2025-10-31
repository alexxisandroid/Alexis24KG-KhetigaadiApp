package com.khetigaadi.base.model;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.jetbrains.annotations.NotNull;

import java.util.List;
public class UsedTractor {
    @SerializedName("used_tractor_id")
    @Expose
    private String id;
    @SerializedName("used_tractor_brand")
    @Expose
    private Brand brand;
    @SerializedName("used_tractor_model")
    @Expose
    private Model model;
    @SerializedName("tractor_price")
    @Expose
    private String price;
    @SerializedName("hours")
    @Expose
    private String hoursDriven;
    @SerializedName("tractor_year")
    @Expose
    private String modelYear = null;

    @SerializedName("tractor_month")
    @Expose
    private String modelMonth = null;
    @SerializedName("view")
    @Expose
    private String userViews;
    @SerializedName("sold_status")
    @Expose
    private String soldUnsoldStatus;
    @SerializedName("is_bookmark")
    @Expose
    private boolean favoritesStatus;

    @SerializedName("tractor_rc_doc")
    @Expose
    private String RC;
    @SerializedName("tractor_owner")
    @Expose
    private String owner;
    @SerializedName("engine_condition")
    @Expose
    private String engineCondition;
    @SerializedName("tyre_condition")
    @Expose
    private String tyreCondition;
    @SerializedName("rto_no")
    @Expose
    private String registrationNo;
    @SerializedName("tractor_noc")
    @Expose
    private String tractorNoc;
    @SerializedName("exact_hp")
    @Expose
    private String exactHp;
    @SerializedName("about_us")
    @Expose
    private String about;
    @NotNull
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("upload_from")
    @Expose
    private String uploadFrom = "android";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHoursDriven() {
        return hoursDriven;
    }
    public void setHoursDriven(String hoursDriven) {
        this.hoursDriven = hoursDriven;
    }
    public String getUserViews() {
        return userViews;
    }
    public void setUserViews(String userViews) {
        this.userViews = userViews;
    }
    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }
    public String getModelYear() {
        return modelYear;
    }
    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }
    public String getModelMonth() {
        return modelMonth;
    }
    public void setModelMonth(String modelMonth) {
        this.modelMonth = modelMonth;
    }
    public String getSoldUnsoldStatus() {
        return soldUnsoldStatus;
    }
    public void setSoldUnsoldStatus(String soldUnsoldStatus) {
        this.soldUnsoldStatus = soldUnsoldStatus;
    }
    public boolean getFavoritesStatus() {
        return favoritesStatus;
    }
    public void setFavoritesStatus(boolean favoritesStatus) {
        this.favoritesStatus = favoritesStatus;
    }
    public @NotNull String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(@NotNull String createdAt) {
        this.createdAt = createdAt;
    }
    public String getTractorNoc() {
        return tractorNoc;
    }
    public void setTractorNoc(String tractorNoc) {
        this.tractorNoc = tractorNoc;
    }
    public String getExactHp() {
        return exactHp;
    }
    public void setExactHp(String exactHp) {
        this.exactHp = exactHp;
    }
    public String getUploadFrom() {
        return uploadFrom;
    }
    public void setUploadFrom(String uploadFrom) {
        this.uploadFrom = uploadFrom;
    }
    @NonNull
    @Override
    public String toString() {
        return "UsedTractor{" +
                "id='" + id + '\'' +
                ", brand=" + brand +
                ", model=" + model +
                ", price='" + price + '\'' +
                ", hoursDriven='" + hoursDriven + '\'' +
                ", modelYear='" + modelYear + '\'' +
                ", modelMonth='" + modelMonth + '\'' +
                ", userViews='" + userViews + '\'' +
                ", soldUnsoldStatus='" + soldUnsoldStatus + '\'' +
                ", favoritesStatus='" + favoritesStatus + '\'' +
                ", RC='" + RC + '\'' +
                ", owner='" + owner + '\'' +
                ", engineCondition='" + engineCondition + '\'' +
                ", tyreCondition='" + tyreCondition + '\'' +
                ", registrationNo='" + registrationNo + '\'' +
                ", about='" + about + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
    public void setRC(String rc) {
        this.RC = rc;
    }
    public String getRC() {
        return RC;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }
    public void setEngineCondition(String engineCondition) {
        this.engineCondition = engineCondition;
    }
    public String getEngineCondition() {
        return engineCondition;
    }
    public void setTyreCondition(String tyreCondition) {
        this.tyreCondition = tyreCondition;
    }
    public String getTyreCondition() {
        return tyreCondition;
    }
    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }
    public String getRegistrationNo() {
        return registrationNo;
    }
    public void setAbout(String about) {
        this.about = about;
    }
    public String getAbout() {
        return about;
    }
}
