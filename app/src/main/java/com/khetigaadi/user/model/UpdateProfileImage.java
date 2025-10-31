package com.khetigaadi.user.model;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class UpdateProfileImage {
    @SerializedName("userProfileImage")
    @Expose
    private String imageName;
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    @NonNull
    @Override
    public String toString() {
        return "UpdateProfileImage{" +
                '}';
    }
}
