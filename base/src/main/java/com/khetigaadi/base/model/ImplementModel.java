package com.khetigaadi.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImplementModel implements Parcelable {
    @SerializedName("implement_model_id")
    @Expose
    String implementModelId;

    @SerializedName("implement_type_id")
    @Expose
    String implementTypeId;

    @SerializedName("implement_model_name")
    @Expose
    String implementModelName;

    protected ImplementModel(Parcel in) {
        implementModelId = in.readString();
        implementTypeId = in.readString();
        implementModelName = in.readString();
    }

    public static final Creator<ImplementModel> CREATOR = new Creator<ImplementModel>() {
        @Override
        public ImplementModel createFromParcel(Parcel in) {
            return new ImplementModel(in);
        }

        @Override
        public ImplementModel[] newArray(int size) {
            return new ImplementModel[size];
        }
    };

    public String getImplementModelId() {
        return implementModelId;
    }

    public void setImplementModelId(String implementModelId) {
        this.implementModelId = implementModelId;
    }

    public String getImplementTypeId() {
        return implementTypeId;
    }

    public void setImplementTypeId(String implementTypeId) {
        this.implementTypeId = implementTypeId;
    }

    public String getImplementModelName() {
        return implementModelName;
    }

    public void setImplementModelName(String implementModelName) {
        this.implementModelName = implementModelName;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(implementModelId);
        dest.writeString(implementTypeId);
        dest.writeString(implementModelName);
    }
}
