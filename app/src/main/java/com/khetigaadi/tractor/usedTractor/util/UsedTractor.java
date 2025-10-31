package com.khetigaadi.tractor.usedTractor.util;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.implement.newImplement.util.Brand;
//import com.khetigaadi.base.model.Model;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorImage;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.user.model.User;
import org.jetbrains.annotations.NotNull;
import java.util.List;
public class UsedTractor implements Parcelable {
    @SerializedName("used_tractor_id")
    @Expose
    private String id;
    @SerializedName("used_tractor_brand")
    @Expose
    private Brand brand;
    @SerializedName("used_tractor_model")
    @Expose
    private Model model;
    @SerializedName("drive")
    @Expose
    private Drive drive;
    @SerializedName("hp")
    @Expose
    private Hp hp;
    @SerializedName(value = "user", alternate = "users")
    @Expose
    private User user;
    @SerializedName("tractor_price")
    @Expose
    private String price;
    @SerializedName("hours")
    @Expose
    private String hoursDriven;
    @SerializedName("tractor_year")
    @Expose
    private String modelYear = null;
    @SerializedName("tractor_month")
    @Expose
    private String modelMonth = null;
    @SerializedName("single_used_tractor_image")
    @Expose
    private UsedTractorImage image = null;
    @SerializedName("view")
    @Expose
    private String userViews;
    @SerializedName("sold_status")
    @Expose
    private String soldUnsoldStatus;
    @SerializedName("is_bookmark")
    @Expose
    private boolean favoritesStatus;
    @SerializedName("tractor_rc_doc")
    @Expose
    private String RC;
    @SerializedName("tractor_owner")
    @Expose
    private String owner;
    @SerializedName("engine_condition")
    @Expose
    private String engineCondition;
    @SerializedName("tyre_condition")
    @Expose
    private String tyreCondition;
    @SerializedName("rto_no")
    @Expose
    private String registrationNo;
    @SerializedName("tractor_noc")
    @Expose
    private String tractorNoc;
    @SerializedName("exact_hp")
    @Expose
    private String exactHp;
    @SerializedName("about_us")
    @Expose
    private String about;
    @NotNull
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("multiple_used_tractor_image")
    @Expose
    private List<UsedTractorImage> multipleUsedTractorImage = null;
    @SerializedName("upload_from")
    @Expose
    private String uploadFrom = "android";
    @SerializedName("brand_id")
    @Expose
    private Integer brandId;
    public UsedTractor(Parcel in) {
        id = in.readString();
        brand = in.readParcelable(Brand.class.getClassLoader());
        model = in.readParcelable(Model.class.getClassLoader());
        drive = in.readParcelable(Drive.class.getClassLoader());
        hp = in.readParcelable(Hp.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
        price = in.readString();
        hoursDriven = in.readString();
        modelYear = in.readString();
        modelMonth = in.readString();
        userViews = in.readString();
        soldUnsoldStatus = in.readString();
        favoritesStatus = in.readByte() != 0;
        RC = in.readString();
        owner = in.readString();
        engineCondition = in.readString();
        tyreCondition = in.readString();
        registrationNo = in.readString();
        tractorNoc = in.readString();
        exactHp = in.readString();
        about = in.readString();
        createdAt = in.readString();
        uploadFrom = in.readString();
    }
    public static final Creator<UsedTractor> CREATOR = new Creator<UsedTractor>() {
        @Override
        public UsedTractor createFromParcel(Parcel in) {
            return new UsedTractor(in);
        }
        @Override
        public UsedTractor[] newArray(int size) {
            return new UsedTractor[size];
        }
    };
    public UsedTractor() {

    }
    public Integer getBrandId() {
        return brandId;
    }
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public Drive getDrive() {
        return drive;
    }
    public void setDrive(Drive drive) {
        this.drive = drive;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getHoursDriven() {
        return hoursDriven;
    }
    public void setHoursDriven(String hoursDriven) {
        this.hoursDriven = hoursDriven;
    }
    public UsedTractorImage getImage() {
        return image;
    }
    public void setImage(UsedTractorImage image) {
        this.image = image;
    }
    public String getUserViews() {
        return userViews;
    }
    public void setUserViews(String userViews) {
        this.userViews = userViews;
    }
    public Model getModel() {
        return model;
    }
    public void setModel(Model model) {
        this.model = model;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getModelYear() {
        return modelYear;
    }
    public void setModelYear(String modelYear) {
        this.modelYear = modelYear;
    }
    public String getModelMonth() {
        return modelMonth;
    }
    public void setModelMonth(String modelMonth) {
        this.modelMonth = modelMonth;
    }
    public String getSoldUnsoldStatus() {
        return soldUnsoldStatus;
    }
    public void setSoldUnsoldStatus(String soldUnsoldStatus) {
        this.soldUnsoldStatus = soldUnsoldStatus;
    }
    public boolean getFavoritesStatus() {
        return favoritesStatus;
    }
    public void setFavoritesStatus(boolean favoritesStatus) {
        this.favoritesStatus = favoritesStatus;
    }
    public @NotNull String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(@NotNull String createdAt) {
        this.createdAt = createdAt;
    }
    public String getTractorNoc() {
        return tractorNoc;
    }
    public void setTractorNoc(String tractorNoc) {
        this.tractorNoc = tractorNoc;
    }
    public String getExactHp() {
        return exactHp;
    }
    public void setExactHp(String exactHp) {
        this.exactHp = exactHp;
    }
    public List<UsedTractorImage> getMultipleUsedTractorImage() {
        return multipleUsedTractorImage;
    }
    public void setMultipleUsedTractorImage(List<UsedTractorImage> multipleUsedTractorImage) {
        this.multipleUsedTractorImage = multipleUsedTractorImage;
    }
    public String getUploadFrom() {
        return uploadFrom;
    }
    public void setUploadFrom(String uploadFrom) {
        this.uploadFrom = uploadFrom;
    }
    public Hp getHp() {
        return hp;
    }
    public void setHp(Hp hp) {
        this.hp = hp;
    }
    @NonNull
    @Override
    public String toString() {
        return "UsedTractor{" +
                "id='" + id + '\'' +
                ", brand=" + brand +
                ", model=" + model +
                ", drive=" + drive +
                ", user=" + user +
                ", price='" + price + '\'' +
                ", hoursDriven='" + hoursDriven + '\'' +
                ", modelYear='" + modelYear + '\'' +
                ", modelMonth='" + modelMonth + '\'' +
                ", image=" + image +
                ", userViews='" + userViews + '\'' +
                ", soldUnsoldStatus='" + soldUnsoldStatus + '\'' +
                ", favoritesStatus='" + favoritesStatus + '\'' +
                ", RC='" + RC + '\'' +
                ", owner='" + owner + '\'' +
                ", engineCondition='" + engineCondition + '\'' +
                ", tyreCondition='" + tyreCondition + '\'' +
                ", registrationNo='" + registrationNo + '\'' +
                ", about='" + about + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
    public void setRC(String rc) {
        this.RC = rc;
    }
    public String getRC() {
        return RC;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }
    public void setEngineCondition(String engineCondition) {
        this.engineCondition = engineCondition;
    }
    public String getEngineCondition() {
        return engineCondition;
    }
    public void setTyreCondition(String tyreCondition) {
        this.tyreCondition = tyreCondition;
    }
    public String getTyreCondition() {
        return tyreCondition;
    }
    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }
    public String getRegistrationNo() {
        return registrationNo;
    }
    public void setAbout(String about) {
        this.about = about;
    }
    public String getAbout() {
        return about;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeString(id);
        dest.writeParcelable(brand, flags);
        dest.writeParcelable(model, flags);
        dest.writeParcelable(drive, flags);
        dest.writeParcelable(hp, flags);
        dest.writeParcelable(user, flags);
        dest.writeString(price);
        dest.writeString(hoursDriven);
        dest.writeString(modelYear);
        dest.writeString(modelMonth);
        dest.writeString(userViews);
        dest.writeString(soldUnsoldStatus);
        dest.writeByte((byte) (favoritesStatus ? 1 : 0));
        dest.writeString(RC);
        dest.writeString(owner);
        dest.writeString(engineCondition);
        dest.writeString(tyreCondition);
        dest.writeString(registrationNo);
        dest.writeString(tractorNoc);
        dest.writeString(exactHp);
        dest.writeString(about);
        dest.writeString(createdAt);
        dest.writeString(uploadFrom);
    }
}
