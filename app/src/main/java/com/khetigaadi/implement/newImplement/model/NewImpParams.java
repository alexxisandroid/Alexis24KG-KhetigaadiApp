package com.khetigaadi.implement.newImplement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewImpParams {
    @SerializedName("implements")
    @Expose
    private ImplementParam imps;

    public ImplementParam getImps() {
        return imps;
    }

    public void setImps(ImplementParam imps) {
        this.imps = imps;
    }
}
