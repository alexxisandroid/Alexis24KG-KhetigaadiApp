package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.khetigaadi.base.room.repository.utils.AppExecutors
import com.khetigaadi.base.room.repository.utils.RateLimiter
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.implement.newImplement.util.Brand
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.room.db.MyDatabase
import org.jetbrains.annotations.NotNull
import java.util.concurrent.TimeUnit
class BrandRepository(context:@NotNull Context,private val khetiAPIs:KhetiAPIs,val sharedPreferenceUtils:SharedPreferenceUtils,val appExecutors:AppExecutors) {
     val database:MyDatabase = MyDatabase.getDatabase(context)
    // private val repoListRateLimit = RateLimiter<String>(1,TimeUnit.HOURS)
    val repoListRateLimit = RateLimiter<String>(1,TimeUnit.HOURS)
    fun insertAll(brands:List<Brand>) {
      try {
          MyDatabase.databaseWriteExecutor.execute { database.daoBrand().insertAll(brands) }
      }
      catch(e:Exception) {
          println("cvbcvbcvbcvbc==="+""+e.localizedMessage);
          e.printStackTrace()
      }
    }
    fun insertBrand(brand:Brand?) {
        var retVall:Long = 2
       try {
           MyDatabase.databaseWriteExecutor.execute {
               retVall = database.daoBrand().insertBrand(brand)
               println("cvbcvbcvbcvbc2333423==="+""+retVall);
           }



       }
       catch(e:Exception) {
           println("cvbcvbcvbcvbc23334==="+""+e.localizedMessage);
       }
    }
    fun updateBrand(brand:Brand?) {
        MyDatabase.databaseWriteExecutor.execute { database.daoBrand().updateBrand(brand) }
    }
    fun deleteBrand(id:Int) {
        val task = getBrand(id)
        MyDatabase.databaseWriteExecutor.execute { database.daoBrand().deleteBrand(task.value) }
    }
    fun deleteBrand(note:Brand?) {
        MyDatabase.databaseWriteExecutor.execute { database.daoBrand().deleteBrand(note) }
    }
    fun getBrand(id: Int): LiveData<Brand> {
        return database.daoBrand().getTask(id)
    }
    fun getImplementBrandsAsc(): LiveData<MutableList<Brand>>? { //LiveData<Resource<List<Brand>>>
//        return object : NetworkBoundResource<List<Brand>,BrandResp>(appExecutors) {
//            override fun saveCallResult(item:BrandResp) {
//                sharedPreferenceUtils.setValue(Constants.BRAND_BASE_URL,item.brandBaseUrl.brandBaseUrl)
//                insertAll(item.brand)
//            }
//            override fun shouldFetch(data: List<Brand>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("brands")
//            }
//            override fun loadFromDb() = database.daoBrand().fetchAllImplementBrandAsc()
//            override fun createCall() = khetiAPIs.brands
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("brands")
//            }
//        }.asLiveData()

       return database.daoBrand().fetchAllImplementBrandAsc()
    }
    fun getImplementBrandsByType(typeId: Int):LiveData<List<Brand>> {
        return database.daoBrand().getBrandsByType(typeId)
    }

//    fun getNewTractorBrands(): LiveData<Resource<List<Brand>>>  { // LiveData<Resource<List<Brand>>>
//        return object : NetworkBoundResource<List<Brand>, BrandResp>(appExecutors) {
//            override fun saveCallResult(item: BrandResp) {
//                sharedPreferenceUtils.setValue(
//                    Constants.BRAND_BASE_URL,
//                    item.brandBaseUrl.brandBaseUrl
//                )
//                insertAll(item.brand)
//            }
//            override fun shouldFetch(data: List<Brand>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("brands")
//            }
//            override fun loadFromDb() = database.daoBrand().fetchAllNewTractorBrand()
//            override fun createCall() = khetiAPIs.brands
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("brands")
//            }
//        }.asLiveData()
//    }


    fun getNewTractorBrands(): LiveData<List<Brand>> {
        return database.daoBrand().fetchAllNewTractorBrand()
    }
    fun getCountt():Int {
        return database.daoBrand().count
    }
    fun deleteAll() {
        return database.daoBrand().deleteAll();
    }
    fun fetchAllBrandss():List<Brand> {
        return database.daoBrand().fetchAllNewTractorBrands();
    }

    fun getBrandCounts(id:Int):Int {
        return database.daoBrand().getBrandCount(id);
    }
    fun getOldTractorBrands(): LiveData<List<Brand>>  { // LiveData<Resource<List<Brand>>>
//        return object : NetworkBoundResource<List<Brand>, BrandResp>(appExecutors) {
//            override fun saveCallResult(item: BrandResp) {
//                sharedPreferenceUtils.setValue(
//                    Constants.BRAND_BASE_URL,
//                    item.brandBaseUrl.brandBaseUrl
//                )
//                insertAll(item.brand)
//            }
//            override fun shouldFetch(data: List<Brand>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("brands")
//            }
//            override fun loadFromDb() = database.daoBrand().fetchAllOldTractorBrand()
//            override fun createCall() = khetiAPIs.brands
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("brands")
//            }
//        }.asLiveData()
//
     return database.daoBrand().fetchAllOldTractorBrand();
    }
    fun getImplementBrands():  LiveData<List<Brand>>  { //LiveData<Resource<List<Brand>>>
//        return object : NetworkBoundResource<List<Brand>, BrandResp>(appExecutors) {
//            override fun saveCallResult(item: BrandResp) {
//                sharedPreferenceUtils.setValue(Constants.BRAND_BASE_URL,item.brandBaseUrl.brandBaseUrl)
//                insertAll(item.brand)
//            }
//            override fun shouldFetch(data: List<Brand>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("brands")
//            }
//            override fun loadFromDb() = database.daoBrand().fetchAllImplementBrand()
//            override fun createCall() = khetiAPIs.brands
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("brands")
//            }
//        }.asLiveData()
//
      return database.daoBrand().fetchAllImplementBrand();
    }
    fun getHarvesterBrands():  LiveData<List<Brand>>  { //LiveData<Resource<List<Brand>>>
//        return object : NetworkBoundResource<List<Brand>, BrandResp>(appExecutors) {
//            override fun saveCallResult(item: BrandResp) { sharedPreferenceUtils.setValue(Constants.BRAND_BASE_URL,item.brandBaseUrl.brandBaseUrl)
//                insertAll(item.brand)
//            }
//            override fun shouldFetch(data: List<Brand>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("brands")
//            }
//            override fun loadFromDb() = database.daoBrand().fetchHarvesterBrands()
//            override fun createCall() = khetiAPIs.brands
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("brands")
//            }
//        }.asLiveData()
//
       return database.daoBrand().fetchHarvesterBrands();
    }
}