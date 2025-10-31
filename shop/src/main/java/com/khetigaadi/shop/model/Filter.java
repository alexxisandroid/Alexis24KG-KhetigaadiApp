package com.khetigaadi.shop.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.Price;
//import com.khetigaadi.shop.model.category.Category;
import java.util.List;
public class Filter implements Parcelable {
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    public Price getPrice() {
        return price;
    }
    public void setPrice(Price price) {
        this.price = price;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    @NonNull
    @Override
    public String toString() {
        return "Filter{" +
                "price=" + price +
                ", categories=" + categories +
                '}';
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.price, flags);
        dest.writeTypedList(this.categories);
    }
    public Filter() {
    }
    protected Filter(Parcel in) {
        this.price = in.readParcelable(Price.class.getClassLoader());
        this.categories = in.createTypedArrayList(Category.CREATOR);
    }
    public static final Creator<Filter> CREATOR = new Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel source) {
            return new Filter(source);
        }
        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}
