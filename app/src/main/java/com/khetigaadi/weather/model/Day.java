package com.khetigaadi.weather.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Day implements Parcelable {
    @SerializedName("maxtemp_c")
    @Expose
    private double maxtempC;
    @SerializedName("maxtemp_f")
    @Expose
    private double maxtempF;
    @SerializedName("mintemp_c")
    @Expose
    private double mintempC;
    @SerializedName("mintemp_f")
    @Expose
    private double mintempF;
    @SerializedName("avgtemp_c")
    @Expose
    private double avgtempC;
    @SerializedName("avgtemp_f")
    @Expose
    private double avgtempF;
    @SerializedName("maxwind_mph")
    @Expose
    private double maxwindMph;
    @SerializedName("maxwind_kph")
    @Expose
    private double maxwindKph;
    @SerializedName("totalprecip_mm")
    @Expose
    private double totalprecipMm;
    @SerializedName("totalprecip_in")
    @Expose
    private double totalprecipIn;
    @SerializedName("avgvis_km")
    @Expose
    private double avgvisKm;
    @SerializedName("avgvis_miles")
    @Expose
    private double avgvisMiles;
    @SerializedName("avghumidity")
    @Expose
    private double avghumidity;
    @SerializedName("daily_will_it_rain")
    @Expose
    private int dailyWillItRain;
    @SerializedName("daily_chance_of_rain")
    @Expose
    private String dailyChanceOfRain;
    @SerializedName("daily_will_it_snow")
    @Expose
    private int dailyWillItSnow;
    @SerializedName("daily_chance_of_snow")
    @Expose
    private String dailyChanceOfSnow;
    @SerializedName("condition")
    @Expose
    private ConditionImage condition;
    @SerializedName("uv")
    @Expose
    private double uv;

    protected Day(Parcel in) {
        maxtempC = in.readDouble();
        maxtempF = in.readDouble();
        mintempC = in.readDouble();
        mintempF = in.readDouble();
        avgtempC = in.readDouble();
        avgtempF = in.readDouble();
        maxwindMph = in.readDouble();
        maxwindKph = in.readDouble();
        totalprecipMm = in.readDouble();
        totalprecipIn = in.readDouble();
        avgvisKm = in.readDouble();
        avgvisMiles = in.readDouble();
        avghumidity = in.readDouble();
        dailyWillItRain = in.readInt();
        dailyChanceOfRain = in.readString();
        dailyWillItSnow = in.readInt();
        dailyChanceOfSnow = in.readString();
        condition = in.readParcelable(ConditionImage.class.getClassLoader());
        uv = in.readDouble();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public double getMaxtempC() {
        return maxtempC;
    }

    public void setMaxtempC(double maxtempC) {
        this.maxtempC = maxtempC;
    }

    public double getMaxtempF() {
        return maxtempF;
    }

    public void setMaxtempF(double maxtempF) {
        this.maxtempF = maxtempF;
    }

    public double getMintempC() {
        return mintempC;
    }

    public void setMintempC(double mintempC) {
        this.mintempC = mintempC;
    }

    public double getMintempF() {
        return mintempF;
    }

    public void setMintempF(double mintempF) {
        this.mintempF = mintempF;
    }

    public double getAvgtempC() {
        return avgtempC;
    }

    public void setAvgtempC(double avgtempC) {
        this.avgtempC = avgtempC;
    }

    public double getAvgtempF() {
        return avgtempF;
    }

    public void setAvgtempF(double avgtempF) {
        this.avgtempF = avgtempF;
    }

    public double getMaxwindMph() {
        return maxwindMph;
    }

    public void setMaxwindMph(double maxwindMph) {
        this.maxwindMph = maxwindMph;
    }

    public double getMaxwindKph() {
        return maxwindKph;
    }

    public void setMaxwindKph(double maxwindKph) {
        this.maxwindKph = maxwindKph;
    }

    public double getTotalprecipMm() {
        return totalprecipMm;
    }

    public void setTotalprecipMm(double totalprecipMm) {
        this.totalprecipMm = totalprecipMm;
    }

    public double getTotalprecipIn() {
        return totalprecipIn;
    }

    public void setTotalprecipIn(double totalprecipIn) {
        this.totalprecipIn = totalprecipIn;
    }

    public double getAvgvisKm() {
        return avgvisKm;
    }

    public void setAvgvisKm(double avgvisKm) {
        this.avgvisKm = avgvisKm;
    }

    public double getAvgvisMiles() {
        return avgvisMiles;
    }

    public void setAvgvisMiles(double avgvisMiles) {
        this.avgvisMiles = avgvisMiles;
    }

    public double getAvghumidity() {
        return avghumidity;
    }

    public void setAvghumidity(double avghumidity) {
        this.avghumidity = avghumidity;
    }

    public int getDailyWillItRain() {
        return dailyWillItRain;
    }

    public void setDailyWillItRain(int dailyWillItRain) {
        this.dailyWillItRain = dailyWillItRain;
    }

    public String getDailyChanceOfRain() {
        return dailyChanceOfRain;
    }

    public void setDailyChanceOfRain(String dailyChanceOfRain) {
        this.dailyChanceOfRain = dailyChanceOfRain;
    }

    public int getDailyWillItSnow() {
        return dailyWillItSnow;
    }

    public void setDailyWillItSnow(int dailyWillItSnow) {
        this.dailyWillItSnow = dailyWillItSnow;
    }

    public String getDailyChanceOfSnow() {
        return dailyChanceOfSnow;
    }

    public void setDailyChanceOfSnow(String dailyChanceOfSnow) {
        this.dailyChanceOfSnow = dailyChanceOfSnow;
    }

    public ConditionImage getCondition() {
        return condition;
    }

    public void setCondition(ConditionImage condition) {
        this.condition = condition;
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(maxtempC);
        dest.writeDouble(maxtempF);
        dest.writeDouble(mintempC);
        dest.writeDouble(mintempF);
        dest.writeDouble(avgtempC);
        dest.writeDouble(avgtempF);
        dest.writeDouble(maxwindMph);
        dest.writeDouble(maxwindKph);
        dest.writeDouble(totalprecipMm);
        dest.writeDouble(totalprecipIn);
        dest.writeDouble(avgvisKm);
        dest.writeDouble(avgvisMiles);
        dest.writeDouble(avghumidity);
        dest.writeInt(dailyWillItRain);
        dest.writeString(dailyChanceOfRain);
        dest.writeInt(dailyWillItSnow);
        dest.writeString(dailyChanceOfSnow);
        dest.writeParcelable(condition, flags);
        dest.writeDouble(uv);
    }

    @Override
    public String toString() {
        return "Day{" +
                "maxtempC=" + maxtempC +
                ", maxtempF=" + maxtempF +
                ", mintempC=" + mintempC +
                ", mintempF=" + mintempF +
                ", avgtempC=" + avgtempC +
                ", avgtempF=" + avgtempF +
                ", maxwindMph=" + maxwindMph +
                ", maxwindKph=" + maxwindKph +
                ", totalprecipMm=" + totalprecipMm +
                ", totalprecipIn=" + totalprecipIn +
                ", avgvisKm=" + avgvisKm +
                ", avgvisMiles=" + avgvisMiles +
                ", avghumidity=" + avghumidity +
                ", dailyWillItRain=" + dailyWillItRain +
                ", dailyChanceOfRain='" + dailyChanceOfRain + '\'' +
                ", dailyWillItSnow=" + dailyWillItSnow +
                ", dailyChanceOfSnow='" + dailyChanceOfSnow + '\'' +
                ", condition=" + condition +
                ", uv=" + uv +
                '}';
    }
}
