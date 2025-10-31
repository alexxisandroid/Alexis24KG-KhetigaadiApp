package com.khetigaadi.base.model.agri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class Others extends com.khetigaadi.base.model.user.Others implements Parcelable {
    @SerializedName(value = "postUploadUrlPath",alternate = "postCommentsUploadPath")
    @Expose
    private String postPath;
    @SerializedName(value = "postGroupIconPath", alternate = "groupImagePath")
    @Expose
    private String groupPath;
    @SerializedName(value = "postCropIconPath", alternate = "cropImagePath")
    @Expose
    private String cropPath;
    @SerializedName("likeIconPath")
    private String likeIconPath;
    @SerializedName("khetiguruPageLogoImagePath")
    @Expose
    private String pageLogoImagePath;
    @SerializedName("khetiguruPagePostImagePath")
    @Expose
    private String pagePostImagePath;
    @SerializedName("khetiguruPageCarouselImagePath")
    @Expose
    private String pageCarouselImagePath;
    public String getCropPath() {
        return cropPath;
    }
    public void setCropPath(String cropPath) {
        this.cropPath = cropPath;
    }
    public String getPostPath() {
        return postPath;
    }
    public void setPostPath(String postPath) {
        this.postPath = postPath;
    }
    public String getGroupPath() {
        return groupPath;
    }
    public void setGroupPath(String groupPath) {
        this.groupPath = groupPath;
    }
    public String getLikeIconPath() {
        return likeIconPath;
    }
    public void setLikeIconPath(String likeIconPath) {
        this.likeIconPath = likeIconPath;
    }
    public String getPageLogoImagePath() {
        return pageLogoImagePath;
    }
    public void setPageLogoImagePath(String pageLogoImagePath) {
        this.pageLogoImagePath = pageLogoImagePath;
    }
    public String getPagePostImagePath() {
        return pagePostImagePath;
    }
    public void setPagePostImagePath(String pagePostImagePath) {
        this.pagePostImagePath = pagePostImagePath;
    }
    public String getPageCarouselImagePath() {
        return pageCarouselImagePath;
    }
    public void setPageCarouselImagePath(String pageCarouselImagePath) {
        this.pageCarouselImagePath = pageCarouselImagePath;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cropPath);
    }
    public Others() {
    }
    protected Others(Parcel in) {
        this.cropPath = in.readString();
    }
    public static final Parcelable.Creator<Others> CREATOR = new Parcelable.Creator<Others>() {
        @Override
        public Others createFromParcel(Parcel source) {
            return new Others(source);
        }
        @Override
        public Others[] newArray(int size) {
            return new Others[size];
        }
    };
}






//package com.khetigaadi.base.model.agri;
//public class Others {
//    public String getUserPath() {
//        return "";
//    }
//    public void setUserPath(String userPath) {
//    }
//    public String getPostPath() {
//        return "";
//    }
//    public void setPostPath(String postPath) {
//    }
//    public String getGroupPath() {
//        return "";
//    }
//    public void setGroupPath(String groupPath) {
//    }
//    public String getCropPath() {
//        return "";
//    }
//    public void setCropPath(String cropPath) {
//    }
//}
