package com.khetigaadi.tractor.rent.model;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;

import java.util.List;
public class RentTractorData extends ListData {
    @SerializedName("data")
    @Expose
    private List<RentTractor> data = null;

    protected RentTractorData(Parcel in) {
        super(in);
    }

    public List<RentTractor> getData() {
        return data;
    }
    public void setData(List<RentTractor> data) {
        this.data = data;
    }
}
