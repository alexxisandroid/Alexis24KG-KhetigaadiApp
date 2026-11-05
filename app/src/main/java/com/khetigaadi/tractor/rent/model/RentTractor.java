package com.khetigaadi.tractor.rent.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.Model;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentTractorImplement;
import com.khetigaadi.user.model.User;
import java.io.Serializable;
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
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("rent_tractor_implement")
    @Expose
    private RentTractorImplement rentTractorImplement;
    @SerializedName("rent_tractor_implements")
    @Expose
    private List<RentTractorImplement> rentTractorImplements = null;
    @SerializedName("drive")
    @Expose
    private Drive drive;
    @SerializedName("rent_tractor_brand")
    @Expose
    private Brand rentTractorBrand;
    @SerializedName("tractor_model")
    @Expose
    private Model tractorModel;
    @SerializedName("rent_tractor_table")
    @Expose
    private RentTractorTable rentTractorTable;
    @SerializedName("hp")
    @Expose
    private Hp hp;
    protected RentTractor(Parcel in) {
        rentTractorId = in.readString();
        tractorModelId = in.readInt();
        exactHp = in.readString();
        userViews = in.readInt();
        tractorMonth = in.readString();
        tractorYear = in.readInt();
        createdAt = in.readString();
        isBookmark = in.readByte() != 0;
        user = in.readParcelable(User.class.getClassLoader());
        rentTractorImplement = in.readParcelable(RentTractorImplement.class.getClassLoader());
        rentTractorImplements = in.createTypedArrayList(RentTractorImplement.CREATOR);
        drive = in.readParcelable(Drive.class.getClassLoader());
        rentTractorBrand = in.readParcelable(Brand.class.getClassLoader());
        tractorModel = in.readParcelable(Model.class.getClassLoader());
        hp = in.readParcelable(Hp.class.getClassLoader());
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
    public RentTractor() {
    }
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
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public RentTractorImplement getRentTractorImplement() {
        return rentTractorImplement;
    }
    public void setRentTractorImplement(RentTractorImplement rentTractorImplement) {
        this.rentTractorImplement = rentTractorImplement;
    }
    public List<RentTractorImplement> getRentTractorImplements() {
        return rentTractorImplements;
    }
    public void setRentTractorImplements(List<RentTractorImplement> rentTractorImplements) {
        this.rentTractorImplements = rentTractorImplements;
    }
    public Hp getHp() {
        return hp;
    }
    public void setHp(Hp hp) {
        this.hp = hp;
    }
    public Drive getDrive() {
        return drive;
    }
    public void setDrive(Drive drive) {
        this.drive = drive;
    }
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
    public RentTractorTable getRentTractorTable() {
        return rentTractorTable;
    }
    public void setRentTractorTable(RentTractorTable rentTractorTable) {
        this.rentTractorTable = rentTractorTable;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeString(rentTractorId);
        dest.writeInt(tractorModelId);
        dest.writeString(exactHp);
        dest.writeInt(userViews);
        dest.writeString(tractorMonth);
        dest.writeInt(tractorYear);
        dest.writeString(createdAt);
        dest.writeByte((byte) (isBookmark ? 1 : 0));
        dest.writeParcelable(user, flags);
        dest.writeParcelable(rentTractorImplement, flags);
        dest.writeTypedList(rentTractorImplements);
        dest.writeParcelable(drive, flags);
        dest.writeParcelable(rentTractorBrand, flags);
        dest.writeParcelable(tractorModel, flags);
        dest.writeParcelable(hp, flags);
    }
}
