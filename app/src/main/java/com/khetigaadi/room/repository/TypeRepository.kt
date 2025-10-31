package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.khetigaadi.base.room.repository.utils.AppExecutors
import com.khetigaadi.base.room.repository.utils.RateLimiter
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.model.TypeResp
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.implement.newImplement.util.Brand
import com.khetigaadi.implement.newImplement.util.BrandTypeMaster
import com.khetigaadi.implement.newImplement.util.Type
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.room.db.MyDatabase
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.util.concurrent.TimeUnit
class TypeRepository(context:@Nullable Context?,private val khetiAPIs:@NotNull KhetiAPIs,val appExecutors:@NotNull AppExecutors,val sharedPreferenceUtils:SharedPreferenceUtils) {
    private val myDatabase: MyDatabase = MyDatabase.getDatabase(context)
    private val repoListRateLimit = RateLimiter<String>(12,TimeUnit.HOURS)
    private fun insertAllBrandTypeMaster(brandTypeMasters:List<BrandTypeMaster?>?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoType().removeAllBrandTypeMaster()
            myDatabase.daoType().insertAllBrandTypeMaster(brandTypeMasters)
        }
    }
    private fun insertAllType(types:List<Type?>?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoType().insertAllType(types)
        }
    }
    fun insertAll(types:List<Type>) {
        try {
            MyDatabase.databaseWriteExecutor.execute { myDatabase.daoType().insertAll(types) }
        }
        catch(e:Exception) {
            println("cvbcvbcvbcvbc==="+""+e.localizedMessage);
            e.printStackTrace()
        }
    }
    fun deleteAll() {
        return myDatabase.daoType().deleteAll();
    }
    fun getCountt():Int {
        return myDatabase.daoType().count
    }
    fun insertType(type:Type?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoType().insertType(type)
        }
    }
    fun updateType(type:Type?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoType().updateType(type)
        }
    }
    fun deleteType(id:Int) {
        val type = getType(id)
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoType().deleteType(type.value)
        }
    }
    fun deleteType(type:Type?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoType().deleteType(type)
        }
    }
    fun getType(id:Int):LiveData<Type> {
        return myDatabase.daoType().getType(id)
    }
    fun getTypeByBrandId(brandId:Int):LiveData<List<Type>> { // LiveData<Resource<List<Type>>>
//        return myDatabase.daoType().getTypeByBrandId(brandId)
//        return object : NetworkBoundResource<List<Type>,TypeResp>(appExecutors) {
//            override fun saveCallResult(item:TypeResp) {
//                saveApiResponse(item)
//            }
//            override fun shouldFetch(data: List<Type>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("type")
//            }
//            override fun loadFromDb() = myDatabase.daoType().getTypeByBrandId(brandId)
//            override fun createCall() = khetiAPIs.types
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("type")
//            }
//        }.asLiveData()
//

    return myDatabase.daoType().types
    }
    fun getTypes():LiveData<List<Type>>  { //LiveData<Resource<List<Type>>>
//        return object : NetworkBoundResource<List<Type>,TypeResp>(appExecutors) {
//            override fun saveCallResult(item:TypeResp) {
//                saveApiResponse(item)
//            }
//            override fun shouldFetch(data: List<Type>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("type")
//            }
//            override fun loadFromDb() = myDatabase.daoType().types
//            override fun createCall() = khetiAPIs.types
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("type")
//            }
//
////            override fun asLiveData(): LiveData<Resource<List<Type>>> {
////               ///////// TODO("Not yet implemented")
////            }
//        }.asLiveData()

        return myDatabase.daoType().types
    }
    private fun saveApiResponse(item:TypeResp) {
        insertAllType(item.types)
        if(item.brandWiseType != null) {
            insertAllBrandTypeMaster(item.brandWiseType.brandTypeMasters)
            sharedPreferenceUtils.setValue(Constants.TYPES_BASE_URL,item.brandWiseType.typePath)
        }
    }
    fun getHarvesterTypes():LiveData<List<Type>>  { //LiveData<Resource<List<Type>>>
//        return object:NetworkBoundResource<List<Type>,TypeResp>(appExecutors) {
//            override fun saveCallResult(item:TypeResp) {
//                saveApiResponse(item)
//            }
//            override fun shouldFetch(data: List<Type>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("type")
//            }
//            override fun loadFromDb() = myDatabase.daoType().harvesterTypes  //: LiveData<MutableList<com.khetigaadi.base.model.imp.Type>>?
//            override fun createCall() = khetiAPIs.types
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("type")
//            }
//
////            override fun asLiveData(): LiveData<Resource<List<Type>>> {
////                TODO("Not yet implemented")
////            }
//        }.asLiveData()
//
      return myDatabase.daoType().harvesterTypes
    }
}