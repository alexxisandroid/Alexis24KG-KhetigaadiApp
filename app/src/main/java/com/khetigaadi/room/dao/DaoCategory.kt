package com.khetigaadi.room.dao
import androidx.lifecycle.LiveData
import androidx.room.*
import com.khetigaadi.shop.model.Category
@Dao
public interface DaoCategory {
 //   @Insert(onConflict = OnConflictStrategy.REPLACE)
    //fun insertCartItem(product: Category): Long
    @Query("SELECT * FROM Category order by priority")
    fun fetchAllItems(): LiveData<List<Category>>
    @Query("SELECT * FROM Category order by priority limit 4")
    fun fetchHomeItems(): LiveData<List<Category>>
   // @Delete
   // fun deleteProduct(product: Category)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<Category>)
}