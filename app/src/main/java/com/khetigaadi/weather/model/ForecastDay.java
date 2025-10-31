package com.khetigaadi.weather.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ForecastDay implements Parcelable {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_epoch")
    @Expose
    private int dateEpoch;
    @SerializedName("day")
    @Expose
    private Day day;
    protected ForecastDay(Parcel in) {
        date = in.readString();
        dateEpoch = in.readInt();
        day = in.readParcelable(Day.class.getClassLoader());
        isSelected = in.readByte() != 0;
    }

    public static final Creator<ForecastDay> CREATOR = new Creator<ForecastDay>() {
        @Override
        public ForecastDay createFromParcel(Parcel in) {
            return new ForecastDay(in);
        }

        @Override
        public ForecastDay[] newArray(int size) {
            return new ForecastDay[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    private boolean isSelected;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDateEpoch() {
        return dateEpoch;
    }

    public void setDateEpoch(int dateEpoch) {
        this.dateEpoch = dateEpoch;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public void setSelected(boolean isSelected) {

        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(dateEpoch);
        dest.writeParcelable(day, flags);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}
