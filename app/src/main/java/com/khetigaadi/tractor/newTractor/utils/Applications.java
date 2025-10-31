package com.khetigaadi.tractor.newTractor.utils;
public class Applications {
    private final String idealHp;
    private String id,brandId,typeId,modelId,name,image;
    public Applications(String id,String brandId,String typeId,String modelId, String name, String image,String idealHp) {
        this.id=id;
        this.brandId = brandId;
        this.typeId=typeId;
        this.modelId=modelId;
        this.name = name;
        this.image = image;
        this.idealHp=idealHp;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getModelId() {
        return modelId;
    }
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getIdealHp() {
        return idealHp;
    }
}
