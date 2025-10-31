package com.khetigaadi.user.model;
import com.khetigaadi.base.model.agri.Crop;
import java.util.List;
public class Others {
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    private String token;
    private String city;
    private String image;
    public String getFname() {
        return Fname;
    }
    public void setFname(String fname) {
        Fname = fname;
    }
    private String Fname;
    public String getLname() {
        return Lname;
    }
    public void setLname(String lname) {
        Lname = lname;
    }
    private String Lname;
    public void setId(String id) {
        this.id = id;
    }
    private String id;
    public String getId() {
        return id;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    private String state;
    public boolean isFollowerStatus() {
        return false;
    }
    private long followerCount;
    public long getFollowerCount() {
        return followerCount;
    }
    public void setFollowerCount(long followerCount) {
        this.followerCount = followerCount;
    }
    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }
    private long followingCount;
    public long getFollowingCount() {
        return followingCount;
    }
    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }
    private long likeCount;
    public long getLikeCount() {
        return likeCount;
    }
    public long getPostCount() {
        return postCount;
    }
    public void setPostCount(long postCount) {
        this.postCount = postCount;
    }
    private long postCount;
    public long getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }
    private long commentCount;
    public void setShareCount(long shareCount) {
        this.shareCount = shareCount;
    }
    private long shareCount;
    public long getShareCount() {
        return shareCount;
    }
    public void setLiked(boolean liked) {
        isLiked = liked;
    }
    private boolean isLiked;
    public boolean isLiked() {
        return isLiked;
    }
    public Others getUserDetail() {
        return userDetail;
    }
    public void setUserDetail(Others userDetails) {
        this.userDetail = userDetails;
    }
    private Others userDetail;
    public String getAwards() {
        return awards;
    }
    public void setAwards(String awards) {
        this.awards = awards;
    }
    private String awards;
    public void setType(Others type) {
        this.type = type;
    }
    private Others type;
    public Others getType() {
        return type;
    }
    public String getUserLevelName() {
        return userLevelName;
    }
    public void setUserLevelName(String userLevelName) {
        this.userLevelName = userLevelName;
    }
    private String userLevelName;
    public List<Crop> getCrops() {
        return crops;
    }
    public void setCrops(List<Crop> crops) {
        this.crops = crops;
    }
    private List<Crop> crops;
    public void setBiography(String biography) {
        this.biography = biography;
    }
    private String biography;
    public String getBiography() {
        return biography;
    }
    public String getRegisteredDate() {
        return registeredDate;
    }
    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }
    private String registeredDate;
    public void setFollowerStatus(boolean followerStatus) {
        this.followerStatus = followerStatus;
    }
    private boolean followerStatus;
}
