package com.khetigaadi.user.myfavorites.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.khetigaadi.tractor.rent.model.RentTractor;
public class MyFavRentTractors {
    @SerializedName("rent_tractor")
    @Expose
    private RentTractor rentTractor;
    public RentTractor getRentTractor() {
        return rentTractor;
    }
    public void setRentTractor(RentTractor rentTractor) {
        this.rentTractor = rentTractor;
    }
}
