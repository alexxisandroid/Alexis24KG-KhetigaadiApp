package com.khetigaadi.implement.newImplement.util;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Model;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Brand extends BrandParent implements Parcelable {
    @PrimaryKey
    @SerializedName(value = "brand_id",alternate = {"id_manufacturer","manufacturer_id", "id"})
    @Expose
    @ColumnInfo(name = "brand_id")
    private int id = 0;
    @SerializedName(value = "brand_name",alternate = {"mf_company_name","name"})
    @ColumnInfo(name = "brand_name")
    private String name = "";
    @SerializedName(value = "brand_type", alternate = {"type","brandType"})
    @ColumnInfo(name = "brand_type")
    private int brand_type;
    @SerializedName(value = "brand_image", alternate = {"mf_company_logo", "image"})
    @ColumnInfo(name = "brand_image")
    private String image;
    @SerializedName("is_harvester_brand")
    @Expose
    private int isHarvesterBrand;
    @SerializedName(value = "brand_status",alternate = {"brandStatus"})
    @ColumnInfo(name = "brand_status")
    private int brand_status;
    @Ignore
    @SerializedName("model")
    public List<Model> models;
    @Ignore
    private boolean selected;
    @Ignore
    public Brand(int id,String name) {
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
    public int getBrand_status() {
        return brand_status;
    }
    public void setBrand_status(int brand_status) {
        this.brand_status = brand_status;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getBrand_type() {
        return brand_type;
    }
    public void setBrand_type(int brand_type) {
        this.brand_type = brand_type;
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
    @NonNull
    @Override
    public String toString() {
        return name;
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
}