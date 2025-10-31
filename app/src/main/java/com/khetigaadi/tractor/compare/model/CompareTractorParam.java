package com.khetigaadi.tractor.compare.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.newTractor.model.Tractor;

import java.util.List;
public class CompareTractorParam implements Parcelable {
    @SerializedName("tractors")
    @Expose
    private List<Tractor> tractors = null;
    public CompareTractorParam() {
    }
    protected CompareTractorParam(Parcel in) {
        tractors = in.createTypedArrayList(Tractor.CREATOR);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(tractors);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<CompareTractorParam> CREATOR = new Creator<CompareTractorParam>() {
        @Override
        public CompareTractorParam createFromParcel(Parcel in) {
            return new CompareTractorParam(in);
        }
        @Override
        public CompareTractorParam[] newArray(int size) {
            return new CompareTractorParam[size];
        }
    };
    public List<Tractor> getTractors() {
        return tractors;
    }
    public void setTractors(List<Tractor> tractors) {
        this.tractors = tractors;
    }
}
