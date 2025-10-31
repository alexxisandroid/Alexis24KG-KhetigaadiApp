package com.khetigaadi.shop.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
public class ProductListResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private ProductListData data;
    @SerializedName("others")
    @Expose
    private Others others;

    public ProductListData getData() {
        return data;
    }
    public void setData(ProductListData data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    public int getLastPage() {
        return lastPage;
    }
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
    //last_page
    @SerializedName("last_page")
    private int lastPage;
}
