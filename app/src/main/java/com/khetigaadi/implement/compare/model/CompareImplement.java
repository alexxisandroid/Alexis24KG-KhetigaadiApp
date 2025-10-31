package com.khetigaadi.implement.compare.model;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;
import com.khetigaadi.implement.compare.util.PopularImpComp;

import java.util.ArrayList;
import java.util.List;
public class CompareImplement extends ListData {
    @SerializedName(value = "data")
    @Expose
    private List<PopularImpComp> implement = null;

    protected CompareImplement(Parcel in) {
        super(in);
    }

    public List<PopularImpComp> getImplement() {
        return implement;
    }
    public void setImplement(ArrayList<PopularImpComp> implement) {
        this.implement = implement;
    }
    @NonNull
    @Override
    public String toString() {
        return "CompareImplement{" +
                "implement=" + implement +
                '}';
    }
}
