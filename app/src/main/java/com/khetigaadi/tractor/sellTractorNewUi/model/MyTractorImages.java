package com.khetigaadi.tractor.sellTractorNewUi.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@Entity
public class MyTractorImages implements Parcelable {
    @SerializedName("id")
    @Expose
    @ColumnInfo(defaultValue = "myTractorId")
    @PrimaryKey(autoGenerate = true)
    private int myTractorId;
    @SerializedName("tractor_owner_id")
    @Expose
    @ColumnInfo(defaultValue = "tractorOwnerId")
    private int tractor_owner_id;
    // name
    @SerializedName("name")
    @Expose
    @ColumnInfo(defaultValue = "imageName")
    private String imageName;
    protected MyTractorImages(Parcel in) {
        myTractorId = in.readInt();
        tractor_owner_id = in.readInt();
        imageName = in.readString();
    }
    public static final Creator<MyTractorImages> CREATOR = new Creator<MyTractorImages>() {
        @Override
        public MyTractorImages createFromParcel(Parcel in) {
            return new MyTractorImages(in);
        }
        @Override
        public MyTractorImages[] newArray(int size) {
            return new MyTractorImages[size];
        }
    };
    public MyTractorImages() {

    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(myTractorId);
        dest.writeInt(tractor_owner_id);
        dest.writeString(imageName);
    }
    public int getMyTractorId() {
        return myTractorId;
    }
    public void setMyTractorId(int myTractorId) {
        this.myTractorId = myTractorId;
    }
    public int getTractor_owner_id() {
        return tractor_owner_id;
    }
    public void setTractor_owner_id(int tractor_owner_id) {
        this.tractor_owner_id = tractor_owner_id;
    }
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    @Override
    public String toString() {
        return "MyTractorImages{" +
                "myTractorId=" + myTractorId +
                ", tractor_owner_id=" + tractor_owner_id +
                ", imageName='" + imageName + '\'' +
                '}';
    }
}
