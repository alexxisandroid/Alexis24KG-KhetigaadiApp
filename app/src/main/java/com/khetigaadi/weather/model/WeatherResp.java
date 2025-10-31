package com.khetigaadi.weather.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class WeatherResp implements Parcelable {
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("forecast")
    @Expose
    private Forecast forecast = null;
    protected WeatherResp(Parcel in) {
        location = in.readParcelable(Location.class.getClassLoader());
        current = in.readParcelable(Current.class.getClassLoader());
        forecast = in.readParcelable(Forecast.class.getClassLoader());
    }

    public static final Creator<WeatherResp> CREATOR = new Creator<WeatherResp>() {
        @Override
        public WeatherResp createFromParcel(Parcel in) {
            return new WeatherResp(in);
        }

        @Override
        public WeatherResp[] newArray(int size) {
            return new WeatherResp[size];
        }
    };

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }
    public Forecast getForecast() {
        return forecast;
    }
    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
        dest.writeParcelable(current, flags);
        dest.writeParcelable(forecast, flags);
    }
    @Override
    public String toString() {
        return "WeatherResp{" +
                "location=" + location +
                ", current=" + current +
                ", forecast=" + forecast +
                '}';
    }
}