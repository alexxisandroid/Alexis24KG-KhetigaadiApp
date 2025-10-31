package com.khetigaadi.base.model.youtube;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ListItem extends ParentItem implements Parcelable {
    @SerializedName("id")
    @Expose
    private Id id;
    public ListItem(Id id) {
        this.id = id;
    }
    protected ListItem(Parcel in) {
        super(in);
        id = in.readParcelable(Id.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(id, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }
        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };
    public Id getId() {
        return id;
    }
    public void setId(Id id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ListItem{" +
                "id=" + id +
                '}';
    }
}







//package com.khetigaadi.base.model.youtube;
//import org.jetbrains.annotations.NotNull;
//public class ListItem {
//    @NotNull
//    public String contentDetails;
//
//    public ListItem(Id id) {
//        this.id = id;
//    }
//    @NotNull
//    public Object etag;
//    @NotNull
//    public String kind;
//    @NotNull
//    public Id getId() {
//        return id;
//    }
//    public void setId(@NotNull Id id) {
//        this.id = id;
//    }
//    @NotNull
//    public String idd;
//    public Id id;
//    public String getVideoId() {
//        return videoId;
//    }
//    public void setVideoId(String videoId) {
//        this.videoId = videoId;
//    }
//    public String videoId;
//    public Snippet getSnippet() {
//        return snippet;
//    }
//    public void setSnippet(Snippet snippet) {
//        this.snippet = snippet;
//    }
//    private Snippet snippet;
//}
