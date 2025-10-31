package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.NewTractor;

public class Categoryy extends Category implements Parcelable {
//   @SerializedName("name")
//   @Ignore
//    private String name;
    @SerializedName("selected")
    @Ignore
    private boolean selected;
//    @SerializedName("image")
//    @Ignore
//    private String image;
    @SerializedName("colorCode")
    private String colorCode;
    private NewTractor newTractor;
    protected Categoryy(Parcel in) {
        selected = in.readByte() != 0;
      //  name = in.readString();
       // image = in.readString();
        colorCode = in.readString();
        newTractor = in.readParcelable(NewTractor.class.getClassLoader());
    }
    public static final Creator<Categoryy> CREATOR = new Creator<Categoryy>() {
        @Override
        public Categoryy createFromParcel(Parcel in) {
            return new Categoryy(in);
        }

        @Override
        public Categoryy[] newArray(int size) {
            return new Categoryy[size];
        }
    };
    public Categoryy() {

    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
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
    public String getColorCode() {
        return colorCode;
    }
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }


//    public T getItem() {
//        return item;
//    }
//
//    public void setItem(T item) {
//        this.item = item;
//    }
//
//    private T item;

    public NewTractor getNewTractor() {
        return newTractor;
    }
    public void setNewTractor(NewTractor newTractor) {
        this.newTractor = newTractor;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeByte((byte) (selected ? 1 : 0));
       // dest.writeString(name);
       // dest.writeString(image);
        dest.writeString(colorCode);
        dest.writeParcelable(newTractor,flags);
    }
}


