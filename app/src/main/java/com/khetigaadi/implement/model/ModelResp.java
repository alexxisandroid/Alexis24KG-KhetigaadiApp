package com.khetigaadi.implement.model;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.List;
public class ModelResp extends MyResponse {
    @SerializedName(value = "data",alternate = "models")
    @Expose
    private List<Model> data = null;
    public List<Model> getData() {
        return data;
    }
    public void setData(List<Model> data) {
        this.data = data;
    }
    @NonNull
    @Override
    public String toString() {
        return "ModelResp{" +
                "data=" + data +
                '}';
    }
}
