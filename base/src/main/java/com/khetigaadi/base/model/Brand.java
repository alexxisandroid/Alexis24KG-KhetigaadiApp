package com.khetigaadi.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sarads8 on 28-Oct-17.
 */
@Entity
public class Brand extends BrandParent implements Parcelable {
    /**
     * Here i am commenting temp. extends BrandParent
     * because we need to change parameters in brands API
     */
    @PrimaryKey
    @SerializedName(value = "brand_id", alternate = {"id_manufacturer", "manufacturer_id", "id"})
    @Expose
    @ColumnInfo(name = "brand_id")
    private int id = 0;

    @SerializedName(value = "brand_name", alternate = {"mf_company_name", "name"})
    @ColumnInfo(name = "brand_name")
    private String name = "";

    @SerializedName(value = "brand_image", alternate = {"mf_company_logo", "image"})
    @ColumnInfo(name = "brand_image")
    private String image = "";
    @SerializedName("is_harvester_brand")
    @Expose
    private int isHarvesterBrand;
    @Ignore
    @SerializedName("model")
    public List<Model> models;
    @Ignore
    private boolean selected;

    @Ignore
    public Brand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public int getIsHarvesterBrand() {
        return isHarvesterBrand;
    }
    public void setIsHarvesterBrand(int isHarvesterBrand) {
        this.isHarvesterBrand = isHarvesterBrand;
    }
    public List<Model> getModels() {
        return models;
    }
    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeTypedList(this.models);
    }
    public Brand() {
    }
    protected Brand(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.image = in.readString();
        this.models = in.createTypedArrayList(Model.CREATOR);
    }
    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }
        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean getSelected() {
        return selected;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return id == brand.id;
    }
    @NonNull
    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", isHarvesterBrand=" + isHarvesterBrand +
                ", models=" + models +
                ", selected=" + selected +
                '}';
    }
}



//package com.khetigaadi.base.model;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import org.jetbrains.annotations.NotNull;
//public class Brand implements Parcelable {
//    @NotNull
//    public  String harvesterId;
//    @NotNull
//    public  Brand brand;
//    @NotNull
//    public Model model;
//    @NotNull
//    public TractorImage getImagee() {
//        return imagee;
//    }
//    public void setImagee(@NotNull TractorImage imagee) {
//        this.imagee = imagee;
//    }
//    @NotNull
//    public  TractorImage imagee;
//    @NotNull
//    public String getHarvesterId() {
//        return harvesterId;
//    }
//    public void setHarvesterId(@NotNull String harvesterId) {
//        this.harvesterId = harvesterId;
//    }
//    @NotNull
//    public HarvesterImage getHarvesterImage() {
//        return harvesterImage;
//    }
//    public void setHarvesterImage(@NotNull HarvesterImage harvesterImage) {
//        this.harvesterImage = harvesterImage;
//    }
//    @NotNull
//    public  HarvesterImage harvesterImage;
//    public Brand() {
//    }
//    //  public static final Parcelable.Creator<Brand> CREATOR = null;
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public int getId() {
//        return id;
//    }
//    public void setId(int id) {
//        this.id = id;
//    }
//    private int id;
//    private String name;
//    public String getImage() {
//        return image;
//    }
//    public void setImage(String image) {
//        this.image = image;
//    }
//    private String image;
//    public boolean isSelected() {
//        return selected;
//    }
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//    private boolean selected;
//    @Override
//    public String toString() {
//        return name;
//    }
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//    @Override
//    public void writeToParcel(Parcel dest,int flags) {
//        dest.writeInt(this.id);
//        dest.writeString(this.name);
//    }
//    public Brand(Parcel in) {
//        this.id = in.readInt();
//        this.name = in.readString();
//    }
//    public static final Parcelable.Creator<Brand> CREATOR = new Parcelable.Creator<Brand>() {
//        @Override
//        public Brand createFromParcel(Parcel source) {
//            return new Brand(source);
//        }
//        @Override
//        public Brand[] newArray(int size) {
//            return new Brand[size];
//        }
//    };
//    public boolean getSelected() {
//        return selected;
//    }
//    public String getImplementName() {
//        return implementName;
//    }
//    public void setImplementName(String implementName) {
//        this.implementName = implementName;
//    }
//    private String implementName;
//    public Brand getImplementImage() {
//        return implementImage;
//    }
//    public void setImplementImage(Brand implementImage) {
//        this.implementImage = implementImage;
//    }
//    private Brand  implementImage;
//    private PopularImpCompDetail firstImplement;
//    public PopularImpCompDetail getFirstImplement() {
//        return firstImplement;
//    }
//    public PopularImpCompDetail getSecondImplement() {
//        return secondImplement;
//    }
//    public void setSecondImplement(PopularImpCompDetail secondImplement) {
//        this.secondImplement = secondImplement;
//    }
//    private PopularImpCompDetail secondImplement;
//    public String getBrandId() {
//        return brandId;
//    }
//    public void setBrandId(String brandId) {
//        this.brandId = brandId;
//    }
//    private String brandId;
//    public String getImplementTypeId() {
//        return implementTypeId;
//    }
//    public void setImplementTypeId(String implementTypeId) {
//        this.implementTypeId = implementTypeId;
//    }
//    private String implementTypeId;
//    public String getImplementModelId() {
//        return implementModelId;
//    }
//    public void setImplementModelId(String implementModelId) {
//        this.implementModelId = implementModelId;
//    }
//    private String implementModelId;
//    public String getCommodity() {
//        return commodity;
//    }
//    public void setCommodity(String commodity) {
//        this.commodity = commodity;
//    }
//    private String commodity;
//    private State state;
//    public State getState() {
//        return state;
//    }
//    private String district;
//    public String getDistrict() {
//        return district;
//    }
//    private String market;
//    public String getMarket() {
//        return market;
//    }
//    private int minPrice;
//    public int getMinPrice() {
//        return minPrice;
//    }
//   private int maxPrice;
//    public int getMaxPrice() {
//        return maxPrice;
//    }
//   private int modalPrice;
//    public int getModalPrice() {
//        return modalPrice;
//    }
//    private String variety;
//    public String getVariety() {
//        return variety;
//    }
//    public void setFirstTractor(Brand firstTractor) {
//        this.firstTractor = firstTractor;
//    }
//    public Brand getFirstTractor() {
//        return firstTractor;
//    }
//    private Brand firstTractor;
//    public void setSecondTractor(Brand secondTractor) {
//        this.secondTractor = secondTractor;
//    }
//    public Brand getSecondTractor() {
//        return secondTractor;
//    }
//    private Brand secondTractor;
//    private String contact;
//    public String getContact() {
//        return contact;
//    }
//    public String getTractor_img_name() {
//        return tractor_img_name;
//    }
//    public void setTractor_img_name(String tractor_img_name) {
//        this.tractor_img_name = tractor_img_name;
//    }
//    private String tractor_img_name;
//    public boolean isBookmark() {
//        return bookmark;
//    }
//    private boolean bookmark;
//    public void setBookmark(boolean bookmarkRemovedSuccessfully) {
//        this.bookmark = bookmarkRemovedSuccessfully;
//    }
//    public void setRentTractorId(String rentTractorId) {
//        this.rentTractorId = rentTractorId;
//    }
//    private String rentTractorId;
//    public String getRentTractorId() {
//        return  rentTractorId;
//    }
//    public void setFavoritesStatus(boolean favoritesStatus) {
//        this.favoritesStatus = favoritesStatus;
//    }
//    private boolean favoritesStatus;
//    public boolean getFavoritesStatus() {
//        return favoritesStatus;
//    }
//    public void setNewTractor(NewTractor newTractor) {
//        this.newTractor = newTractor;
//    }
//    private NewTractor newTractor;
//    public NewTractor getNewTractor() {
//        return newTractor;
//    }
//    public void setRentTractor(RentTractor rentTractor) {
//        this.rentTractor = rentTractor;
//    }
//    private RentTractor rentTractor;
//    public RentTractor getRentTractor() {
//      return rentTractor;
//    }
//    public void setUsedTractor(UsedTractor usedTractor) {
//        this.usedTractor = usedTractor;
//    }
//    private UsedTractor usedTractor;
//    public UsedTractor getUsedTractor() {
//        return usedTractor;
//    }
//   private boolean buyersOpen;
//    public boolean isBuyersOpen() {
//        return buyersOpen;
//    }
//    private Object interestedBuyers;
//    public void setInterestedBuyers(Object o) {
//         this.interestedBuyers = o;
//    }
//    public void setBuyersOpen(boolean buyersOpen) {
//        this.buyersOpen = buyersOpen;
//    }
//    public void setSoldUnsoldStatus(String soldUnsoldStatus) {
//        this.soldUnsoldStatus = soldUnsoldStatus;
//    }
//    private String soldUnsoldStatus;
//    public String getSoldUnsoldStatus() {
//        return soldUnsoldStatus;
//    }
//}
