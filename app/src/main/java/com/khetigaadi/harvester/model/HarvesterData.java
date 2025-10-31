package com.khetigaadi.harvester.model;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;
import java.util.List;
public class HarvesterData extends ListData {
    @SerializedName("data")
    @Expose
    private List<Harvester> data = null;

    protected HarvesterData(Parcel in) {
        super(in);
    }

    public List<Harvester> getData() {
        return data;
    }
    public void setData(List<Harvester> data) {
        this.data = data;
    }
    @Override
    public int getLastPage() {
        return lastPage;
    }
    @Override
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
    private int lastPage;
}
