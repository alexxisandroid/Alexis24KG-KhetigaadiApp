package com.khetigaadi.news.model;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;
import com.khetigaadi.commons.utils.News;

import java.util.List;
public class NewsData extends ListData {
    @SerializedName("data")
    @Expose
    private List<News> data = null;

    protected NewsData(Parcel in) {
        super(in);
    }

    public List<News> getData() {
        return data;
    }
    public void setData(List<News> data) {
        this.data = data;
    }
}
