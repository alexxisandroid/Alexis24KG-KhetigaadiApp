package com.khetigaadi.user.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.State;
import com.khetigaadi.base.model.Taluka;
public class User implements Parcelable {
    String password;
    @SerializedName("user_id")
    @Expose
    private String id;
    @SerializedName("first_name")
    @Expose
    private String fname;
    @SerializedName("last_name")
    @Expose
    private String lname;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("otp")
    @Expose
    private int otp;
    @SerializedName("state")
    @Expose
    private State state;
    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("taluka")
    @Expose
    private Taluka taluka;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user_detail")
    @Expose
    private UserDetail userDetail;
    @SerializedName("created_at")
    @Expose
    private String registeredDate;
    //We need to give a path
    @SerializedName(value = "user_image", alternate = {"image_name"})
    @Expose
    private String image;
    private String address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("posts_count")
    @Expose
    private int postCount;
    @SerializedName("likes_count")
    @Expose
    private int likeCount;
    @SerializedName("follower_count")
    @Expose
    private int followerCount;
    @SerializedName("following_count")
    @Expose
    private int followingCount;
    @SerializedName("comments_count")
    @Expose
    private int commentCount;
    @SerializedName("share_count")
    @Expose
    private int shareCount;
    @SerializedName("follower_status")
    @Expose
    private boolean followerStatus;
    @SerializedName("is_like")
    @Expose
    private boolean liked;
    @SerializedName("is_init_pwd")
    @Expose
    private int isInitPwd = 1;
    public User() {
    }
    protected User(Parcel in) {
        password = in.readString();
        id = in.readString();
        fname = in.readString();
        lname = in.readString();
        mobile = in.readString();
        otp = in.readInt();
        state = in.readParcelable(State.class.getClassLoader());
        city = in.readParcelable(City.class.getClassLoader());
        taluka = in.readParcelable(Taluka.class.getClassLoader());
        token = in.readString();
        userDetail = in.readParcelable(UserDetail.class.getClassLoader());
        registeredDate = in.readString();
        image = in.readString();
        address = in.readString();
        email = in.readString();
        postCount = in.readInt();
        likeCount = in.readInt();
        followerCount = in.readInt();
        followingCount = in.readInt();
        commentCount = in.readInt();
        isInitPwd = in.readInt();
        shareCount = in.readInt();
        followerStatus = in.readByte() != 0;
        liked = in.readByte() != 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeString(password);
        dest.writeString(id);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(mobile);
        dest.writeInt(otp);
        dest.writeParcelable(state, flags);
        dest.writeParcelable(city, flags);
        dest.writeParcelable(taluka, flags);
        dest.writeString(token);
        dest.writeParcelable(userDetail, flags);
        dest.writeString(registeredDate);
        dest.writeString(image);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeInt(postCount);
        dest.writeInt(likeCount);
        dest.writeInt(followerCount);
        dest.writeInt(followingCount);
        dest.writeInt(commentCount);
        dest.writeInt(shareCount);
        dest.writeInt(isInitPwd);
        dest.writeByte((byte) (followerStatus ? 1 : 0));
        dest.writeByte((byte) (liked ? 1 : 0));
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    public int getOtp() {
        return otp;
    }
    public void setOtp(int otp) {
        this.otp = otp;
    }
    public String getPassword() {
        return password;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public UserDetail getUserDetail() {
        return userDetail;
    }
    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }
    public Taluka getTaluka() {
        return taluka;
    }
    public void setTaluka(Taluka taluka) {
        this.taluka = taluka;
    }
    public String getRegisteredDate() {
        return registeredDate;
    }
    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getPostCount() {
        return postCount;
    }
    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    public int getFollowerCount() {
        return followerCount;
    }
    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
    public int getFollowingCount() {
        return followingCount;
    }
    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
    public int getShareCount() {
        return shareCount;
    }
    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
    public boolean isFollowerStatus() {
        return followerStatus;
    }
    public void setFollowerStatus(boolean followerStatus) {
        this.followerStatus = followerStatus;
    }
    public boolean isLiked() {
        return liked;
    }
    public void setLiked(boolean liked) {
        this.liked = liked;
    }
    public int getIsInitPwd() {
        return isInitPwd;
    }
    public void setIsInitPwd(int isInitPwd) {
        this.isInitPwd = isInitPwd;
    }
    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", id='" + id + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", mobile='" + mobile + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", registeredDate='" + registeredDate + '\'' +
                ", image='" + image + '\'' +
                ", address='" + address + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", postCount=" + postCount +
                ", likeCount=" + likeCount +
                ", followerCount=" + followerCount +
                ", commentCount=" + commentCount +
                ", shareCount=" + shareCount +
                ", followerStatus=" + followerStatus +
                ", liked=" + liked +
                '}';
    }
}
