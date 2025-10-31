package com.khetigaadi.tractor.util;
import androidx.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.room.model.BrandParent;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Brand extends BrandParent  {
    @PrimaryKey
    @NonNull
    @SerializedName(value = "id",alternate = {"id_manufacturer","manufacturer_id","id"})
    @Expose
    private int id = 0;
    @SerializedName(value = "brand_name",alternate = {"mf_company_name","name"})
    private String name = "";
    @SerializedName(value = "brand_image", alternate = {"mf_company_logo", "image"})
    private String image = "";
    @Ignore
    @SerializedName("model")
    public List<Model> models;
    @SerializedName("manufacturerType")
    public String manufacturerType;
    @SerializedName("status")
    public String status;
    @SerializedName("position")
    public String position;
    @SerializedName("implement_position")
    public String implement_position;
    @SerializedName("isHarvesterBrand")
    public int isHarvesterBrand;
    @Ignore
    private boolean selected;
    @Ignore
    public Brand() {
    }
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
    public List<Model> getModels() {
        return models;
    }
    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeTypedList(this.models);
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
}



//package com.khetigaadi.tractor.util;
//import androidx.room.Entity;
//import android.os.Parcel;
//import android.os.Parcelable;
//import androidx.annotation.NonNull;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.khetigaadi.base.model.Model;
//import com.khetigaadi.room.model.BrandParent;
//import java.util.List;
//@Entity
//public class Brand extends BrandParent implements Parcelable {
//    @PrimaryKey
//    @NonNull
//    @SerializedName(value = "brand_id",alternate = {"id_manufacturer","manufacturer_id","id"})
//    @Expose
//    private int id = 0;
//    @SerializedName(value = "brand_name",alternate = {"mf_company_name","name"})
//    private String name = "";
//    @SerializedName(value = "brand_image",alternate = {"mf_company_logo","image"})
//    private String image = "";
//    @Ignore
//    @SerializedName("model")
//    public List<Model> models;
//    @Ignore
//    private boolean selected;
//    @Ignore
//    public Brand(int id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//    public int getId() {
//        return id;
//    }
//    public void setId(int id) {
//        this.id = id;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String getImage() {
//        return image;
//    }
//    public void setImage(String image) {
//        this.image = image;
//    }
//    public List<Model> getModels() {
//        return models;
//    }
//    public void setModels(List<Model> models) {
//        this.models = models;
//    }
//    @NonNull
//    @Override
//    public String toString() {
//        return name;
//    }
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.id);
//        dest.writeString(this.name);
//        dest.writeString(this.image);
//        dest.writeTypedList(this.models);
//    }
//    public Brand() {
//    }
//    protected Brand(Parcel in) {
//        this.id = in.readInt();
//        this.name = in.readString();
//        this.image = in.readString();
//        this.models = in.createTypedArrayList(Model.CREATOR);
//    }
//    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
//        @Override
//        public Brand createFromParcel(Parcel source) {
//            return new Brand(source);
//        }
//
//        @Override
//        public Brand[] newArray(int size) {
//            return new Brand[size];
//        }
//    };
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//    public boolean getSelected() {
//        return selected;
//    }
//}






//package com.khetigaadi.tractor.util;
//import androidx.room.Entity;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import androidx.annotation.NonNull;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//import com.khetigaadi.base.model.Model;
//import com.khetigaadi.room.model.BrandParent;
//
//import java.util.ArrayList;
//import java.util.List;
//@Entity
//public class Brand extends BrandParent implements Parcelable {
//    @PrimaryKey
//    @NonNull
//    @SerializedName(value = "brand_id", alternate = {"id_manufacturer","manufacturer_id","id"})
//    @Expose
//    private int id = 0;
//    @SerializedName(value = "brand_name", alternate = {"mf_company_name","name"})
//    private String name = "";
//    @SerializedName(value = "brand_image", alternate = {"mf_company_logo","image"})
//    private String image = "";
//    @Ignore
//    @SerializedName("model")
//    public List<Model> models;
//    @Ignore
//    private boolean selected;
//    @Ignore
//    public Brand(int id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//    public int getId() {
//        return id;
//    }
//    public void setId(int id) {
//        this.id = id;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String getImage() {
//        return image;
//    }
//    public void setImage(String image) {
//        this.image = image;
//    }
//    public List<Model> getModels() {
//        return models;
//    }
//    public void setModels(ArrayList<Model> models) {
//        this.models = models;
//    }
//    @Override
//    public String toString() {
//        return name;
//    }
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.id);
//        dest.writeString(this.name);
//        dest.writeString(this.image);
//        dest.writeTypedList(this.models);
//    }
//    public Brand() {
//    }
//    protected Brand(Parcel in) {
//        this.id = in.readInt();
//        this.name = in.readString();
//        this.image = in.readString();
//        this.models = in.createTypedArrayList(Model.CREATOR);
//    }
//    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
//        @Override
//        public Brand createFromParcel(Parcel source) {
//            return new Brand(source);
//        }
//        @Override
//        public Brand[] newArray(int size) {
//            return new Brand[size];
//        }
//    };
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//    public boolean getSelected() {
//        return selected;
//    }
//}
