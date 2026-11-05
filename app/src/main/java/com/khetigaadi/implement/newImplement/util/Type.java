package com.khetigaadi.implement.newImplement.util;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;
@Entity
public class Type implements Parcelable {
    @SerializedName("implement_type_id")
    @PrimaryKey
    @ColumnInfo(name = "implement_type_id")
    private int id;
    @SerializedName("implement_type_name")
    @ColumnInfo(name = "implement_type_name")
    private String name;
    @SerializedName("image")
    @ColumnInfo(name = "implement_type_image")
    private String image;
    @ColumnInfo(name = "implement_type_status")
    @SerializedName("status")
    private int status;
    @Ignore
    private boolean selected;
    public Type() {
    }
    @Ignore
    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }
    protected Type(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        status = in.readInt();
        selected = in.readByte() != 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(status);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }
        @Override
        public Type[] newArray(int size) {
            return new Type[size];
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
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean getSelected() {
        return selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return id == type.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id,name,image,status,selected);
    }

    @NonNull
    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", status=" + status +
                ", selected=" + selected +
                '}';
    }
}
