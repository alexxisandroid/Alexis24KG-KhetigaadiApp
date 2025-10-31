package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.khetigaadi.base.model.City
import com.khetigaadi.base.model.CityResp
import com.khetigaadi.base.room.repository.utils.AppExecutors

import com.khetigaadi.base.room.repository.utils.RateLimiter
import com.khetigaadi.base.room.repository.utils.Resource
import com.khetigaadi.commons.NetworkBoundResource
import com.khetigaadi.room.db.MyDatabase
import com.khetigaadi.user.retrofit.UserAPIs
import java.util.concurrent.TimeUnit
class CityRepository(context:Context?,private val userAPIs:UserAPIs,val appExecutors:AppExecutors) {
    private val myDatabase: MyDatabase = MyDatabase.getDatabase(context)
   // private val repoListRateLimit = RateLimiter<String>(12, TimeUnit.HOURS)
    private val repoListRateLimit = RateLimiter<String>(12,TimeUnit.HOURS)
    fun insertAll(cities:List<City?>?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCity().insertAll(cities)
        }
    }
    fun insertType(city:City?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCity().insertCity(city)
        }
    }
    fun updateType(city:City?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCity().updateCity(city)
        }
    }
    fun deleteCity(id:Int) {
        val city = getCity(id)
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCity().deleteCity(city.value)
        }
    }
    fun deleteCity(city:City?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCity().deleteCity(city)
        }
    }
    fun getCity(id:Int):LiveData<City> {
        return myDatabase.daoCity().getCity(id)
    }
    fun getCities(stateId:Int):LiveData<List<City>> { // LiveData<Resource<List<City>>>
//        return object : NetworkBoundResource<List<City>, CityResp>(appExecutors) {
//            override fun saveCallResult(item: CityResp) {
//                insertAll(item.city)
//            }
//            override fun shouldFetch(data: List<City>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("city")
//            }
//            override fun loadFromDb() = myDatabase.daoCity().getCities(stateId)
//            override fun createCall() = userAPIs.getCitiesByState(stateId)
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("city")
//            }
//        }.asLiveData()
//
        return myDatabase.daoCity().getCities(stateId);
    }
    fun getCities():LiveData<List<City>> {  // LiveData<Resource<List<City>>>
//        return object : NetworkBoundResource<List<City>, CityResp>(appExecutors) {
//            override fun saveCallResult(item: CityResp) {
//                insertAll(item.city)
//            }
//            override fun shouldFetch(data: List<City>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("city")
//            }
//            override fun loadFromDb() = myDatabase.daoCity().cites
//            override fun createCall() = userAPIs.cities
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("city")
//            }
//        }.asLiveData()
       return myDatabase.daoCity().cites
    }
}