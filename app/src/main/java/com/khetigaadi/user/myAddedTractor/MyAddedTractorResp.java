package com.khetigaadi.user.myAddedTractor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.model.Others;
//import com.khetigaadi.tractor.rent.model.RentTractor;
import java.util.List;
public class MyAddedTractorResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<AddedTractor> data = null;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<AddedTractor> getData() {
        return data;
    }
    public void setData(List<AddedTractor> data) {
        this.data = data;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
}
