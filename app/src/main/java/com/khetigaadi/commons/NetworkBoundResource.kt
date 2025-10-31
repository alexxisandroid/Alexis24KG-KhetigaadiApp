package com.khetigaadi.commons
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.khetigaadi.base.model.Model
import com.khetigaadi.base.room.repository.utils.ApiResponse
import com.khetigaadi.base.room.repository.utils.AppExecutors
import com.khetigaadi.base.room.repository.utils.Resource
import com.khetigaadi.commons.model.TypeResp
import com.khetigaadi.implement.newImplement.util.Type
import com.khetigaadi.shop.model.Category
abstract class NetworkBoundResource<T,R>(appExecuters:AppExecutors?) {
    // @Override
    abstract fun saveCallResult(vcbv:R)
    abstract fun shouldFetch(fdg:T?): Boolean
    // override fun loadFromDb()
    abstract fun loadFromDb(): LiveData<T>
    //createCall()
    abstract fun createCall(): LiveData<ApiResponse<R?>?>?
    //onFetchFailed()
    abstract fun onFetchFailed()
//    fun asLiveData() {
//        val liveData = MutableLiveData<Resource<T>>();
//        return; //liveData;
//    }
//    fun asLiveData(): LiveData<Resource<T>> {
//     return LiveData<Resource<T>>()
//    }  //
    // abstract fun asLiveData(): LiveData<Resource<T>>
}

