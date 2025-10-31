package com.khetigaadi.commons.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class ModelResp extends MyResponse {
    @SerializedName(value = "data")
    @Expose
    private Data data = null;
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "ModelResp{" +
                "data=" + data +
                '}';
    }
}
