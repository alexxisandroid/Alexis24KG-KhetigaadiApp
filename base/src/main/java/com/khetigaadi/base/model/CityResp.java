package com.khetigaadi.base.model;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class CityResp extends MyResponse implements Parcelable {
    @SerializedName("data")
    @Expose
    private List<City> cities = null;
    public List<City> getCity() {
        return cities;
    }
    public void setCity(List<City> city) {
        this.cities = city;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeTypedList(this.cities);
    }
    public CityResp() {
    }
    protected CityResp(Parcel in)
    {

        this.cities = in.createTypedArrayList(City.CREATOR);
    }
    public static final Parcelable.Creator<CityResp> CREATOR = new Parcelable.Creator<CityResp>() {
        @Override
        public CityResp createFromParcel(Parcel source) {
            return new CityResp(source);
        }
        @Override
        public CityResp[] newArray(int size) {
            return new CityResp[size];
        }
    };
}
