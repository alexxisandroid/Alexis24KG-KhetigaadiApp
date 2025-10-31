package com.khetigaadi.base.model.youtube;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParentItem implements Parcelable {
    @SerializedName("kind")
    @Expose
    private String kind;
    @SerializedName("etag")
    @Expose
    private String etag;
    @SerializedName("snippet")
    @Expose
    private Snippet snippet;
    @SerializedName("contentDetails")
    @Expose
    private ContentDetails contentDetails;

    public ParentItem() {
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    protected ParentItem(Parcel in) {
        kind = in.readString();
        etag = in.readString();
        snippet = in.readParcelable(Snippet.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kind);
        dest.writeString(etag);
        dest.writeParcelable(snippet, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParentItem> CREATOR = new Creator<ParentItem>() {
        @Override
        public ParentItem createFromParcel(Parcel in) {
            return new ParentItem(in);
        }

        @Override
        public ParentItem[] newArray(int size) {
            return new ParentItem[size];
        }
    };

    @Override
    public String toString() {
        return "ParentItem{" +
                "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", snippet=" + snippet +
                ", contentDetails=" + contentDetails +
                '}';
    }
}
