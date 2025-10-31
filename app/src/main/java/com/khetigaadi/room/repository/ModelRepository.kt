package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import com.khetigaadi.base.model.Model
import com.khetigaadi.base.room.repository.utils.*
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.model.ModelResp
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.commons.NetworkBoundResource
import com.khetigaadi.room.db.MyDatabase
import com.khetigaadi.tractor.util.Model
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import org.jetbrains.annotations.NotNull
import java.util.concurrent.TimeUnit
class ModelRepository(
    context:@NotNull Context,
    private val khetiAPIs:KhetiAPIs,
    val sharedPreferenceUtils:SharedPreferenceUtils,
    val appExecutors:AppExecutors) {
    private val myDatabase:MyDatabase = MyDatabase.getDatabase(context)
    // private val repoListRateLimit = RateLimiter<String>(1, TimeUnit.HOURS)
    private val repoListRateLimit = RateLimiter<String>(1,TimeUnit.HOURS)
    private fun insertAll(models:List<Model>) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoModel().insertAll(models)
        }
    }
    fun insertModel(model:Model?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoModel().insertModel(model)
        }
    }
    fun updateModel(hp:Model?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoModel().updateModel(hp)
        }
    }
    fun deleteModel(id:Int) {
        val task = getModel(id)
        if (task != null) {
            MyDatabase.databaseWriteExecutor.execute {
                myDatabase.daoModel().deleteModel(task.value)
            }
        }
    }
    private fun getModel(id:Int):LiveData<Model>? {
        return null
    }
    fun deleteModel(note:Model?) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoModel().deleteModel(note)
        }
    }
    private fun getModels(id:Int):LiveData<List<Model>> {
        return myDatabase.daoModel().getModels(id)
    }
    private val models:LiveData<List<Model>>
        get() = myDatabase.daoModel().fetchAllModel()
    fun getModels(brandIds:List<Int?>?):LiveData<List<Model>> {
        return myDatabase.daoModel().getModelsByBrands(brandIds)
    }
    fun getNewTractorModels(id:Int):LiveData<List<Model>>  {
        //LiveData<Resource<List<Model>>>
//        return object : NetworkBoundResource<List<Model>,ModelResp>(appExecutors) {
//            override fun saveCallResult(item: ModelResp) {
//                if (item.data != null && item.data.data != null)
//                    insertAll(item.data.data)
//            }
//            override fun shouldFetch(data:List<Model>?): Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("newModels")
//            }
//            override fun loadFromDb() = myDatabase.daoModel().getModels(id)
//            override fun createCall() = khetiAPIs.getNewTractorModels(id)
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("newModels")
//            }
//        }.asLiveData()
//
        return myDatabase.daoModel().getModels(id)
    }
    fun getOldTractorModels(id:Int):MutableLiveData<Resource<List<Model>>> {
        val liveData = MutableLiveData<Resource<List<Model>>>()

        liveData.postValue(Resource<List<Model>>(Status.LOADING,null,null))
        khetiAPIs.getOldTractorModels(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeWith(object : DisposableSingleObserver<ModelResp?>() {
//                override fun onSuccess(modelResp:  ModelResp) {
//                    liveData.postValue(Resource(Status.SUCCESS,modelResp!!.data.data,null))
//                }
//                override fun onError(e: @NonNull Throwable) {
//                    liveData.postValue(Resource<List<Model>>(Status.ERROR,null,e!!.message))
//                }
//            })

            .subscribeWith(object : DisposableSingleObserver<ModelResp>() {
                // This method handles the successful emission of one item.
                override fun onSuccess(modelResp: ModelResp) {
                    liveData.postValue(Resource(Status.SUCCESS,modelResp!!.data.data,null))

                }

                // This method handles any error that terminates the stream.
                override fun onError(e: Throwable) {
                    // Handle the error here. It's crucial for robust code.
                  //  Log.e("ModelRepository", "An error occurred while fetching data", e)

                    liveData.postValue(Resource<List<Model>>(Status.ERROR,null,e!!.message))

                }
            })

        return liveData
    }
}