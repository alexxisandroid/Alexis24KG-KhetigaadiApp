package com.khetigaadi.tractor.compare.util;
import androidx.annotation.NonNull;
public class PopularTractorComp {
    String id, name, hp, image, id1, name1, hp1, image1;
    public PopularTractorComp(String id, String name, String hp, String image, String id1, String name1, String hp1, String image1) {
        this.id = id;
        this.name = name;
        this.hp = hp;
        this.image = image;
        this.id1 = id1;
        this.name1 = name1;
        this.hp1 = hp1;
        this.image1 = image1;
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
    public String getHp() {
        return hp;
    }
    public void setHp(String hp) {
        this.hp = hp;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getId1() {
        return id1;
    }
    public void setId1(String id1) {
        this.id1 = id1;
    }
    public String getName1() {
        return name1;
    }
    public void setName1(String name1) {
        this.name1 = name1;
    }
    public String getHp1() {
        return hp1;
    }
    public void setHp1(String hp1) {
        this.hp1 = hp1;
    }
    public String getImage1() {
        return image1;
    }
    public void setImage1(String image1) {
        this.image1 = image1;
    }
    @NonNull
    @Override
    public String toString() {
        return "PopularTractorComp{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", hp='" + hp + '\'' +
                ", image='" + image + '\'' +
                ", id1='" + id1 + '\'' +
                ", name1='" + name1 + '\'' +
                ", hp1='" + hp1 + '\'' +
                ", image1='" + image1 + '\'' +
                '}';
    }
}
