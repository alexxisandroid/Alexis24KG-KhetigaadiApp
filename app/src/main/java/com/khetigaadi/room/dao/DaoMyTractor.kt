package com.khetigaadi.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.khetigaadi.tractor.sellTractorNewUi.model.MyTractorImages
import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages
import com.khetigaadi.tractor.util.MyTractorImage
@Dao
interface DaoMyTractor {
    @Insert
    open fun insertTractor(usedTractor: MyTractorImages): Long?

    @Query("SELECT * FROM MyTractorImages WHERE myTractorId =:id")
    open fun getUsedTractor(id: Int): MyTractorImages?

    @Delete
    open fun deleteTractorImages(tractorImages: MyTractorImages)
}