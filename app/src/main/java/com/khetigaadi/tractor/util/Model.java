package com.khetigaadi.tractor.util;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.TypeConverters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.ImplementModelDataTypeConverter;
import com.khetigaadi.base.model.ImplementModel;
@Entity //com.khetigaadi.base.model
public class Model implements Parcelable {
 //    public int getIdV() {
 //        return idV;
 //    }
 //    public void setIdV(int idV) {
 //        this.idV = idV;
 //    }
    @Expose
    @SerializedName("implementModelId")
    @TypeConverters(ImplementModelDataTypeConverter.class)
    @Ignore
    public ImplementModel implementModelId;
    @PrimaryKey
    @NonNull
    @SerializedName(value = "idd",alternate = {"id_model","tractor_model_id"})
    @Expose
    private int id;
    @SerializedName(value = "brand_id",alternate = {"id_manufacturer"})
    public String brandId;
    @SerializedName(value = "tr_model_name",alternate = {"tractor_model_name","brand_name"})
    public String name;
    @Ignore
    private boolean selected;
    public Model() {
    }
    @Ignore
    public Model(int id,String name) {
        this.id = id;
        this.name = name;
    }
    @Ignore
    public Model(int id,String name,boolean selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.brandId);
        dest.writeString(this.name);
    }
    protected Model(Parcel in) {
        this.id = in.readInt();
        this.brandId = in.readString();
        this.name = in.readString();
    }
    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel source) {
            return new Model(source);
        }
        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean getSelected() {
        return selected;
    }
    public ImplementModel getImplementModelId() {
        return implementModelId;
    }
    public void setImplementModelId(ImplementModel implementModelId) {
        this.implementModelId = implementModelId;
    }
    public boolean isSelected() {
        return selected;
    }
    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", brandId='" + brandId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
