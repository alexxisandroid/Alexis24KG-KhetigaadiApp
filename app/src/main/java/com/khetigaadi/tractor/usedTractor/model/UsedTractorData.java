package com.khetigaadi.tractor.usedTractor.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;

import java.util.List;

public class UsedTractorData extends ListData {
    @SerializedName("data")
    @Expose
    private List<UsedTractor> data = null;

    protected UsedTractorData(Parcel in) {
        super(in);
    }

    public List<UsedTractor> getData() {
        return data;
    }

    public void setData(List<UsedTractor> data) {
        this.data = data;
    }
}
