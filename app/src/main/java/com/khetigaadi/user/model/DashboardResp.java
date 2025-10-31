package com.khetigaadi.user.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class DashboardResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private Dashboard data;
    public Dashboard getData() {
        return data;
    }
    public void setData(Dashboard data) {
        this.data = data;
    }
}
