package com.khetigaadi.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.khetigaadi.commons.model.Recent;

import java.util.List;
@Dao
public interface DaoRecent {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(Recent recent);
    @Query("Select * from Recent Order by time desc")
    LiveData<List<Recent>> getAllRecent();
}
