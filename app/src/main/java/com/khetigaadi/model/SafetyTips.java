package com.khetigaadi.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SafetyTips {
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("id")
    @Expose
    private int id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
