package com.khetigaadi.room.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages;
@Dao
public interface DaoUsedTractor {
    @Insert
    Long insertTractor(TractorImages usedTractor);
    @Query("SELECT * FROM TractorImages WHERE usedTractorId =:id")
    TractorImages getUsedTractor(int id);
    @Delete
    void deleteTractorImages(TractorImages tractorImages);
}
