package com.khetigaadi.news.model;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class CommentResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private Comment comment;
    public Comment getComment() {
        return comment;
    }
    public void setComment(Comment comment) {
        this.comment = comment;
    }
    @NonNull
    @Override
    public String toString() {
        return "CommentResp{" +
                "comment=" + comment +
                '}';
    }
}
