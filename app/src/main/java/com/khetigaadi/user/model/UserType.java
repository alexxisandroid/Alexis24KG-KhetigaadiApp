package com.khetigaadi.user.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserType implements Parcelable {
    @SerializedName("user_level_id")
    @Expose
    private Integer userLevelId = 0;
    @SerializedName("user_level_name")
    @Expose
    private String userLevelName = "";
    @SerializedName("created_at")
    @Expose
    private String createdAt = "";
    @SerializedName("update_at")
    @Expose
    private String updateAt = "";

    public Integer getUserLevelId() {
        return userLevelId;
    }

    public void setUserLevelId(Integer userLevelId) {
        this.userLevelId = userLevelId;
    }

    public String getUserLevelName() {
        return userLevelName;
    }

    public void setUserLevelName(String userLevelName) {
        this.userLevelName = userLevelName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "userLevelId=" + userLevelId.toString() +
                ", userLevelName='" + userLevelName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.userLevelId);
        dest.writeString(this.userLevelName);
        dest.writeString(this.createdAt);
        dest.writeString(this.updateAt);
    }

    public UserType() {
    }

    protected UserType(Parcel in) {
        this.userLevelId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userLevelName = in.readString();
        this.createdAt = in.readString();
        this.updateAt = in.readString();
    }

    public static final Parcelable.Creator<UserType> CREATOR = new Parcelable.Creator<UserType>() {
        @Override
        public UserType createFromParcel(Parcel source) {
            return new UserType(source);
        }

        @Override
        public UserType[] newArray(int size) {
            return new UserType[size];
        }
    };
}
