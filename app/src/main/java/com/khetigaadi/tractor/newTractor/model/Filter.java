package com.khetigaadi.tractor.newTractor.model;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.model.City;
//import com.khetigaadi.base.model.Model;

import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.base.model.Price;
import com.khetigaadi.base.model.State;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.implement.newImplement.util.Type;
import java.util.List;
public class Filter implements Parcelable {
    @SerializedName("brands")
    @Expose
    private List<Brand> brands = null;
    @SerializedName("type")
    @Expose
    private List<Type> types = null;
    @SerializedName("drives")
    @Expose
    private List<Drive> drives = null;
    @SerializedName("hps")
    @Expose
    private List<Hp> hps = null;
    @SerializedName("tractor_models")
    @Expose
    private List<Model> models = null;
    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("states")
    @Expose
    private List<State> states = null;
    @SerializedName("city")
    @Expose
    private List<City> city = null;
    public List<Brand> getBrands() {
        return brands;
    }
    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
    public List<Drive> getDrives() {
        return drives;
    }
    public void setDrives(List<Drive> drives) {
        this.drives = drives;
    }
    public List<Hp> getHps() {
        return hps;
    }
    public void setHps(List<Hp> hps) {
        this.hps = hps;
    }
    public List<Model> getModels() {
        return models;
    }
    public void setModels(List<Model> models) {
        this.models = models;
    }
    public List<Type> getTypes() {
        return types;
    }
    public void setTypes(List<Type> types) {
        this.types = types;
    }
    public Price getPrice() {
        return price;
    }
    public void setPrice(Price price) {
        this.price = price;
    }
    public List<State> getStates() {
        return states;
    }
    public void setStates(List<State> states) {
        this.states = states;
    }
    public List<City> getCity() {
        return city;
    }
    public void setCity(List<City> city) {
        this.city = city;
    }
    public void clear() {
        if (brands != null)
            brands.clear();
        if (types != null)
            types.clear();
        if (drives != null)
            drives.clear();
        if (hps != null)
            hps.clear();
        if (price != null)
            price = null;
        if (states != null)
            states.clear();
        if (city != null)
            city.clear();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest,int flags) {
        dest.writeTypedList(this.brands);
        dest.writeTypedList(this.types);
        dest.writeTypedList(this.drives);
        dest.writeTypedList(this.hps);
        dest.writeTypedList(this.models);
        dest.writeParcelable(this.price, flags);
        dest.writeTypedList(this.states);
        dest.writeTypedList(this.city);
    }
    public Filter() {
    }
    protected Filter(Parcel in) {
        this.brands = in.createTypedArrayList(Brand.CREATOR);
        this.types = in.createTypedArrayList(Type.CREATOR);
        this.drives = in.createTypedArrayList(Drive.CREATOR);
        this.hps = in.createTypedArrayList(Hp.CREATOR);
        this.models = in.createTypedArrayList(Model.CREATOR);
        this.price = in.readParcelable(Price.class.getClassLoader());
        this.states = in.createTypedArrayList(State.CREATOR);
        this.city = in.createTypedArrayList(City.CREATOR);
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

    @Override
    public String toString() {
        return "Filter{" +
                "brands=" + brands +
                ", types=" + types +
                ", drives=" + drives +
                ", hps=" + hps +
                ", models=" + models +
                ", price=" + price +
                ", states=" + states +
                ", city=" + city +
                '}';
    }
}
