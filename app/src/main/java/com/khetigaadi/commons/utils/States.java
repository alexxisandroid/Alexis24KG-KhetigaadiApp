package com.khetigaadi.commons.utils;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.model.State;

import java.util.ArrayList;
public class States extends MyResponse {
    @SerializedName("state")
    private ArrayList<State> states;
    public ArrayList<State> getStates() {
        return states;
    }
    public void setStates(ArrayList<State> states) {
        this.states = states;
    }
}
