package com.khetigaadi.tractor.compare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompTracSpecific {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("postion")
    @Expose
    private int postion;
    @SerializedName("tractor_spec")
    @Expose
    private List<CompTractorSpec> tractorSpec = null;

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

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public List<CompTractorSpec> getTractorSpec() {
        return tractorSpec;
    }

    public void setTractorSpec(List<CompTractorSpec> tractorSpec) {
        this.tractorSpec = tractorSpec;
    }
}
