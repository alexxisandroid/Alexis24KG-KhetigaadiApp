package com.khetigaadi.user.profile;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AboutData {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("page_name")
    @Expose
    private String pageName;
    @SerializedName("content")
    @Expose
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "AboutData{" +
                "id=" + id +
                ", pageName='" + pageName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
