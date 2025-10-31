package com.khetigaadi.commons.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
public class TractorImagePath implements Parcelable {
    @SerializedName("tractorImagePath")
    private String tractor_img_path;
    protected TractorImagePath(Parcel in) {
        tractor_img_path = in.readString();
    }
    public static final Creator<TractorImagePath> CREATOR = new Creator<TractorImagePath>() {
        @Override
        public TractorImagePath createFromParcel(Parcel in) {
            return new TractorImagePath(in);
        }

        @Override
        public TractorImagePath[] newArray(int size) {
            return new TractorImagePath[size];
        }
    };
    public String getTractor_img_path() {
        return tractor_img_path;
    }
    public void setTractor_img_path(String tractor_img_path) {
        this.tractor_img_path = tractor_img_path;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(tractor_img_path);
    }
    @Override
    public String toString() {
        return "TractorImagePath{" +
                "tractor_img_path='" + tractor_img_path + '\'' +
                '}';
    }
}
