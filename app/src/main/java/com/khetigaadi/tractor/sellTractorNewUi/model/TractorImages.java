package com.khetigaadi.tractor.sellTractorNewUi.model;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class TractorImages implements Parcelable {
    @SerializedName("usedTractorId")
    @Expose
    @ColumnInfo(defaultValue = "usedTractorId")
    @PrimaryKey(autoGenerate = true)
    private int usedTractorId;
    @ColumnInfo(defaultValue = "rightImage")
    @SerializedName(value = "rightImage", alternate = "rightImage")
    private Uri rightImage;
    @ColumnInfo(defaultValue = "frontSideImage")
    @SerializedName(value = "frontSideImage", alternate = "frontSideImage")
    private Uri frontSideImage;
    @ColumnInfo(defaultValue = "leftImage")
    @SerializedName(value = "leftImage", alternate = "leftImage")
    private Uri leftImage;
    @ColumnInfo(defaultValue = "backSideImage")
    @SerializedName(value = "backSideImage", alternate = "backSideImage")
    private Uri backSideImage;
    @ColumnInfo(defaultValue = "frontTyreImage")
    @SerializedName(value = "frontTyreImage", alternate = "frontTyreImage")
    private Uri frontTyreImage;
    @ColumnInfo(defaultValue = "backTyreImage")
    @SerializedName(value = "backTyreImage", alternate = "backTyreImage")
    private Uri backTyreImage;
    public TractorImages() {
    }
    protected TractorImages(Parcel in) {
        usedTractorId = in.readInt();
        rightImage = in.readParcelable(Uri.class.getClassLoader());
        frontSideImage = in.readParcelable(Uri.class.getClassLoader());
        leftImage = in.readParcelable(Uri.class.getClassLoader());
        backSideImage = in.readParcelable(Uri.class.getClassLoader());
        frontTyreImage = in.readParcelable(Uri.class.getClassLoader());
        backTyreImage = in.readParcelable(Uri.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(usedTractorId);
        dest.writeParcelable(rightImage, flags);
        dest.writeParcelable(frontSideImage, flags);
        dest.writeParcelable(leftImage, flags);
        dest.writeParcelable(backSideImage, flags);
        dest.writeParcelable(frontTyreImage, flags);
        dest.writeParcelable(backTyreImage, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<TractorImages> CREATOR = new Creator<TractorImages>() {
        @Override
        public TractorImages createFromParcel(Parcel in) {
            return new TractorImages(in);
        }
        @Override
        public TractorImages[] newArray(int size) {
            return new TractorImages[size];
        }
    };
    public int getUsedTractorId() {
        return usedTractorId;
    }
    public void setUsedTractorId(int usedTractorId) {
        this.usedTractorId = usedTractorId;
    }
    public Uri getRightImage() {
        return rightImage;
    }
    public void setRightImage(Uri rightImage) {
        this.rightImage = rightImage;
    }
    public Uri getFrontSideImage() {
        return frontSideImage;
    }
    public void setFrontSideImage(Uri frontSideImage) {
        this.frontSideImage = frontSideImage;
    }
    public Uri getLeftImage() {
        return leftImage;
    }
    public void setLeftImage(Uri leftImage) {
        this.leftImage = leftImage;
    }
    public Uri getBackSideImage() {
        return backSideImage;
    }
    public void setBackSideImage(Uri backSideImage) {
        this.backSideImage = backSideImage;
    }
    public Uri getFrontTyreImage() {
        return frontTyreImage;
    }
    public void setFrontTyreImage(Uri frontTyreImage) {
        this.frontTyreImage = frontTyreImage;
    }
    public Uri getBackTyreImage() {
        return backTyreImage;
    }
    public void setBackTyreImage(Uri backTyreImage) {
        this.backTyreImage = backTyreImage;
    }
}
