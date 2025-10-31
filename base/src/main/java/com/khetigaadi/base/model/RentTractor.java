package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RentTractor implements Parcelable {
    @SerializedName("rent_tractor_id")
    @Expose
    private String rentTractorId;
    @SerializedName("tractor_model_id")
    @Expose
    private int tractorModelId;
    @SerializedName("exact_hp")
    @Expose
    private String exactHp;
    @SerializedName("user_views")
    @Expose
    private int userViews;
    @SerializedName("tractor_month")
    @Expose
    private String tractorMonth;
    @SerializedName("tractor_year")
    @Expose
    private int tractorYear;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("is_bookmark")
    @Expose
    private boolean isBookmark;
//    @SerializedName("rent_tractor_implement")
//    @Expose
//    private RentTractorImplement rentTractorImplement;
//    @SerializedName("rent_tractor_implements")
//    @Expose
//    private List<RentTractorImplement> rentTractorImplements = null;
    @SerializedName("rent_tractor_brand")
    @Expose
    private Brand rentTractorBrand;
    @SerializedName("tractor_model")
    @Expose
    private Model tractorModel;

    protected RentTractor(Parcel in) {
        rentTractorId = in.readString();
        tractorModelId = in.readInt();
        exactHp = in.readString();
        userViews = in.readInt();
        tractorMonth = in.readString();
        tractorYear = in.readInt();
        createdAt = in.readString();
        isBookmark = in.readByte() != 0;
        rentTractorBrand = in.readParcelable(Brand.class.getClassLoader());
        tractorModel = in.readParcelable(Model.class.getClassLoader());
    }

    public static final Creator<RentTractor> CREATOR = new Creator<RentTractor>() {
        @Override
        public RentTractor createFromParcel(Parcel in) {
            return new RentTractor(in);
        }

        @Override
        public RentTractor[] newArray(int size) {
            return new RentTractor[size];
        }
    };

    public String getRentTractorId() {
        return rentTractorId;
    }
    public void setRentTractorId(String rentTractorId) {
        this.rentTractorId = rentTractorId;
    }
    public int getTractorModelId() {
        return tractorModelId;
    }
    public void setTractorModelId(int tractorModelId) {
        this.tractorModelId = tractorModelId;
    }
    public String getExactHp() {
        return exactHp;
    }
    public void setExactHp(String exactHp) {
        this.exactHp = exactHp;
    }
    public int getUserViews() {
        return userViews;
    }
    public void setUserViews(int userViews) {
        this.userViews = userViews;
    }
    public String getTractorMonth() {
        return tractorMonth;
    }
    public void setTractorMonth(String tractorMonth) {
        this.tractorMonth = tractorMonth;
    }
    public int getTractorYear() {
        return tractorYear;
    }
    public void setTractorYear(int tractorYear) {
        this.tractorYear = tractorYear;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public boolean isBookmark() {
        return isBookmark;
    }
    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
//    public RentTractorImplement getRentTractorImplement() {
//        return rentTractorImplement;
//    }
//    public void setRentTractorImplement(RentTractorImplement rentTractorImplement) {
//        this.rentTractorImplement = rentTractorImplement;
//    }
//    public List<RentTractorImplement> getRentTractorImplements() {
//        return rentTractorImplements;
//    }
//    public void setRentTractorImplements(List<RentTractorImplement> rentTractorImplements) {
//        this.rentTractorImplements = rentTractorImplements;
//    }
    public Brand getRentTractorBrand() {
        return rentTractorBrand;
    }
    public void setRentTractorBrand(Brand rentTractorBrand) {
        this.rentTractorBrand = rentTractorBrand;
    }
    public Model getTractorModel() {
        return tractorModel;
    }
    public void setTractorModel(Model tractorModel) {
        this.tractorModel = tractorModel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(rentTractorId);
        dest.writeInt(tractorModelId);
        dest.writeString(exactHp);
        dest.writeInt(userViews);
        dest.writeString(tractorMonth);
        dest.writeInt(tractorYear);
        dest.writeString(createdAt);
        dest.writeByte((byte) (isBookmark ? 1 : 0));
        dest.writeParcelable(rentTractorBrand, flags);
        dest.writeParcelable(tractorModel, flags);
    }

    @Override
    public String toString() {
        return "RentTractor{" +
                "rentTractorId='" + rentTractorId + '\'' +
                ", tractorModelId=" + tractorModelId +
                ", exactHp='" + exactHp + '\'' +
                ", userViews=" + userViews +
                ", tractorMonth='" + tractorMonth + '\'' +
                ", tractorYear=" + tractorYear +
                ", createdAt='" + createdAt + '\'' +
                ", isBookmark=" + isBookmark +
                ", rentTractorBrand=" + rentTractorBrand +
                ", tractorModel=" + tractorModel +
                '}';
    }
}
