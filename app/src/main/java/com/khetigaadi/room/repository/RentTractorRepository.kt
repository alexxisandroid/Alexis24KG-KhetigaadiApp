package com.khetigaadi.room.repository
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.khetigaadi.dagger.qualifier.ApplicationContext
import com.khetigaadi.implement.newImplement.util.Type
import com.khetigaadi.room.db.MyDatabase
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImplement
import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentUnit
import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

//@UninstallModules(AppModule::class)
//@HiltAndroidTest
//@TestInstallIn(components = SingletonComponent.class, replaces = AppModule.class)
//@UninstallModules(AppModule.class)
//@DisableInstallInCheck
//@HiltAndroidTest
//@HiltAndroidApp
//@Module
class RentTractorRepository(context:@NotNull Context) {
//    constructor() : this() {
//
//    }
   // @Inject
    constructor(context:Context,item:RentImplement) : this(context) {
        // this.database = database
    }
    var implementsAsLive: LiveData<MutableList<RentImplement>>
    private var database: MyDatabase = MyDatabase.getDatabase(context)
    init {
        implementsAsLive = database.daoRentTractor().getImplements().asLiveData()
    }
    fun insertImplement(type:Type,rent:String,unit:RentUnit,image:Uri) {
        val rentImplement = RentImplement()
        rentImplement.type = type
        rentImplement.rentAmt = rent
        rentImplement.unit = unit
        rentImplement.image = image.path
        database.daoRentTractor().insertImplement(rentImplement)
    }
    fun deleteImplement(item:RentImplement) {
        database.daoRentTractor().deleteImp(item)
    }
    fun deleteAllRentImplement() {
        database.daoRentTractor().deleteAllRentImp()
    }
    fun updateRentTractorIdToImplement(rentTrId:Int) {
        database.daoRentTractor().updateIdToImplement(rentTrId)
    }
   // @Provides
    fun providesAppD(@ApplicationContext context:Context):Context {
     return context
    }
}



//
//import android.content.Context
//import android.net.Uri
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.asLiveData
//import com.khetigaadi.implement.newImplement.util.Type
//import com.khetigaadi.room.db.MyDatabase
//import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentImplement
//import com.khetigaadi.tractor.rent.rentTractorNewUi.model.RentUnit
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import org.jetbrains.annotations.NotNull
//import javax.inject.Inject
//import javax.inject.Singleton
//@AndroidEntryPoint
//@Module
//@InstallIn(SingletonComponent::class)
//class RentTractorRepository // database = MyDatabase.getDatabase(context) {
// constructor(context: @NotNull Context) { //:Parcelable
//    var implementsAsLive: LiveData<MutableList<RentImplement>>
//    private var database: MyDatabase
// constructor(database: MyDatabase) : this(database,context) {
//
// }
//
//    init {
//        database = MyDatabase.getDatabase(context)
//        implementsAsLive = database.daoRentTractor().getImplements().asLiveData()
//    }
////    constructor(parcel: Parcel) : this(parcel) {
////
////    }
////   constructor(context:Context,process:Process)
////   {
////
////   }
//
////    constructor(parcel: Parcel) : this(TODO("context")) {
////
////    }
////    init {
////        database = MyDatabase.getDatabase(context)
////        implementsAsLive = database.daoRentTractor().getImplements().asLiveData()
////    }
//   // @Provides
//    @Singleton
//    fun insertImplement(type:Type,rent:String,unit:RentUnit,image:Uri) {
//        val rentImplement = RentImplement()
//        rentImplement.type = type
//        rentImplement.rentAmt = rent
//        rentImplement.unit = unit
//        rentImplement.image = image.path
//        database.daoRentTractor().insertImplement(rentImplement)
//    }
//   // @Provides
//    @Singleton
//    fun deleteImplement(item:RentImplement) {
//        database.daoRentTractor().deleteImp(item)
//    }
//   // @Provides
//    @Singleton
//    fun deleteAllRentImplement() {
//        database.daoRentTractor().deleteAllRentImp()
//    }
//   // @Provides
//    @Singleton
//    fun updateRentTractorIdToImplement(rentTrId:Int) {
//        database.daoRentTractor().updateIdToImplement(rentTrId)
//    }
////    override fun writeToParcel(parcel: Parcel, flags: Int) {
////
////    }
////    override fun describeContents(): Int {
////        return 0
////    }
////    companion object CREATOR : Parcelable.Creator<RentTractorRepository> {
////        override fun createFromParcel(parcel: Parcel): RentTractorRepository {
////            return RentTractorRepository(parcel)
////        }
////
////        override fun newArray(size: Int): Array<RentTractorRepository?> {
////            return arrayOfNulls(size)
////        }
////    }
////       companion object CREATOR : Parcelable.Creator<RentTractorRepository> {
////        override fun createFromParcel(parcel: Parcel): RentTractorRepository {
////            return RentTractorRepository(parcel)
////        }
////        override fun newArray(size: Int): Array<RentTractorRepository?> {
////            return arrayOfNulls(size)
////        }
////    }
//}