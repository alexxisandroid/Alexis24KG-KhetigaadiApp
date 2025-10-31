package com.khetigaadi.news.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class NewsListResp {
    @SerializedName("data")
    @Expose
    private NewsData data;
    public NewsData getData() {
        return data;
    }
    public void setData(NewsData data) {
        this.data = data;
    }
}
