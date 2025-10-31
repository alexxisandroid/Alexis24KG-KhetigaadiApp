package com.khetigaadi.news.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.user.model.User;
public class Comment implements Parcelable {
    @SerializedName("users")
    @Expose
    private User user;
    @SerializedName("news_comment_id")
    @Expose
    private String commentId;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("likes_count")
    @Expose
    private Integer likes = 0;
    @SerializedName("reply_count")
    @Expose
    private Integer replies=0;
    @SerializedName("isLike")
    @Expose
    private boolean isLike;
    @SerializedName("created_at")
    @Expose
    private String postedDate;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getReplies() {
        return replies;
    }

    public void setReplies(Integer replies) {
        this.replies = replies;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    @NonNull
    @Override
    public String toString() {
        return "Comment{" +
                "user=" + user +
                ", commentId=" + commentId +
                ", comment='" + comment + '\'' +
                ", likes=" + likes +
                ", replies=" + replies +
                ", isLike=" + isLike +
                ", postedDate='" + postedDate + '\'' +
                '}';
    }

    public Comment() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.commentId);
        dest.writeString(this.comment);
        dest.writeValue(this.likes);
        dest.writeValue(this.replies);
        dest.writeByte(this.isLike ? (byte) 1 : (byte) 0);
        dest.writeString(this.postedDate);
    }

    protected Comment(Parcel in) {
        this.user = in.readParcelable(User.class.getClassLoader());
        this.commentId = in.readString();
        this.comment = in.readString();
        this.likes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.replies = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isLike = in.readByte() != 0;
        this.postedDate = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
