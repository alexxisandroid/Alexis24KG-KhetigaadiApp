package com.khetigaadi.user.myfavorites.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.user.myfavorites.util.MyFavorites;
public class MyFavoritesResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private MyFavorites data;
    @SerializedName("others")
    @Expose
    private Others others;
    public MyFavorites getData() {
        return data;
    }
    public void setData(MyFavorites data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
