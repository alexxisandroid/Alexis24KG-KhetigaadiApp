package com.khetigaadi.base.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.user.Others;

import java.util.List;

public class StateResp extends MyResponse {
    @SerializedName("data")
    @Expose
    private List<State> states = null;
    @SerializedName("others")
    private Others others;

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public Others getOthers() {
        return others;
    }

    public void setOthers(Others others) {
        this.others = others;
    }

    @NonNull
    @Override
    public String toString() {
        return "StateResp{" +
                "state=" + states +
                '}';
    }
}
