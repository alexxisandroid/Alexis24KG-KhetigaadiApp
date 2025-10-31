package com.khetigaadi.tractor.rent.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.Model;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentTractorImplement;
import com.khetigaadi.user.model.User;

import java.util.List;
public class RentTractor {
    @SerializedName("rent_tractor_id")
    @Expose
    private String rentTractorId;
    @SerializedName("tractor_model_id")
    @Expose
    private int tractorModelId;
    @SerializedName("exact_hp")
    @Expose
    private String exactHp;
    @SerializedName("user_views")
    @Expose
    private int userViews;
    @SerializedName("tractor_month")
    @Expose
    private String tractorMonth;
    @SerializedName("tractor_year")
    @Expose
    private int tractorYear;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_bookmark")
    @Expose
    private boolean isBookmark;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("rent_tractor_implement")
    @Expose
    private RentTractorImplement rentTractorImplement;
    @SerializedName("rent_tractor_implements")
    @Expose
    private List<RentTractorImplement> rentTractorImplements = null;
    @SerializedName("drive")
    @Expose
    private Drive drive;
    @SerializedName("rent_tractor_brand")
    @Expose
    private Brand rentTractorBrand;
    @SerializedName("tractor_model")
    @Expose
    private Model tractorModel;
    @SerializedName("rent_tractor_table")
    @Expose
    private RentTractorTable rentTractorTable;
    @SerializedName("hp")
    @Expose
    private Hp hp;
    public String getRentTractorId() {
        return rentTractorId;
    }
    public void setRentTractorId(String rentTractorId) {
        this.rentTractorId = rentTractorId;
    }
    public int getTractorModelId() {
        return tractorModelId;
    }
    public void setTractorModelId(int tractorModelId) {
        this.tractorModelId = tractorModelId;
    }
    public String getExactHp() {
        return exactHp;
    }
    public void setExactHp(String exactHp) {
        this.exactHp = exactHp;
    }
    public int getUserViews() {
        return userViews;
    }
    public void setUserViews(int userViews) {
        this.userViews = userViews;
    }
    public String getTractorMonth() {
        return tractorMonth;
    }
    public void setTractorMonth(String tractorMonth) {
        this.tractorMonth = tractorMonth;
    }
    public int getTractorYear() {
        return tractorYear;
    }
    public void setTractorYear(int tractorYear) {
        this.tractorYear = tractorYear;
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
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public RentTractorImplement getRentTractorImplement() {
        return rentTractorImplement;
    }
    public void setRentTractorImplement(RentTractorImplement rentTractorImplement) {
        this.rentTractorImplement = rentTractorImplement;
    }
    public List<RentTractorImplement> getRentTractorImplements() {
        return rentTractorImplements;
    }
    public void setRentTractorImplements(List<RentTractorImplement> rentTractorImplements) {
        this.rentTractorImplements = rentTractorImplements;
    }
    public Hp getHp() {
        return hp;
    }
    public void setHp(Hp hp) {
        this.hp = hp;
    }
    public Drive getDrive() {
        return drive;
    }
    public void setDrive(Drive drive) {
        this.drive = drive;
    }
    public Brand getRentTractorBrand() {
        return rentTractorBrand;
    }
    public void setRentTractorBrand(Brand rentTractorBrand) {
        this.rentTractorBrand = rentTractorBrand;
    }
    public Model getTractorModel() {
        return tractorModel;
    }
    public void setTractorModel(Model tractorModel) {
        this.tractorModel = tractorModel;
    }
    public RentTractorTable getRentTractorTable() {
        return rentTractorTable;
    }
    public void setRentTractorTable(RentTractorTable rentTractorTable) {
        this.rentTractorTable = rentTractorTable;
    }
}
