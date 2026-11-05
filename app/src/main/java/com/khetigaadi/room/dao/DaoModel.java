package com.khetigaadi.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
//import com.khetigaadi.base.model.Model;
import com.khetigaadi.tractor.util.Model;
import java.util.List;
@Dao
public interface DaoModel {
    @Insert
    Long insertModel(Model model);
    @Query("SELECT * FROM Model")
    LiveData<List<Model>> fetchAllModel();
    @Query("SELECT * FROM Model WHERE id in (:ids)")
    LiveData<List<Model>> getModels(int ids);
    @Query("SELECT * FROM Model WHERE id in (:ids)")
    LiveData<List<Model>> getModelsByBrands(List<Integer> ids);
    @Update
    void updateModel(Model note);
    @Delete
    void deleteModel(Model brand);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Model> models);
}
