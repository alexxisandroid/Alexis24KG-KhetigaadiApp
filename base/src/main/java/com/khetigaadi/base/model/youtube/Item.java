package com.khetigaadi.base.model.youtube;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
public class Item extends ParentItem implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
//    @Nullable
//    public Snippet snippet;
    protected Item(Parcel in) {
        super(in);
        id = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }
        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
//    @Override
//    public String toString() {
//        return "Item{" +
//                "id='" + id + '\'' +
//                ", snippet=" + snippet +
//                '}';
//    }
}


//
//package com.khetigaadi.base.model.youtube;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//public class Item {
//    @NotNull
//    public  String contentDetails;
//    @Nullable
//    public String id;
//    @NotNull
//    public String kind;
//    @NotNull
//    public String etag;
//    @Nullable
//    public Snippet snippet;
//}
