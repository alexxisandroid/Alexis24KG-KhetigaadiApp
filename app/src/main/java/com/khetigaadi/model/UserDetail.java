package com.khetigaadi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.util.Brand;
import com.khetigaadi.user.model.UserType;

public class UserDetail implements Parcelable {
    @SerializedName("is_owner")
    @Expose
    private String is_Owner = "";
    @SerializedName("is_smartphone")
    @Expose
    private String is_smartphone = "";
    @SerializedName("biography")
    @Expose
    private String biography = "";
    @SerializedName("awards")
    @Expose
    private String awards = "";
    @SerializedName("land")
    @Expose
    private String land = "";
    @SerializedName("user_level")
    @Expose
    private UserType type;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("plan")
    @Expose
    private String plan = "";


    public String getIs_Owner() {
        return is_Owner;
    }

    public void setIs_Owner(String is_Owner) {
        this.is_Owner = is_Owner;
    }

    public String getIs_smartphone() {
        return is_smartphone;
    }

    public String getAwards() {
        return awards;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setIs_smartphone(String is_smartphone) {
        this.is_smartphone = is_smartphone;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "Is_owner=" + is_Owner +
                ", Is_smartphone='" + is_smartphone + '\'' +
                ", biography='" + biography + '\'' +
                ", plan='" + plan + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.is_Owner);
        dest.writeString(this.is_smartphone);
        dest.writeString(biography);
        dest.writeString(awards);
        dest.writeString(land);
        dest.writeParcelable(this.type, flags);
        dest.writeParcelable(this.brand, flags);
        dest.writeString(plan);
    }

    protected UserDetail(Parcel in) {
        is_Owner = in.readString();
        is_smartphone = in.readString();
        biography = in.readString();
        awards = in.readString();
        land = in.readString();
        this.type = in.readParcelable(UserType.class.getClassLoader());
        this.brand = in.readParcelable(Brand.class.getClassLoader());
        plan = in.readString();
    }

    public static final Creator<UserDetail> CREATOR = new Creator<UserDetail>() {
        @Override
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        @Override
        public UserDetail[] newArray(int size) {
            return new UserDetail[size];
        }
    };

}
