package com.khetigaadi.implement.newImplement.util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.implement.newImplement.model.NewImpData;
import com.khetigaadi.implement.newImplement.model.Others;
public class ImplementListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private NewImpData data;
    @SerializedName("others")
    @Expose
    private Others others;
    public NewImpData getData() {
        return data;
    }
    public void setData(NewImpData data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
