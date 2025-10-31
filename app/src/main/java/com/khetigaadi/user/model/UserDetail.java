package com.khetigaadi.user.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.agri.Crop;
import java.util.List;
public class UserDetail implements Parcelable {
    @SerializedName("crop_details")
    @Expose
    private List<Crop> crops = null;
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
    @SerializedName("gender")
    @Expose
    private String gender = "";
    @SerializedName("dob")
    @Expose
    private String dob = "";
    @SerializedName("education")
    @Expose
    private Education education;
    public UserDetail() {
    }
    protected UserDetail(Parcel in) {
        crops = in.createTypedArrayList(Crop.CREATOR);
        is_Owner = in.readString();
        is_smartphone = in.readString();
        biography = in.readString();
        awards = in.readString();
        land = in.readString();
        type = in.readParcelable(UserType.class.getClassLoader());
        brand = in.readParcelable(Brand.class.getClassLoader());
        plan = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(crops);
        dest.writeString(is_Owner);
        dest.writeString(is_smartphone);
        dest.writeString(biography);
        dest.writeString(awards);
        dest.writeString(land);
        dest.writeParcelable(type, flags);
        dest.writeParcelable(brand, flags);
        dest.writeString(plan);
    }
    @Override
    public int describeContents() {
        return 0;
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
    public List<Crop> getCrops() {
        return crops;
    }
    public void setCrops(List<Crop> crops) {
        this.crops = crops;
    }
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }
    @NonNull
    @Override
    public String toString() {
        return "UserDetail{" +
                "Is_owner=" + is_Owner +
                ", Is_smartphone='" + is_smartphone + '\'' +
                ", biography='" + biography + '\'' +
                ", plan='" + plan + '\'' +
                '}';
    }
}
