package com.khetigaadi.room.repository
import android.content.Context
import com.khetigaadi.room.db.MyDatabase
import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages
import org.jetbrains.annotations.NotNull
class UsedTractorRepository(context:@NotNull Context) {
    private val database: MyDatabase = MyDatabase.getDatabase(context)
    fun insertTractor(usedTractor: TractorImages) {
        MyDatabase.databaseWriteExecutor.execute {
            database.daoUsedTractor().insertTractor(usedTractor)
        }
    }
    fun getUsedTractor(id:Int):TractorImages? {
        return database.daoUsedTractor().getUsedTractor(id)
    }
    fun deleteRecord(UsedTractorId:Int) {
        val task = getUsedTractor(UsedTractorId)
        MyDatabase.databaseWriteExecutor.execute {
            database.daoUsedTractor().deleteTractorImages(task)
        }
    }
}