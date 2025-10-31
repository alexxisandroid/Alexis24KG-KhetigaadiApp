package com.khetigaadi.shop.model;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import java.util.List;
public class MyOrderResp extends MyResponse {
//    public boolean isError() {
//        return error;
//    }
//    public void setError(boolean error) {
//        this.error = error;
//    }
   // private boolean error;
//    public List<String> getMsg() {
//        return msg;
//    }
//    public void setMsg(List<String> msg) {
//        this.msg = msg;
//    }
//    private List<String> msg;
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    @SerializedName("others")
    @Expose
    private Others others;
    public List<Order> getData() {
        return data;
    }
    public void setData(List<Order> data) {
        this.data = data;
    }
    @SerializedName("data")
    @Expose
    private List<Order> data;
    @NonNull
    @Override
    public String toString() {
        return "MyOrderResp{" +
                "data=" + data +
                '}';
    }
}
