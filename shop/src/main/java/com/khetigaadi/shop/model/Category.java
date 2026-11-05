package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;
import kotlin.jvm.Transient;
@Entity
public class Category implements Parcelable {
    @PrimaryKey
    @SerializedName("id")
    @Expose
    public int id = 0;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("priority")
    @Expose
    public int priority = 0;
    @SerializedName("home_priority")
    @Expose
    @Transient
    public int homePriority = 0;
    @SerializedName("color_code")
    @Expose
    @Transient
    public String colorCode;
    //public boolean isSelected = false;
    private boolean selected;
    public Category() {
    }
    protected Category(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        priority = in.readInt();
        homePriority = in.readInt();
        colorCode = in.readString();
        selected = in.readByte() != 0;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(priority);
        dest.writeInt(homePriority);
        dest.writeString(colorCode);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }
        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    public String getName() {
        return name;
    }
    public String getColorCode() {
        return colorCode;
    }
    public String getImage() {
        return image;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public int getId() {
        return id;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
//    public boolean getSelected(){
//        return selected;
//    }
    public boolean isSelected() {
        return selected;
    }
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", priority=" + priority +
                ", homePriority=" + homePriority +
                ", colorCode='" + colorCode + '\'' +
                ", selected=" + selected +
                '}';
    }
}