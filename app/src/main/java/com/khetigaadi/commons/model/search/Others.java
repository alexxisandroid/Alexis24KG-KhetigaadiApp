package com.khetigaadi.commons.model.search;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
public class Others implements Parcelable {
    @NotNull
    public final String usedTractorImagePath;
    protected Others(Parcel in) {
        usedTractorImagePath = in.readString();
        tractorImagePath = in.readString();
    }
    public static final Creator<Others> CREATOR = new Creator<Others>() {
        @Override
        public Others createFromParcel(Parcel in) {
            return new Others(in);
        }
        @Override
        public Others[] newArray(int size) {
            return new Others[size];
        }
    };
    @NonNull
    public String getTractorImagePath() {
        return tractorImagePath;
    }
    public void setTractorImagePath(@NonNull String tractorImagePath) {
        this.tractorImagePath = tractorImagePath;
    }
    @NotNull
    public String tractorImagePath="";
    public Others(@NotNull String usedTractorImagePath) {
        this.usedTractorImagePath = usedTractorImagePath;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(usedTractorImagePath);
        dest.writeString(tractorImagePath);
    }
    @Override
    public String toString() {
        return "Others{" +
                "usedTractorImagePath='" + usedTractorImagePath + '\'' +
                ", tractorImagePath='" + tractorImagePath + '\'' +
                '}';
    }
}
