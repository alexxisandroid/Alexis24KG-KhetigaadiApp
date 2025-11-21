package com.khetigaadi.tractor.newTractor.model;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;

import java.util.List;
public class NewTractorData extends ListData {
    @SerializedName("data")
    @Expose
    private List<NewTractor> data = null;

    public NewTractorData(Parcel in) {
        super(in);
    }

    public NewTractorData() {
       // super();
    }

    public List<NewTractor> getData() {
        return data;
    }
    public void setData(List<NewTractor> data) {
        this.data = data;
    }
}
