package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.khetigaadi.base.room.repository.utils.AppExecutors
import com.khetigaadi.base.room.repository.utils.RateLimiter
import com.khetigaadi.base.room.repository.utils.Resource
import com.khetigaadi.commons.NetworkBoundResource
import com.khetigaadi.commons.model.Hp
import com.khetigaadi.commons.utils.HpResponse
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.room.db.MyDatabase
import java.util.concurrent.TimeUnit
class HpRepository(context:Context?,private val khetiAPIs:KhetiAPIs,val appExecutors:AppExecutors) {
    private val myDatabase: MyDatabase = MyDatabase.getDatabase(context)
   // private val repoListRateLimit = RateLimiter<String>(12, TimeUnit.HOURS)
    private val repoListRateLimit = RateLimiter<String>(12,TimeUnit.HOURS)
    fun insertAll(hps: List<Hp?>?) {
        MyDatabase.databaseWriteExecutor.execute { myDatabase.daoHp().insertAll(hps) }
    }
    fun insertHP(hp: Hp?) {
        MyDatabase.databaseWriteExecutor.execute { myDatabase.daoHp().insertHP(hp) }
    }
    fun updateHP(hp: Hp?) {
        MyDatabase.databaseWriteExecutor.execute { myDatabase.daoHp().updateTask(hp) }
    }
    fun deleteHP(id: String?) {
        val task = getHP(id)
        MyDatabase.databaseWriteExecutor.execute { myDatabase.daoHp().deleteTask(task.value) }
    }
    fun deleteHP(note: Hp?) {
        MyDatabase.databaseWriteExecutor.execute { myDatabase.daoHp().deleteTask(note) }
    }
    private fun getHP(id: String?): LiveData<Hp> {
        return myDatabase.daoHp().getTask(id)
    }
    fun getHPs(): LiveData<List<Hp>>  { //LiveData<Resource<List<Hp>>>
//        return object : NetworkBoundResource<List<Hp>, HpResponse>(appExecutors) {
//            override fun saveCallResult(item: HpResponse) {
//                insertAll(item.hp)
//            }
//            override fun shouldFetch(data: List<Hp>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("hpRange")
//            }
//            override fun loadFromDb() = myDatabase.daoHp().fetchAllHP()
//            override fun createCall() = khetiAPIs.hPs
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("hpRange")
//            }
//        }.asLiveData()
//
    return myDatabase.daoHp().fetchAllHP()

    }
}