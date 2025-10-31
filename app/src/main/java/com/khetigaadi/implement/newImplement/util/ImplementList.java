package com.khetigaadi.implement.newImplement.util;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.MyResponse;

import java.util.ArrayList;

/**
 * Created by sarads8 on 04-Nov-17.
 */

public class ImplementList extends MyResponse {
    @SerializedName("data")
    private ArrayList<Implements> implementse;

    public ArrayList<Implements> getImplementse() {
        return implementse;
    }

    public void setImplementse(ArrayList<Implements> implementse) {
        this.implementse = implementse;
    }

    @NonNull
    @Override
    public String toString() {
        return "ImplementList{" +
                "implementse=" + implementse +
                '}';
    }
}
