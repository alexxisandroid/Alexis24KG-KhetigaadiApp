package com.khetigaadi.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.implement.newImplement.util.BrandTypeMaster;
import com.khetigaadi.implement.newImplement.util.Type;

import java.util.List;
@Dao
public interface DaoType {
    @Insert
    Long insertType(Type type);
    @Query("SELECT * FROM Type Order by implement_type_name")
    LiveData<List<Type>> getTypes();
    @Query("SELECT * FROM Type WHERE implement_type_id =:id")
    LiveData<List<Type>> getTypes(int id);
    @Query("SELECT * FROM Type WHERE implement_type_status =0")
    LiveData<List<Type>> getHarvesterTypes();
    @Update
    void updateType(Type type);
    @Query("SELECT COUNT(*) FROM Type")
    int getCount();
    @Delete
    void deleteType(Type type);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllBrandTypeMaster(List<BrandTypeMaster> types);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllType(List<Type> types);
    @Query("SELECT * FROM Type WHERE implement_type_id =:id")
    LiveData<Type> getType(int id);
    @Query("SELECT Type.* FROM Type JOIN BrandTypeMaster ON Type.implement_type_id=BrandTypeMaster.typeId WHERE BrandTypeMaster.brandId =:brandId")
    LiveData<List<Type>> getTypeByBrandId(int brandId);
    @Query("DELETE FROM BrandTypeMaster")
    void removeAllBrandTypeMaster();


    @Query("DELETE FROM Type")
    void deleteAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Type> brands);

}
