package com.khetigaadi.tractor.dealer.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.user.model.User;

public class Dealer {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("dealership_name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("telephone")
    @Expose
    private String contact;

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("tractor_brand")
    @Expose
    private Brand brandImage;

    public Brand getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(Brand brandImage) {
        this.brandImage = brandImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public String toString() {
        return "Dealer{" +
                "brandImage='" + brandImage + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

