package com.khetigaadi.commons.utils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.news.model.Comment;

import java.util.List;

public class NewsDetailsResp extends MyResponse {
    @SerializedName("news_data")
    @Expose
    private List<News> news = null;
    //private News news;
    //Temporarly commented by PRANAV
    // @SerializedName("comment")
    // @Expose
    private List<Comment> comments = null;

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public String toString() {
        return "NewsDetailsResp{" +
                "news=" + news +
                ", comments=" + comments +
                '}';
    }
}
