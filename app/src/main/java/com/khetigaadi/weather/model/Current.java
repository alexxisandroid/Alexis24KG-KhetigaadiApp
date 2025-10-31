package com.khetigaadi.weather.model;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Deshmukh Namdev(namdev.deshmukh@gmail.com on 27-01-2019.
 */
public class Current implements Parcelable {
    @SerializedName("last_updated_epoch")
    @Expose
    private int lastUpdatedEpoch;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;
    @SerializedName("temp_c")
    @Expose
    private double tempC;
    @SerializedName("temp_f")
    @Expose
    private double tempF;
    @SerializedName("is_day")
    @Expose
    private int isDay;
    @SerializedName("condition")
    @Expose
    private ConditionImage condition;
    @SerializedName("wind_mph")
    @Expose
    private double windMph;
    @SerializedName("wind_kph")
    @Expose
    private double windKph;
    @SerializedName("wind_degree")
    @Expose
    private int windDegree;
    @SerializedName("wind_dir")
    @Expose
    private String windDir;
    @SerializedName("pressure_mb")
    @Expose
    private double pressureMb;
    @SerializedName("pressure_in")
    @Expose
    private double pressureIn;
    @SerializedName("precip_mm")
    @Expose
    private double precipMm;
    @SerializedName("precip_in")
    @Expose
    private double precipIn;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("cloud")
    @Expose
    private int cloud;
    @SerializedName("feelslike_c")
    @Expose
    private double feelslikeC;
    @SerializedName("feelslike_f")
    @Expose
    private double feelslikeF;
    @SerializedName("vis_km")
    @Expose
    private double visKm;
    @SerializedName("vis_miles")
    @Expose
    private double visMiles;
    @SerializedName("uv")
    @Expose
    private double uv;
    @SerializedName("gust_mph")
    @Expose
    private double gustMph;
    @SerializedName("gust_kph")
    @Expose
    private double gustKph;

    protected Current(Parcel in) {
        lastUpdatedEpoch = in.readInt();
        lastUpdated = in.readString();
        tempC = in.readDouble();
        tempF = in.readDouble();
        isDay = in.readInt();
        condition = in.readParcelable(ConditionImage.class.getClassLoader());
        windMph = in.readDouble();
        windKph = in.readDouble();
        windDegree = in.readInt();
        windDir = in.readString();
        pressureMb = in.readDouble();
        pressureIn = in.readDouble();
        precipMm = in.readDouble();
        precipIn = in.readDouble();
        humidity = in.readInt();
        cloud = in.readInt();
        feelslikeC = in.readDouble();
        feelslikeF = in.readDouble();
        visKm = in.readDouble();
        visMiles = in.readDouble();
        uv = in.readDouble();
        gustMph = in.readDouble();
        gustKph = in.readDouble();
    }

    public static final Creator<Current> CREATOR = new Creator<Current>() {
        @Override
        public Current createFromParcel(Parcel in) {
            return new Current(in);
        }

        @Override
        public Current[] newArray(int size) {
            return new Current[size];
        }
    };

    public int getLastUpdatedEpoch() {
        return lastUpdatedEpoch;
    }
    public void setLastUpdatedEpoch(int lastUpdatedEpoch) {
        this.lastUpdatedEpoch = lastUpdatedEpoch;
    }
    public String getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public double getTempC() {
        return tempC;
    }
    public void setTempC(double tempC) {
        this.tempC = tempC;
    }
    public double getTempF() {
        return tempF;
    }
    public void setTempF(double tempF) {
        this.tempF = tempF;
    }
    public int getIsDay() {
        return isDay;
    }
    public void setIsDay(int isDay) {
        this.isDay = isDay;
    }
    public ConditionImage getCondition() {
        return condition;
    }
    public void setCondition(ConditionImage condition) {
        this.condition = condition;
    }
    public double getWindMph() {
        return windMph;
    }
    public void setWindMph(double windMph) {
        this.windMph = windMph;
    }
    public double getWindKph() {
        return windKph;
    }
    public void setWindKph(double windKph) {
        this.windKph = windKph;
    }
    public int getWindDegree() {
        return windDegree;
    }
    public void setWindDegree(int windDegree) {
        this.windDegree = windDegree;
    }
    public String getWindDir() {
        return windDir;
    }
    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }
    public double getPressureMb() {
        return pressureMb;
    }
    public void setPressureMb(double pressureMb) {
        this.pressureMb = pressureMb;
    }
    public double getPressureIn() {
        return pressureIn;
    }
    public void setPressureIn(double pressureIn) {
        this.pressureIn = pressureIn;
    }
    public double getPrecipMm() {
        return precipMm;
    }
    public void setPrecipMm(double precipMm) {
        this.precipMm = precipMm;
    }
    public double getPrecipIn() {
        return precipIn;
    }
    public void setPrecipIn(double precipIn) {
        this.precipIn = precipIn;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public int getCloud() {
        return cloud;
    }
    public void setCloud(int cloud) {
        this.cloud = cloud;
    }
    public double getFeelslikeC() {
        return feelslikeC;
    }
    public void setFeelslikeC(double feelslikeC) {
        this.feelslikeC = feelslikeC;
    }
    public double getFeelslikeF() {
        return feelslikeF;
    }
    public void setFeelslikeF(double feelslikeF) {
        this.feelslikeF = feelslikeF;
    }
    public double getVisKm() {
        return visKm;
    }
    public void setVisKm(double visKm) {
        this.visKm = visKm;
    }
    public double getVisMiles() {
        return visMiles;
    }
    public void setVisMiles(double visMiles) {
        this.visMiles = visMiles;
    }
    public double getUv() {
        return uv;
    }
    public void setUv(double uv) {
        this.uv = uv;
    }
    public double getGustMph() {
        return gustMph;
    }
    public void setGustMph(double gustMph) {
        this.gustMph = gustMph;
    }
    public double getGustKph() {
        return gustKph;
    }
    public void setGustKph(double gustKph) {
        this.gustKph = gustKph;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(lastUpdatedEpoch);
        dest.writeString(lastUpdated);
        dest.writeDouble(tempC);
        dest.writeDouble(tempF);
        dest.writeInt(isDay);
        dest.writeParcelable(condition, flags);
        dest.writeDouble(windMph);
        dest.writeDouble(windKph);
        dest.writeInt(windDegree);
        dest.writeString(windDir);
        dest.writeDouble(pressureMb);
        dest.writeDouble(pressureIn);
        dest.writeDouble(precipMm);
        dest.writeDouble(precipIn);
        dest.writeInt(humidity);
        dest.writeInt(cloud);
        dest.writeDouble(feelslikeC);
        dest.writeDouble(feelslikeF);
        dest.writeDouble(visKm);
        dest.writeDouble(visMiles);
        dest.writeDouble(uv);
        dest.writeDouble(gustMph);
        dest.writeDouble(gustKph);
    }
}
