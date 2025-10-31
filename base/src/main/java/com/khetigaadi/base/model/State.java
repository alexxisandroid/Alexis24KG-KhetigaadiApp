package com.khetigaadi.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class State implements Parcelable {
    @PrimaryKey
    @SerializedName("state_id")
    private int id;
    @SerializedName("state_name")
    private String name;
    @SerializedName("short_name")
    @Expose
    private String shortName;
    @SerializedName("state_position")
    @Expose
    private int statePosition;
    @SerializedName("image")
    @Expose
    private String image;

    public State(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected State(Parcel in) {
        id = in.readInt();
        name = in.readString();
        shortName = in.readString();
        statePosition = in.readInt();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(shortName);
        dest.writeInt(statePosition);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getStatePosition() {
        return statePosition;
    }

    public void setStatePosition(int statePosition) {
        this.statePosition = statePosition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", statePosition=" + statePosition +
                ", image='" + image + '\'' +
                '}';
    }

//    @NonNull
//    @Override
//    public String toString() {
//        return name;
//    }
}
