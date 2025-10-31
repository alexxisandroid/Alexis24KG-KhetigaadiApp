package com.khetigaadi.model;
public class NameAndImage {
    private final String name;
    private final int drawable;
    public NameAndImage(String name, int drawable) {
        this.name = name;
        this.drawable = drawable;
    }
    public String getName() {
        return name;
    }
    public int getDrawable() {
        return drawable;
    }
}
