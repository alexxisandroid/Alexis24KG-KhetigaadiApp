package com.khetigaadi.commons.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//import com.khetigaadi.base.model.Model;
import com.khetigaadi.tractor.util.Model;
import java.util.List;

public class Data implements Parcelable {
    @SerializedName(value = "tractor_models", alternate = "models")
    @Expose
    private List<Model> data = null;

    public List<Model> getData() {
        return data;
    }

    public void setData(List<Model> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "ModelResp{" +
                "data=" + data +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.data);
    }

    public Data() {
    }

    protected Data(Parcel in) {
        this.data = in.createTypedArrayList(Model.CREATOR);;
    }

    public final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };


}
