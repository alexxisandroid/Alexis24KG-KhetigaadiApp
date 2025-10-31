package com.khetigaadi.commons.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Testimonial implements Parcelable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;

    protected Testimonial(Parcel in) {
        id = in.readInt();
        customerName = in.readString();
        description = in.readString();
        image = in.readString();
    }

    public static final Creator<Testimonial> CREATOR = new Creator<Testimonial>() {
        @Override
        public Testimonial createFromParcel(Parcel in) {
            return new Testimonial(in);
        }

        @Override
        public Testimonial[] newArray(int size) {
            return new Testimonial[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(customerName);
        dest.writeString(description);
        dest.writeString(image);
    }
    @Override
    public String toString() {
        return "Testimonial{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
