package com.khetigaadi.news.model;
import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.ListData;

import java.util.List;
public class CommentData extends ListData {
    @SerializedName("news_comments")
    @Expose
    private List<Comment> comments = null;
    protected CommentData(Parcel in) {
        super(in);
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
