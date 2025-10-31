package com.khetigaadi.notification.model;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.def.NotificationType;
public class Notification implements Parcelable {
    @Expose
    @SerializedName("image")
    private String imageUrl;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("body")
    private String description;
    private Bitmap image;
    @NotificationType
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("id")
    private String id;
    private String brandId;
    private String typeId;
    private String modelId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public String getType() {
        return type;
    }
    public void setType(@NotificationType String type) {
        this.type = type;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public Bitmap getImage() {
        return image;
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
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.type);
        dest.writeString(this.id);
        dest.writeString(this.brandId);
        dest.writeString(this.typeId);
        dest.writeString(this.modelId);
    }
    public Notification() {
    }
    protected Notification(Parcel in) {
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.type = in.readString();
        this.id = in.readString();
        this.brandId = in.readString();
        this.typeId = in.readString();
        this.modelId = in.readString();
    }
    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }
        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
