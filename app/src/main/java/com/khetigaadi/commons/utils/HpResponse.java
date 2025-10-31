package com.khetigaadi.commons.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.model.Hp;

import java.util.ArrayList;

public class HpResponse extends MyResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<Hp> hp = null;

    public ArrayList<Hp> getHp() {
        return hp;
    }

    public void setHp(ArrayList<Hp> hp) {
        this.hp  = hp;
    }


}
