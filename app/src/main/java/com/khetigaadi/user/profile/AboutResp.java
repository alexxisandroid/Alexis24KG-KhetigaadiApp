package com.khetigaadi.user.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.commons.utils.MyResponse;
//import com.khetiguru.user.model.MyResponse;

public class AboutResp extends MyResponse {

    @SerializedName("data")
    @Expose
    private AboutData data;

    public AboutData getData() {
        return data;
    }

    public void setData(AboutData data) {
        this.data = data;
    }
}
