package com.khetigaadi.news.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class ReplyResp extends MyResponse {
    @SerializedName("NewsCommentReply")
    @Expose
    private List<Reply> replies = null;
    @SerializedName("last_page")
    @Expose
    private int pages;
    public List<Reply> getReplies() {
        return replies;
    }
    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
    public int getPages() {
        return pages;
    }
    public void setPages(int pages) {
        this.pages = pages;
    }
}
