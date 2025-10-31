package com.khetigaadi.tractor.compare.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class TractorShortSpec {
    @SerializedName("tractor_spec_id")
    @Expose
    private String id;
    @SerializedName("tractor_spec_name")
    @Expose
    private String name;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
