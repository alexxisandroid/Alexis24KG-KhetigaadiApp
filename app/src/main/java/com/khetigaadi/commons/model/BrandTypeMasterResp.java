package com.khetigaadi.commons.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.implement.newImplement.util.BrandTypeMaster;

import java.util.List;

public class BrandTypeMasterResp extends MyResponse {
    @SerializedName(value = "data", alternate = "type")
    @Expose
    private List<BrandTypeMaster> types = null;

    public List<BrandTypeMaster> getTypes() {
        return types;
    }

    public void setTypes(List<BrandTypeMaster> types) {
        this.types = types;
    }

    @NonNull
    @Override
    public String toString() {
        return "TypeResp{" +
                "types=" + types +
                '}';
    }
}
