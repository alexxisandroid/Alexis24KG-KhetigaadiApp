package com.khetigaadi.commons.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

public class NewsDetailResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
