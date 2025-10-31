package com.khetigaadi.tractor.newTractor.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.model.Others;

import java.util.List;
public class NewTrSpecResp {
    @SerializedName("data")
    @Expose
    private List<NewTrSpecList> data = null;
    @SerializedName("tractor_detail")
    @Expose
    private NewTractor tractorDetail;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("status_code")
    @Expose
    private int statusCode;
    @SerializedName("others")
    @Expose
    private Others others;
    public List<NewTrSpecList> getData() {
        return data;
    }
    public void setData(List<NewTrSpecList> data) {
        this.data = data;
    }
    public NewTractor getTractorDetail() {
        return tractorDetail;
    }
    public void setTractorDetail(NewTractor tractorDetail) {
        this.tractorDetail = tractorDetail;
    }
    public Others getOthers() {
        return others;
    }
    public void setOthers(Others others) {
        this.others = others;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isError() {
        return error;
    }
    public void setError(boolean error) {
        this.error = error;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
