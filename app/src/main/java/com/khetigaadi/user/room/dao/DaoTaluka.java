package com.khetigaadi.user.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.khetigaadi.base.model.Taluka;

import java.util.List;
@Dao
public interface DaoTaluka {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Taluka> cities);
    @Query("SELECT * FROM Taluka WHERE cityId =:cityId order by name")
    LiveData<List<Taluka>> getTalukas(int cityId);
}
