package com.khetigaadi.tractor.newTractor.model;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.TypeConverters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class TractorFilter implements Parcelable {
    @SerializedName("filter")
    @TypeConverters(FilterDataTypeConverter.class)
    @Expose
    private Filter filter;
    protected TractorFilter(Parcel in) {
        filter = in.readParcelable(Filter.class.getClassLoader());
    }
    public static final Creator<TractorFilter> CREATOR = new Creator<TractorFilter>() {
        @Override
        public TractorFilter createFromParcel(Parcel in) {
            return new TractorFilter(in);
        }
        @Override
        public TractorFilter[] newArray(int size) {
            return new TractorFilter[size];
        }
    };
    public TractorFilter() {
    }
    public TractorFilter(Filter filter) {
        this.filter = filter;
    }
    public Filter getFilter() {
        return filter;
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
        dest.writeParcelable(filter,flags);
    }
    @Override
    public String toString() {
        return "TractorFilter{" +
                "filter=" + filter +
                '}';
    }
}
