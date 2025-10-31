package com.khetigaadi.commons.utils;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
public class NewsResp{
    @SerializedName("news_data")
    @Expose
    private ArrayList<News> news = null;
    @SerializedName("last_page")
    @Expose
    private int totPages;
    public ArrayList<News> getNews() {
        return news;
    }
    public void setNews(ArrayList<News> news) {
        this.news = news;
    }
    public int getTotPages() {
        return totPages;
    }
    public void setTotPages(int totPages) {
        this.totPages = totPages;
    }
    @NonNull
    @Override
    public String toString() {
        return "NewsResp{" +
                "news=" + news +
                ", totPages=" + totPages +
                '}';
    }
}
