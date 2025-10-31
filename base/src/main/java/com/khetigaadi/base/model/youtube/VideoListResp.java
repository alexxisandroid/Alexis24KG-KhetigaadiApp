package com.khetigaadi.base.model.youtube;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class VideoListResp implements Parcelable {
    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;
    @SerializedName("items")
    @Expose
    private List<ListItem> items = null;
    protected VideoListResp(Parcel in) {
        nextPageToken = in.readString();
        items = in.createTypedArrayList(ListItem.CREATOR);
    }
    public String getNextPageToken() {
        return nextPageToken;
    }
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
    public List<ListItem> getItems() {
        return items;
    }
    public void setItems(List<ListItem> items) {
        this.items = items;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(nextPageToken);
        dest.writeTypedList(items);
    }
    public static final Creator<VideoListResp> CREATOR = new Creator<VideoListResp>() {
        @Override
        public VideoListResp createFromParcel(Parcel in) {
            return new VideoListResp(in);
        }

        @Override
        public VideoListResp[] newArray(int size) {
            return new VideoListResp[size];
        }
    };
    @Override
    public String toString() {
        return "VideoListResp{" +
                "nextPageToken='" + nextPageToken + '\'' +
                ", items=" + items +
                '}';
    }
}

