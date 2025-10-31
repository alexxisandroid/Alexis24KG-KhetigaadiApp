package com.khetigaadi.pick_image;
import android.os.Parcel;
import android.os.Parcelable;
public class CropParam implements Parcelable {
    private float ratioY = 1f;
    private float ratioX = 1f;
    private String cropImageName;
    private boolean rounded;
    public float getRatioY() {
        return ratioY;
    }
    public float getRatioX() {
        return ratioX;
    }
    public void setRatio(float x, float y) {
        ratioX = x;
        ratioY = y;
    }
    public String getCropImageName() {
        return cropImageName;
    }
    public void setCropImageName(String cropImageName) {
        this.cropImageName = cropImageName;
    }
    public void setRounded(boolean rounded) {
        this.rounded = rounded;
    }
    public boolean getRounded() {
        return rounded;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeFloat(this.ratioY);
        dest.writeFloat(this.ratioX);
        dest.writeString(this.cropImageName);
        dest.writeByte(this.rounded ? (byte) 1 : (byte) 0);
    }
    public CropParam() {
    }
    protected CropParam(Parcel in) {
        this.ratioY = in.readFloat();
        this.ratioX = in.readFloat();
        this.cropImageName = in.readString();
        this.rounded = in.readByte() != 0;
    }
    public static final Parcelable.Creator<CropParam> CREATOR = new Parcelable.Creator<CropParam>() {
        @Override
        public CropParam createFromParcel(Parcel source) {
            return new CropParam(source);
        }
        @Override
        public CropParam[] newArray(int size) {
            return new CropParam[size];
        }
    };
}
