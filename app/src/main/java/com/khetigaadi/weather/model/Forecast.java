package com.khetigaadi.weather.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class Forecast implements Parcelable {
    @SerializedName("forecastday")
    @Expose
    private List<ForecastDay> forecastDay = null;
    protected Forecast(Parcel in) {
    }
    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }
        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
    public List<ForecastDay> getForecastDay() {
        return forecastDay;
    }
    public void setForecastDay(List<ForecastDay> forecastDay) {
        this.forecastDay = forecastDay;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(@NonNull Parcel dest,int flags) {
    }
}