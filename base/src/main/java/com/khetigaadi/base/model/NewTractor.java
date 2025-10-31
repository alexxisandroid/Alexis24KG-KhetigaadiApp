package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//import com.khetigaadi.commons.model.Hp;
//import com.khetigaadi.commons.utils.Drive;
import java.util.List;
/**
 * Created by sarads8 on 11-Nov-17.
 */
public class NewTractor implements Parcelable {
    @SerializedName("tractor_name")
    private String name;
    @SerializedName("views")
    @Expose
    private String userView;
    @SerializedName("tractor_image")
    @Expose
    private TractorImage image;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("engine_rpm")
    @Expose
    private String engineRpm;
    @SerializedName("hp")
    @Expose
    private Hp hp;
    @SerializedName("exact_hp")
    @Expose
    private String exactHp;
    @SerializedName("pto_rpm")
    @Expose
    private String ptoRpm;
    @SerializedName("drive")
    @Expose
    private Drive drive;
    @SerializedName("tractor_model")
    @Expose
    private Model tractorModel;
    @SerializedName(value = "get_short_spec_data", alternate = {"get_my_fav_short_spec_data", "get_short_spec_data_detail"})
    @Expose
    private List<NewTractorShortSpec> getShortSpecData = null;
    @SerializedName("sponsor")
    @Expose
    private boolean sponsor;
    @SerializedName("is_bookmark")
    @Expose
    private boolean isBookmark;
    protected NewTractor(Parcel in) {
        name = in.readString();
        userView = in.readString();
        image = in.readParcelable(TractorImage.class.getClassLoader());
        brand = in.readParcelable(Brand.class.getClassLoader());
        engineRpm = in.readString();
        hp=in.readParcelable(Hp.class.getClassLoader());
        exactHp = in.readString();
        ptoRpm = in.readString();
        drive = in.readParcelable(Drive.class.getClassLoader());
        tractorModel = in.readParcelable(Model.class.getClassLoader());
        sponsor = in.readByte() != 0;
        isBookmark = in.readByte() != 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
      //  super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeString(userView);
        dest.writeParcelable(image, flags);
        dest.writeParcelable(brand, flags);
        dest.writeString(engineRpm);
        dest.writeParcelable(hp,flags);
        dest.writeString(exactHp);
        dest.writeString(ptoRpm);
        dest.writeParcelable(drive, flags);
        dest.writeParcelable(tractorModel, flags);
        dest.writeByte((byte) (sponsor ? 1 : 0));
        dest.writeByte((byte) (isBookmark ? 1 : 0));
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<NewTractor> CREATOR = new Creator<NewTractor>() {
        @Override
        public NewTractor createFromParcel(Parcel in) {
            return new NewTractor(in);
        }
        @Override
        public NewTractor[] newArray(int size) {
            return new NewTractor[size];
        }
    };
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserView() {
        return userView;
    }
    public void setUserView(String userView) {
        this.userView = userView;
    }
    public TractorImage getImage() {
        return image;
    }
    public void setImage(TractorImage image) {
        this.image = image;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public String getEngineRpm() {
        return engineRpm;
    }
    public void setEngineRpm(String engineRpm) {
        this.engineRpm = engineRpm;
    }
    public Hp getHp() {
        return hp;
    }
    public void setHp(Hp hp) {
        this.hp = hp;
    }
    public String getExactHp() {
        return exactHp;
    }
    public void setExactHp(String exactHp) {
        this.exactHp = exactHp;
    }
    public String getPtoRpm() {
        return ptoRpm;
    }
    public void setPtoRpm(String ptoRpm) {
        this.ptoRpm = ptoRpm;
    }
    public Drive getDrive() {
        return drive;
    }
    public void setDrive(Drive drive) {
        this.drive = drive;
    }
    public Model getTractorModel() {
        return tractorModel;
    }
    public void setTractorModel(Model tractorModel) {
        this.tractorModel = tractorModel;
    }
    public List<NewTractorShortSpec> getGetShortSpecData() {
        return getShortSpecData;
    }
    public void setGetShortSpecData(List<NewTractorShortSpec> getShortSpecData) {
        this.getShortSpecData = getShortSpecData;
    }
    public boolean isSponsor() {
        return sponsor;
    }
    public void setSponsor(boolean sponsor) {
        this.sponsor = sponsor;
    }
    public boolean isBookmark() {
        return isBookmark;
    }
    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }
    @Override
    public String toString() {
        return "NewTractor{" +
                "name='" + name + '\'' +
                ", userView='" + userView + '\'' +
                ", image=" + image +
                ", brand=" + brand +
                ", engineRpm='" + engineRpm + '\'' +
                ", hp=" + hp +
                ", exactHp='" + exactHp + '\'' +
                ", ptoRpm='" + ptoRpm + '\'' +
                ", drive=" + drive +
                ", tractorModel=" + tractorModel +
                ", getShortSpecData=" + getShortSpecData +
                ", sponsor=" + sponsor +
                ", isBookmark=" + isBookmark +
                '}';
    }
}
