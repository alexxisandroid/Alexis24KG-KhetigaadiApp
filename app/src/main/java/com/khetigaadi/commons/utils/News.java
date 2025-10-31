package com.khetigaadi.commons.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.news.model.NewsImage;

/**
 * Created by sarads8 on 28-Oct-17.
 */

public class News {
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("post_author")
    @Expose
    private int postAuthor;
    @SerializedName("post_date")
    @Expose
    private String postDate;
    @SerializedName("post_date_gmt")
    @Expose
    private String postDateGmt;
    @SerializedName("post_content")
    @Expose
    private String postContent;
    @SerializedName("post_title")
    @Expose
    private String postTitle;
    @SerializedName("post_excerpt")
    @Expose
    private String postExcerpt;
    @SerializedName("post_status")
    @Expose
    private String postStatus;
    @SerializedName("comment_status")
    @Expose
    private String commentStatus;
    @SerializedName("ping_status")
    @Expose
    private String pingStatus;
    @SerializedName("post_password")
    @Expose
    private String postPassword;
    @SerializedName("post_name")
    @Expose
    private String postName;
    @SerializedName("to_ping")
    @Expose
    private String toPing;
    @SerializedName("pinged")
    @Expose
    private String pinged;
    @SerializedName("post_modified")
    @Expose
    private String postModified;
    @SerializedName("post_modified_gmt")
    @Expose
    private String postModifiedGmt;
    @SerializedName("post_content_filtered")
    @Expose
    private String postContentFiltered;
    @SerializedName("post_parent")
    @Expose
    private int postParent;
    @SerializedName("guid")
    @Expose
    private String guid;
    @SerializedName("menu_order")
    @Expose
    private int menuOrder;
    @SerializedName("post_type")
    @Expose
    private String postType;
    @SerializedName("post_mime_type")
    @Expose
    private String postMimeType;
    @SerializedName("comment_count")
    @Expose
    private int commentCount;
    @SerializedName("image")
    @Expose
    private NewsImage image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(int postAuthor) {
        this.postAuthor = postAuthor;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostDateGmt() {
        return postDateGmt;
    }

    public void setPostDateGmt(String postDateGmt) {
        this.postDateGmt = postDateGmt;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostExcerpt() {
        return postExcerpt;
    }

    public void setPostExcerpt(String postExcerpt) {
        this.postExcerpt = postExcerpt;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }

    public String getPostPassword() {
        return postPassword;
    }

    public void setPostPassword(String postPassword) {
        this.postPassword = postPassword;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getToPing() {
        return toPing;
    }

    public void setToPing(String toPing) {
        this.toPing = toPing;
    }

    public String getPinged() {
        return pinged;
    }

    public void setPinged(String pinged) {
        this.pinged = pinged;
    }

    public String getPostModified() {
        return postModified;
    }

    public void setPostModified(String postModified) {
        this.postModified = postModified;
    }

    public String getPostModifiedGmt() {
        return postModifiedGmt;
    }

    public void setPostModifiedGmt(String postModifiedGmt) {
        this.postModifiedGmt = postModifiedGmt;
    }

    public String getPostContentFiltered() {
        return postContentFiltered;
    }

    public void setPostContentFiltered(String postContentFiltered) {
        this.postContentFiltered = postContentFiltered;
    }

    public int getPostParent() {
        return postParent;
    }

    public void setPostParent(int postParent) {
        this.postParent = postParent;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(int menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostMimeType() {
        return postMimeType;
    }

    public void setPostMimeType(String postMimeType) {
        this.postMimeType = postMimeType;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public NewsImage getImage() {
        return image;
    }

    public void setImage(NewsImage image) {
        this.image = image;
    }
}