package com.khetigaadi.user.myfavorites.util;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.khetigaadi.user.myfavorites.model.MyFavNewTractor;
import com.khetigaadi.user.myfavorites.model.MyFavRentTractors;
import com.khetigaadi.user.myfavorites.model.MyFavUsedTractor;

import java.util.List;
public class MyFavorites {
    @SerializedName("new_tractor")
    List<MyFavNewTractor> newTractors;
    @SerializedName("rent_tractor")
    List<MyFavRentTractors> rentTractors;
    @SerializedName("old_tractor")
    List<MyFavUsedTractor> usedTractors;
    public List<MyFavNewTractor> getNewTractors() {
        return newTractors;
    }
    public void setNewTractors(List<MyFavNewTractor> newTractors) {
        this.newTractors = newTractors;
    }
    public List<MyFavRentTractors> getRentTractors() {
        return rentTractors;
    }
    public void setRentTractors(List<MyFavRentTractors> rentTractors) {
        this.rentTractors = rentTractors;
    }
    public List<MyFavUsedTractor> getUsedTractors() {
        return usedTractors;
    }
    public void setUsedTractors(List<MyFavUsedTractor> usedTractors) {
        this.usedTractors = usedTractors;
    }
    @NonNull
    @Override
    public String toString() {
        return "MyFavorites{" +
                "newTractors=" + newTractors +
                ", rentTractors=" + rentTractors +
                ", usedTractors=" + usedTractors +
                '}';
    }
}
