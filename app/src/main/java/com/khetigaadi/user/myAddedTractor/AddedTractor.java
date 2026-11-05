package com.khetigaadi.user.myAddedTractor;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.model.Model;
import com.khetigaadi.tractor.util.MyTractorImage;
import com.khetigaadi.user.model.User;
import java.io.Serializable;
import java.util.List;

public class AddedTractor implements Parcelable {
    @SerializedName("id")
    @Expose
    private String tractorId;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("tractor_model_id")
    @Expose
    private String tractorModelId;
    @SerializedName("tractor_year")
    @Expose
    private String tractorYear;
    @SerializedName("tractor_month")
    @Expose
    private String tractorMonth;
    @SerializedName("upload_from")
    @Expose
    private String uploadFrom;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("tractor_model")
    @Expose
    private Model tractorModel;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("images")
    @Expose
    private List<MyTractorImage> images;
    public AddedTractor() {

    }
    protected AddedTractor(Parcel in) {
        tractorId = in.readString();
        brandId = in.readString();
        userId = in.readString();
        tractorModelId = in.readString();
        tractorYear = in.readString();
        tractorMonth = in.readString();
        uploadFrom = in.readString();
        brand = in.readParcelable(Brand.class.getClassLoader());
        tractorModel = in.readParcelable(Model.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }
    public static final Creator<AddedTractor> CREATOR = new Creator<AddedTractor>() {
        @Override
        public AddedTractor createFromParcel(Parcel in) {
            return new AddedTractor(in);
        }
        @Override
        public AddedTractor[] newArray(int size) {
            return new AddedTractor[size];
        }
    };
    public String getTractorId() {
        return tractorId;
    }
    public void setTractorId(String tractorId) {
        this.tractorId = tractorId;
    }
    public String getBrandId() {
        return brandId;
    }
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTractorModelId() {
        return tractorModelId;
    }
    public void setTractorModelId(String tractorModelId) {
        this.tractorModelId = tractorModelId;
    }
    public String getTractorYear() {
        return tractorYear;
    }
    public void setTractorYear(String tractorYear) {
        this.tractorYear = tractorYear;
    }
    public String getTractorMonth() {
        return tractorMonth;
    }
    public void setTractorMonth(String tractorMonth) {
        this.tractorMonth = tractorMonth;
    }
    public String getUploadFrom() {
        return uploadFrom;
    }
    public void setUploadFrom(String uploadFrom) {
        this.uploadFrom = uploadFrom;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public Model getTractorModel() {
        return tractorModel;
    }
    public void setTractorModel(Model tractorModel) {
        this.tractorModel = tractorModel;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public List<MyTractorImage> getImages() {
        return images;
    }
    public void setImages(List<MyTractorImage> images) {
        this.images = images;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeString(tractorId);
        dest.writeString(brandId);
        dest.writeString(userId);
        dest.writeString(tractorModelId);
        dest.writeString(tractorYear);
        dest.writeString(tractorMonth);
        dest.writeString(uploadFrom);
        dest.writeParcelable(brand, flags);
        dest.writeParcelable(tractorModel, flags);
        dest.writeParcelable(user, flags);
    }
    @NonNull
    @Override
    public String toString() {
        return "AddedTractor{" +
                "tractorId='" + tractorId + '\'' +
                ", brandId='" + brandId + '\'' +
                ", userId='" + userId + '\'' +
                ", tractorModelId='" + tractorModelId + '\'' +
                ", tractorYear='" + tractorYear + '\'' +
                ", tractorMonth='" + tractorMonth + '\'' +
                ", uploadFrom='" + uploadFrom + '\'' +
                ", brand=" + brand +
                ", tractorModel=" + tractorModel +
                ", user=" + user +
                ", images=" + images +
                '}';
    }
}
