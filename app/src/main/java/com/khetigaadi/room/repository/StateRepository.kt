package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.khetigaadi.base.model.State
import com.khetigaadi.base.model.StateResp
import com.khetigaadi.base.room.repository.utils.AppExecutors
//import com.khetigaadi.base.room.repository.utils.NetworkBoundResource
import com.khetigaadi.base.room.repository.utils.RateLimiter
import com.khetigaadi.base.room.repository.utils.Resource
import com.khetigaadi.commons.NetworkBoundResource
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.room.db.MyDatabase
import com.khetigaadi.user.retrofit.UserAPIs
import java.util.concurrent.TimeUnit
class StateRepository(context:Context?,private val userAPIs:UserAPIs,val sharedPreferenceUtils:SharedPreferenceUtils,val appExecutors:AppExecutors) {
    private val myDatabase: MyDatabase = MyDatabase.getDatabase(context)
   // private val repoListRateLimit = RateLimiter<String>(12,TimeUnit.HOURS)
    private val repoListRateLimit = RateLimiter<String>(12,TimeUnit.HOURS)
    fun insertAll(types: List<State?>?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoState().insertAll(types)
        }
    }
    fun getState(id:Int):LiveData<State> {
        return myDatabase.daoState().getState(id)
    }
    fun getStates(): LiveData<List<State>> { // LiveData<Resource<List<State>>>
//        return object : NetworkBoundResource<List<State>, StateResp>(appExecutors) {
//            override fun saveCallResult(item: StateResp) {
//                sharedPreferenceUtils.setValue(Constants.STATE_BASE_URL, item.others.stateImagePath)
//                insertAll(item.states)
//            }
//            override fun shouldFetch(data: List<State>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("state")
//            }
//            override fun loadFromDb() = myDatabase.daoState().states
//            override fun createCall() = userAPIs.states
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("state")
//            }
//        }.asLiveData()

      return myDatabase.daoState().states
    }
    fun getStatesByPosition(): LiveData<List<State>> { // LiveData<Resource<List<State>>>
//        return object : NetworkBoundResource<List<State>, StateResp>(appExecutors) {
//            override fun saveCallResult(item:StateResp) {
//                sharedPreferenceUtils.setValue(Constants.STATE_BASE_URL,item.others.stateImagePath)
//                insertAll(item.states)
//            }
//            override fun shouldFetch(data:List<State>?):Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("state")
//            }
//            override fun loadFromDb() = myDatabase.daoState().statesByPosition
//            override fun createCall() = userAPIs.states
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("state")
//            }
//        }.asLiveData()
//
      return myDatabase.daoState().statesByPosition;
    }
}