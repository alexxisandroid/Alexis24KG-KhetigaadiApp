package com.khetigaadi.base.model.agri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;
@Entity
public class Crop implements Parcelable {
    @SerializedName("crop_id")
    @Expose
    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("crop_name")
    @Expose
    private String name;
    @SerializedName("crop_icon")
    @Expose
    private String icon;
    @SerializedName("create_date")
    @Expose
    @Ignore
    private String createDate;
    @SerializedName("update_date")
    @Expose
    @Ignore
    private String updateDate;
    @Ignore
    private boolean selected;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getCreateDate() {
        return createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @NonNull
    @Override
    public String toString() {
        return "Crop{" +
                "cropId='" + id + '\'' +
                ", cropName='" + name + '\'' +
                ", cropIcon='" + icon + '\'' +
                ", createDate='" + createDate + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", selected=" + selected +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.icon);
        dest.writeString(this.createDate);
        dest.writeString(this.updateDate);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }
    public Crop() {
    }
    protected Crop(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.icon = in.readString();
        this.createDate = in.readString();
        this.updateDate = in.readString();
        this.selected = in.readByte() != 0;
    }
    public static final Creator<Crop> CREATOR = new Creator<Crop>() {
        @Override
        public Crop createFromParcel(Parcel source) {
            return new Crop(source);
        }
        @Override
        public Crop[] newArray(int size) {
            return new Crop[size];
        }
    };
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crop crop = (Crop) o;
        return id.equals(crop.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


//import android.os.Parcel;
//import android.os.Parcelable;
//
//import androidx.annotation.NonNull;
//public class Crop implements Parcelable {
//    protected Crop(Parcel in) {
//    }
//    public static final Creator<Crop> CREATOR = new Creator<Crop>() {
//        @Override
//        public Crop createFromParcel(Parcel in) {
//            return new Crop(in);
//        }
//        @Override
//        public Crop[] newArray(int size) {
//            return new Crop[size];
//        }
//    };
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//    @Override
//    public void writeToParcel(@NonNull Parcel parcel, int i) {
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String name;
//    public String getName() {
//        return name;
//    }
//    public boolean isSelected() {
//        return selected;
//    }
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
//    public boolean selected;
//}
