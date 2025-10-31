package com.khetigaadi.tractor.usedTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

public class GetLoanResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private GetLoan data;

    public GetLoan getData() {
        return data;
    }

    public void setData(GetLoan data) {
        this.data = data;
    }
}
