package com.khetigaadi.implement.newImplement.util;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.implement.newImplement.model.ImplementModel;
import com.khetigaadi.implement.newImplement.model.ImplementSpecification;

import java.util.List;
/**
 * Created by Irfan on 18-Apr-16.
 */
public class NewImp implements Parcelable {
    @SerializedName("implement_id")
    @Expose
    private String id;
    @SerializedName("brand_id")
    @Expose
    private int brandId;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("implement_type_id")
    @Expose
    private int implementTypeId;
    @SerializedName("implement_type")
    @Expose
    private Type type;
    @SerializedName("implement_model_id")
    @Expose
    private int implementModelId;
    @SerializedName("implement_model")
    @Expose
    private ImplementModel model;
    @SerializedName("implement_image")
    @Expose
    private ImplementImage implementImage = new ImplementImage();
    @SerializedName("implement_name")
    @Expose
    private String implementName;
    @SerializedName("views")
    @Expose
    private String views;
    @SerializedName("hp_range")
    @Expose
    private Hp hp;
    @SerializedName("implement_specification")
    @Expose
    private List<ImplementSpecification> implementSpecification = null;
    protected NewImp(Parcel in) {
        id = in.readString();
        brandId = in.readInt();
        brand = in.readParcelable(Brand.class.getClassLoader());
        implementTypeId = in.readInt();
        type = in.readParcelable(Type.class.getClassLoader());
        implementModelId = in.readInt();
        implementName = in.readString();
        views = in.readString();
        hp = in.readParcelable(Hp.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(brandId);
        dest.writeParcelable(brand, flags);
        dest.writeInt(implementTypeId);
        dest.writeParcelable(type, flags);
        dest.writeInt(implementModelId);
        dest.writeString(implementName);
        dest.writeString(views);
        dest.writeParcelable(hp, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<NewImp> CREATOR = new Creator<NewImp>() {
        @Override
        public NewImp createFromParcel(Parcel in) {
            return new NewImp(in);
        }
        @Override
        public NewImp[] newArray(int size) {
            return new NewImp[size];
        }
    };
    public Hp getHp() {
        return hp;
    }
    public void setHp(Hp hp) {
        this.hp = hp;
    }
    public Integer getImplementModelId() {
        return implementModelId;
    }
    public void setImplementModelId(Integer implementModelId) {
        this.implementModelId = implementModelId;
    }
    public Integer getImplementTypeId() {
        return implementTypeId;
    }
    public void setImplementTypeId(Integer implementTypeId) {
        this.implementTypeId = implementTypeId;
    }
    public Type getType() {
        return type;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrandImage(Brand brand) {
        this.brand = brand;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public ImplementModel getModel() {
        return model;
    }
    public void setModel(ImplementModel model) {
        this.model = model;
    }
    public Integer getBrandId() {
        return brandId;
    }
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public ImplementImage getImplementImage() {
        return implementImage;
    }
    public void setImplementImage(ImplementImage implementImage) {
        this.implementImage = implementImage;
    }
    public String getImplementName() {
        return implementName;
    }
    public void setImplementName(String implementName) {
        this.implementName = implementName;
    }
    public String getViews() {
        return views;
    }
    public void setViews(String views) {
        this.views = views;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public List<ImplementSpecification> getImplementSpecification() {
        return implementSpecification;
    }
    public void setImplementSpecification(List<ImplementSpecification> implementSpecification) {
        this.implementSpecification = implementSpecification;
    }
    @NonNull
    @Override
    public String toString() {
        return "NewImp{" +
                "id='" + id + '\'' +
                ", brandId=" + brandId +
                ", brand=" + brand +
                ", implementTypeId=" + implementTypeId +
                ", type=" + type +
                ", implementModelId=" + implementModelId +
                ", model=" + model +
                ", implementImage=" + implementImage +
                ", implementName='" + implementName + '\'' +
                ", views='" + views + '\'' +
                ", hp=" + hp +
                ", implementSpecification=" + implementSpecification +
                '}';
    }
}
