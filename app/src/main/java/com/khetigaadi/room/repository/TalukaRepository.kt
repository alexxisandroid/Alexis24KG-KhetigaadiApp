package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.khetigaadi.base.model.Taluka
import com.khetigaadi.base.model.TalukaResp
import com.khetigaadi.base.room.repository.utils.AppExecutors
//import com.khetigaadi.base.room.repository.utils.NetworkBoundResource
import com.khetigaadi.base.room.repository.utils.RateLimiter
import com.khetigaadi.base.room.repository.utils.Resource
import com.khetigaadi.commons.NetworkBoundResource
import com.khetigaadi.room.db.MyDatabase
import com.khetigaadi.user.retrofit.UserAPIs
import java.util.concurrent.TimeUnit
class TalukaRepository(
    context: Context?,
    private val userAPIs: UserAPIs,
    val appExecutors: AppExecutors) {
    private val myDatabase: MyDatabase = MyDatabase.getDatabase(context)
    //private val repoListRateLimit = RateLimiter<String>(12, TimeUnit.HOURS)
    private val repoListRateLimit = RateLimiter<String>(12,TimeUnit.HOURS)
    fun insertAll(cities: List<Taluka?>?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoTaluka().insertAll(cities)
        }
    }
    fun getTalukas(cityId: Int): LiveData<List<Taluka>> { // LiveData<Resource<List<Taluka>>>
//        return object : NetworkBoundResource<List<Taluka>, TalukaResp>(appExecutors) {
//            override fun saveCallResult(item: TalukaResp) {
//                insertAll(item.data)
//            }
//            override fun shouldFetch(data: List<Taluka>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("taluka")
//            }
//            override fun loadFromDb() = myDatabase.daoTaluka().getTalukas(cityId)
//            override fun createCall() = userAPIs.getTalukas(cityId)
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("taluka")
//            }
//        }.asLiveData()
//
       return myDatabase.daoTaluka().getTalukas(cityId)
    }
}