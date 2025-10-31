package com.khetigaadi.implement.newImplement.model;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;
import com.khetigaadi.implement.newImplement.util.NewImp;

import java.util.List;

public class NewImpData extends ListData {
    @SerializedName("data")
    @Expose
    private List<NewImp> imps = null;

    protected NewImpData(Parcel in) {
        super(in);
    }

    public List<NewImp> getImps() {
        return imps;
    }

    public void setImps(List<NewImp> imps) {
        this.imps = imps;
    }
}
