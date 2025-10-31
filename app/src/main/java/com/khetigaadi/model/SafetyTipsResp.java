package com.khetigaadi.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SafetyTipsResp {
    @SerializedName("data")
    @Expose
    private SafetyTips data;
    public SafetyTips getData() {
        return data;
    }
    public void setData(SafetyTips data) {
        this.data = data;
    }
}
