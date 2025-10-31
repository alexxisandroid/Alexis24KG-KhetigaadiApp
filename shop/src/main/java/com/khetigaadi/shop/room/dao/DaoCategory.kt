package com.khetigaadi.shop.room.dao
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DaoCategory {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(product: Category): Long
    @Query("SELECT * FROM Category2 order by priority")
    fun fetchAllItems(): LiveData<List<Category>>
    @Query("SELECT * FROM Category2 order by priority limit 4")
    fun fetchHomeItems(): LiveData<List<Category>>
    @Delete
    fun deleteProduct(product: Category)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(categories: List<Category>)
}