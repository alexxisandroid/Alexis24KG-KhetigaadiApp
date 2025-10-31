package com.khetigaadi.news.model;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class AddReplyResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private Reply reply;
    public Reply getReply() {
        return reply;
    }
    public void setReply(Reply reply) {
        this.reply = reply;
    }
    @NonNull
    @Override
    public String toString() {
        return "AddReplyResp{" +
                "reply=" + reply +
                '}';
    }
}
