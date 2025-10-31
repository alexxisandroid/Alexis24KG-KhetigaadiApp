package com.khetigaadi.room.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImplement
import kotlinx.coroutines.flow.Flow
@Dao
interface DaoRentTractor {
    @Insert
    fun insertImplement(rentImplement: RentImplement)
    @Query("SELECT * FROM RentImplement ORDER BY id")
    fun getImplements(): Flow<MutableList<RentImplement>>
    @Delete
    fun deleteImp(item: RentImplement)
    @Query("DELETE FROM RentImplement")
    fun deleteAllRentImp()
    @Query("Update RentImplement set rentId=:rentTrId where rentId=0")
    fun updateIdToImplement(rentTrId: Int)
}