package com.khetigaadi.base.model.youtube;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class VideoResp implements Parcelable {
    @SerializedName("nextPageToken")
    @Expose
    private String nextPageToken;
    @SerializedName("items")
    @Expose
    private List<Item> items;
    protected VideoResp(Parcel in) {
        nextPageToken = in.readString();
        items = in.createTypedArrayList(Item.CREATOR);
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeString(nextPageToken);
        dest.writeTypedList(items);
    }
    public static final Creator<VideoResp> CREATOR = new Creator<VideoResp>() {
        @Override
        public VideoResp createFromParcel(Parcel in) {
            return new VideoResp(in);
        }
        @Override
        public VideoResp[] newArray(int size) {
            return new VideoResp[size];
        }
    };
    @Override
    public String toString() {
        return "VideoResp{" +
                "nextPageToken='" + nextPageToken + '\'' +
                ", items=" + items +
                '}';
    }
}
