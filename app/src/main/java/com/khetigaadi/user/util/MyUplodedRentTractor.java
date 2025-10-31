package com.khetigaadi.user.util;
import androidx.annotation.NonNull;
public class MyUplodedRentTractor {
    private String id, name, category, hoursRate, hp, listedOn, image;
    public MyUplodedRentTractor(String id, String name, String category, String hoursRate, String hp, String listedOn, String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.hoursRate = hoursRate;
        this.hp = hp;
        this.listedOn = listedOn;
        this.image = image;
    }
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
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getHoursRate() {
        return hoursRate;
    }
    public void setHoursRate(String hoursRate) {
        this.hoursRate = hoursRate;
    }
    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getListedOn() {
        return listedOn;
    }

    public void setListedOn(String listedOn) {
        this.listedOn = listedOn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return "MyUplodedRentTractor{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", hoursRate='" + hoursRate + '\'' +
                ", hp='" + hp + '\'' +
                ", listedOn='" + listedOn + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
