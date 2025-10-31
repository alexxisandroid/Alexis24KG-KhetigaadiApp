package com.khetigaadi.tractor.compare.util;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;

import java.util.List;
public class CompareTractors extends ListData {
    @SerializedName(value = "data")
    @Expose
    private List<Tractors> tractors = null;

    protected CompareTractors(Parcel in) {
        super(in);
    }

    public List<Tractors> getTractors() {
        return tractors;
    }
    public void setTractors(List<Tractors> tractors) {
        this.tractors = tractors;
    }
    @NonNull
    @Override
    public String toString() {
        return "CompareTractors{" +
                "tractors=" + tractors +
                '}';
    }
}
