package com.khetigaadi.news.activity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.model.agri.Others;
import com.khetigaadi.news.model.CommentData;
public class CommentListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private CommentData data;
    @SerializedName("others")
    @Expose
    private Others others;
    public CommentData getData() {
        return data;
    }
    public void setData(CommentData data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
