package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.Model;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.tractor.util.TractorImage;

import java.util.List;
public class CompTractTractor {
    @SerializedName("tractor_id")
    @Expose
    private String tractorId;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("tractor_model_id")
    @Expose
    private String tractorModelId;
    @SerializedName("exact_hp")
    @Expose
    private String exactHp;
    @SerializedName("price_from")
    @Expose
    private String priceFrom;
    @SerializedName("price_to")
    @Expose
    private String priceTo;
    @SerializedName("tractor_name")
    @Expose
    private String tractorName;
    @SerializedName("views")
    @Expose
    private int views;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("tractor_position")
    @Expose
    private int tractorPosition;
    @SerializedName("is_bookmark")
    @Expose
    private boolean isBookmark;
    @SerializedName("get_short_spec_data")
    @Expose
    private List<GetShortSpec> getShortSpecData = null;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("tractor_model")
    @Expose
    private Model tractorModel;
    @SerializedName("drive")
    @Expose
    private Drive drive;
    @SerializedName("tractor_image")
    @Expose
    private TractorImage tractorImage;
    @SerializedName("tractor_spec_data")
    @Expose
    private List<TractorSpec> tractorSpecData = null;
    public String getTractorId() {
        return tractorId;
    }
    public void setTractorId(String tractorId) {
        this.tractorId = tractorId;
    }
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getTractorModelId() {
        return tractorModelId;
    }
    public void setTractorModelId(String tractorModelId) {
        this.tractorModelId = tractorModelId;
    }
    public String getExactHp() {
        return exactHp;
    }
    public void setExactHp(String exactHp) {
        this.exactHp = exactHp;
    }
    public String getPriceFrom() {
        return priceFrom;
    }
    public void setPriceFrom(String priceFrom) {
        this.priceFrom = priceFrom;
    }
    public String getPriceTo() {
        return priceTo;
    }
    public void setPriceTo(String priceTo) {
        this.priceTo = priceTo;
    }
    public String getTractorName() {
        return tractorName;
    }
    public void setTractorName(String tractorName) {
        this.tractorName = tractorName;
    }
    public int getViews() {
        return views;
    }
    public void setViews(int views) {
        this.views = views;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getTractorPosition() {
        return tractorPosition;
    }
    public void setTractorPosition(int tractorPosition) {
        this.tractorPosition = tractorPosition;
    }
    public boolean isBookmark() {
        return isBookmark;
    }
    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
    public List<GetShortSpec> getGetShortSpecData() {
        return getShortSpecData;
    }
    public void setGetShortSpecData(List<GetShortSpec> getShortSpecData) {
        this.getShortSpecData = getShortSpecData;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public Model getTractorModel() {
        return tractorModel;
    }
    public void setTractorModel(Model tractorModel) {
        this.tractorModel = tractorModel;
    }
    public Drive getDrive() {
        return drive;
    }
    public void setDrive(Drive drive) {
        this.drive = drive;
    }
    public TractorImage getTractorImage() {
        return tractorImage;
    }
    public void setTractorImage(TractorImage tractorImage) {
        this.tractorImage = tractorImage;
    }
    public List<TractorSpec> getTractorSpecData() {
        return tractorSpecData;
    }
    public void setTractorSpecData(List<TractorSpec> tractorSpecData) {
        this.tractorSpecData = tractorSpecData;
    }
}
