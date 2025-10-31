package com.khetigaadi.user.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.commons.utils.MyResponse;

import java.util.List;

public class UserTypes extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<UserType> data = null;

    public List<UserType> getData() {
        return data;
    }

    public void setData(List<UserType> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserTypes{" +
                "data=" + data +
                '}';
    }
}
