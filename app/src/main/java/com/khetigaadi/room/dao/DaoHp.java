package com.khetigaadi.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.khetigaadi.commons.model.Hp;
import java.util.List;
@Dao
public interface DaoHp {
    @Insert
    Long insertHP(Hp hp);
    @Query("SELECT * FROM Hp")
    LiveData<List<Hp>> fetchAllHP();
    @Query("SELECT * FROM Hp WHERE hpId =:id")
    LiveData<Hp> getTask(String id);
    @Update
    void updateTask(Hp note);
    @Delete
    void deleteTask(Hp brand);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Hp> hps);
}
