package com.khetigaadi.tractor.newTractor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewTrSpecList {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("postion")
    @Expose
    private int position;
    @SerializedName("tractor_spec")
    @Expose
    private List<NewTraSpec> tractorSpec = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<NewTraSpec> getTractorSpec() {
        return tractorSpec;
    }

    public void setTractorSpec(List<NewTraSpec> tractorSpec) {
        this.tractorSpec = tractorSpec;
    }
}
