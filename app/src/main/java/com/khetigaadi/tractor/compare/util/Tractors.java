package com.khetigaadi.tractor.compare.util;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
public class Tractors {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("first_tractor")
    @Expose
    private NewTractor firstTractor;
    @SerializedName("secound_tractor")
    @Expose
    private NewTractor secondTractor;
    public NewTractor getFirstTractor() {
        return firstTractor;
    }
    public void setFirstTractor(NewTractor firstTractor) {
        this.firstTractor = firstTractor;
    }
    public NewTractor getSecondTractor() {
        return secondTractor;
    }
    public void setSecondTractor(NewTractor secondTractor) {
        this.secondTractor = secondTractor;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}

