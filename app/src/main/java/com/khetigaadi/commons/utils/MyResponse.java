package com.khetigaadi.commons.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.model.Banner;

import java.util.List;

/**
 * Created by sarads8 on 28-Oct-17.
 */

public class MyResponse {
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName(value = "msg", alternate = "message")
    @Expose
    //private String msg;
    private List<String> msg = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "MyResponse{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                '}';
    }
}
