package com.khetigaadi.user.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.khetigaadi.base.model.City;

import java.util.List;
@Dao
public interface DaoCity {
    @Insert
    Long insertCity(City state);
    @Query("SELECT * FROM City Order by name")
    LiveData<List<City>> getCites();
    @Update
    void updateCity(City city);
    @Delete
    void deleteCity(City city);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<City> cities);
    @Query("SELECT * FROM City WHERE id =:id")
    LiveData<City> getCity(int id);
    @Query("SELECT * FROM City WHERE stateId =:stateId order by name")
    LiveData<List<City>> getCities(int stateId);
}


//
//
//import androidx.lifecycle.LiveData;
//
//import com.khetigaadi.base.model.City;
//
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//public class DaoCity {
//    @NotNull
//    public  LiveData<List<City>> cities;
//    public void insertAll(@Nullable List<? extends City> cities) {
//    }
//    public void insertCity(@Nullable City city) {
//    }
//    public void updateCity(@Nullable City city) {
//    }
//    public void deleteCity(@Nullable City value) {
//    }
//    @NotNull
//    public LiveData<City> getCity(int id) {
//        return null;
//    }
//    @NotNull
//    public LiveData<List<City>> getCities(int stateId) {
//        return null;
//    }
//}
