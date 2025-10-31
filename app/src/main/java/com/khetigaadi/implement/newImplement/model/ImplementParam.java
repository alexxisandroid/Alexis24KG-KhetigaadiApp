package com.khetigaadi.implement.newImplement.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.model.Model;
import com.khetigaadi.implement.newImplement.util.Type;

public class ImplementParam {
    @SerializedName("brands")
    @Expose
    private Brand brands;
    @SerializedName("type")
    @Expose
    private Type type;
    @SerializedName("model")
    @Expose
    private Model model;

    public Brand getBrands() {
        return brands;
    }

    public void setBrands(Brand brands) {
        this.brands = brands;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
