package com.khetigaadi.shop.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.State;
import com.khetigaadi.base.model.Taluka;
import com.khetigaadi.shop.def.AddressType;
public class Address implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("customer_user_id")
    @Expose
    private String customerUserId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("type")
    @Expose
    @AddressType
    public String type;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("pincode")
    @Expose
    private String pinCode;
    @SerializedName("is_default")
    @Expose
    private int isDefault;
    @SerializedName("state")
    @Expose
    private State state;
    @SerializedName("city")
    @Expose
    private City city;
    private Taluka taluka;
    public Address() {
    }
    protected Address(Parcel in) {
        id = in.readString();
        customerUserId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        mobile = in.readString();
        type = in.readString();
        address = in.readString();
        pinCode = in.readString();
        isDefault = in.readInt();
        state = in.readParcelable(State.class.getClassLoader());
        city = in.readParcelable(City.class.getClassLoader());
        taluka = in.readParcelable(Taluka.class.getClassLoader());
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(customerUserId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(mobile);
        dest.writeString(type);
        dest.writeString(address);
        dest.writeString(pinCode);
        dest.writeInt(isDefault);
        dest.writeParcelable(state, flags);
        dest.writeParcelable(city, flags);
        dest.writeParcelable(taluka, flags);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCustomerUserId() {
        return customerUserId;
    }
    public void setCustomerUserId(String customerUserId) {
        this.customerUserId = customerUserId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getType() {
        return type;
    }
    public void setType(@AddressType String type) {
        this.type = type;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPinCode() {
        return pinCode;
    }
    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
    public int getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public City getCity() {
        return city;
    }
    public void setCity(City city) {
        this.city = city;
    }
    public void setTaluka(Taluka taluka) {
        this.taluka = taluka;
    }
    public Taluka getTaluka() {
        return taluka;
    }
}
