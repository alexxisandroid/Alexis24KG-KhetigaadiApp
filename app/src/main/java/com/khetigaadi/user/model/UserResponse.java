package com.khetigaadi.user.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.model.agri.Others;

public class UserResponse extends MyResponse {

    @SerializedName("data")
    @Expose
    private User user;
    @SerializedName("others")
    @Expose
    private Others others;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    @NonNull
    @Override
    public String toString() {
        return "UserResponse {" +
                "user=" + user.toString() +
                '}';
    }
}
