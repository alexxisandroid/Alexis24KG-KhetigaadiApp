package com.khetigaadi.room.repository

//import android.content.Context
//import com.khetigaadi.room.db.MyDatabase
//import com.khetigaadi.tractor.sellTractorNewUi.model.MyTractorImages
//import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages
////import com.khetigaadi.tractor.util.MyTractorImage
//import org.jetbrains.annotations.NotNull
//
//class MyAddedTractorRepository (context:@NotNull Context) {
//    private val database: MyDatabase = MyDatabase.getDatabase(context)
//    fun insertTractor(usedTractor: MyTractorImages) {
//        MyDatabase.databaseWriteExecutor.execute {
//            database.daoMyTractor().insertTractor(usedTractor)
//        }
//    }
//    fun getUsedTractor(id:Int):TractorImages? {
//        return database.daoUsedTractor().getUsedTractor(id)
//    }
//    fun deleteRecord(usedTractorId:Int) {
//        val task = getUsedTractor(usedTractorId)
//        MyDatabase.databaseWriteExecutor.execute {
//            database.daoUsedTractor().deleteTractorImages(task)
//        }
//    }
//}