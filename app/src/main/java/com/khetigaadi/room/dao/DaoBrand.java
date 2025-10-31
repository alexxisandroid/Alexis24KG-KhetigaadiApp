package com.khetigaadi.room.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.khetigaadi.implement.newImplement.util.Brand;
import java.util.List;
@Dao
public interface DaoBrand {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertBrand(Brand brand);
    //@Query("Select * FROM Brand") manufacturerType brand_type  ORDER BY possition
    //  @Query("SELECT * FROM Brand WHERE brand_type IN ('0','1') AND status='1' ")
    @Query("SELECT * FROM Brand  WHERE brand_type IN (0,1) and brand_status = 1 ")
    LiveData<List<Brand>> fetchAllNewTractorBrand();
    @Query("SELECT * FROM Brand ")
    List<Brand> fetchAllNewTractorBrands();
    @Query("SELECT COUNT(*) FROM Brand")
    int getCount();

    @Query("SELECT COUNT(*) FROM Brand where brand_id =:id")
    int getBrandCount(int id);
    @Query("SELECT * FROM Brand WHERE brand_type IN (0,1) ")
    LiveData<List<Brand>> fetchAllOldTractorBrand();
    @Query("SELECT * FROM Brand WHERE brand_type IN (0,2) AND brand_status=1 ORDER BY implement_possition")
    LiveData<List<Brand>> fetchAllImplementBrand();
    @Query("SELECT * FROM Brand WHERE brand_id =:id")
    LiveData<Brand> getTask(int id);
    @Update
    void updateBrand(Brand brand);
    @Delete
    void deleteBrand(Brand brand);
    @Query("DELETE FROM Brand")
    void deleteAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Brand> brands);
    @Query("SELECT Brand.* FROM Brand INNER JOIN BrandTypeMaster ON Brand.brand_id = BrandTypeMaster.brandId WHERE BrandTypeMaster.typeId=:typeId")
    LiveData<List<Brand>> getBrandsByType(int typeId);
   //@Query("SELECT * FROM Brand WHERE manufacturerType IN ('0','2') AND status='1' ORDER BY brand_name")
    @Query("SELECT * FROM Brand WHERE brand_type IN (0,2) AND status=1 ORDER BY brand_name")
    LiveData<List<Brand>> fetchAllImplementBrandAsc();
    @Query("SELECT * FROM Brand WHERE isHarvesterBrand ='1'")
    LiveData<List<Brand>> fetchHarvesterBrands();
}
////import com.khetigaadi.base.model.Brand;
//import com.khetigaadi.tractor.util.Brand;
////import com.khetigaadi.implement.newImplement.util.Brand;
////import com.khetigaadi.commons.model.Brand;
////import com.khetigaadi.commons.model.Brand;
//import com.khetigaadi.commons.model.Hp;
//import java.util.List;
//@Dao
//public interface DaoBrand {
//    @Insert
//    Long insertBrand(Brand brand);
//    @Query("SELECT * FROM Hp")
//    LiveData<List<Hp>> fetchAllHP();
//    @Query("SELECT * FROM Brand WHERE manufacturerType IN ('0','1') AND status='1' ORDER BY possition")
//    LiveData<List<Brand>> fetchAllNewTractorBrand();
//    @Query("SELECT * FROM Brand WHERE manufacturerType IN ('0','1') ORDER BY possition")
//    LiveData<List<Brand>> fetchAllOldTractorBrand();
//    @Query("SELECT * FROM Brand WHERE manufacturerType IN ('0','2') AND status='1' ORDER BY implement_possition")
//    LiveData<List<Brand>> fetchAllImplementBrand();
//    @Query("SELECT * FROM Brand WHERE manufacturerType IN ('0','2') AND status='1' ORDER BY brand_name")
//    LiveData<List<Brand>> fetchAllImplementBrandAsc();
//    @Query("SELECT * FROM Brand WHERE brand_id =:id")
//    LiveData<Brand> getTask(int id);
//    @Query("SELECT * FROM Brand WHERE isHarvesterBrand =1")
//    LiveData<List<Brand>> fetchHarvesterBrands();
//    @Update
//    void updateBrand(Brand brand);
//    @Delete
//    void deleteBrand(Brand brand);
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<Brand> brands);
//    @Query("SELECT Brand.* FROM Brand INNER JOIN BrandTypeMaster ON Brand.brand_id = BrandTypeMaster.brandId WHERE BrandTypeMaster.typeId=:typeId")
//    LiveData<List<Brand>> getBrandsByType(int typeId);
//}



