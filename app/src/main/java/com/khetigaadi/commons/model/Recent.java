package com.khetigaadi.commons.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.def.RecentType;
@Entity
public class Recent implements Parcelable {
    @SerializedName("id")
    @Expose
    @ColumnInfo(defaultValue = "recent_id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(defaultValue = "name")
    @SerializedName(value = "name", alternate = "name")
    public String name;
    @ColumnInfo(defaultValue = "type")
    @SerializedName(value = "type", alternate = "type")
    private int type;
    @ColumnInfo(defaultValue = "image")
    @SerializedName(value = "image", alternate = "image")
    private String image;
    @ColumnInfo(defaultValue = "time")
    @SerializedName(value = "time", alternate = "time")
    private long time;
    public Recent() {
    }
    @Ignore
    public Recent(int id,String name,String image,@RecentType int type,long time) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.type = type;
        this.time = time;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeString(image);
        dest.writeLong(time);
    }
    protected Recent(Parcel in) {
        id = in.readInt();
        name = in.readString();
        type = in.readInt();
        image = in.readString();
        time = in.readLong();
    }
    public static final Creator<Recent> CREATOR = new Creator<Recent>() {
        @Override
        public Recent createFromParcel(Parcel in) {
            return new Recent(in);
        }

        @Override
        public Recent[] newArray(int size) {
            return new Recent[size];
        }
    };
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @RecentType
    public int getType() {
        return type;
    }
    public void setType(@RecentType int type) {
        this.type = type;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public String toString() {
        return "Recent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", image='" + image + '\'' +
                ", time=" + time +
                '}';
    }
}
