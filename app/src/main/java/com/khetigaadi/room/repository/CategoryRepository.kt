package com.khetigaadi.room.repository
import android.content.Context
import androidx.lifecycle.LiveData
import com.khetigaadi.base.room.repository.utils.AppExecutors
import com.khetigaadi.base.room.repository.utils.RateLimiter
import com.khetigaadi.base.room.repository.utils.Resource
import com.khetigaadi.commons.NetworkBoundResource
import com.khetigaadi.retrofit.ShopAPIs
import com.khetigaadi.room.db.MyDatabase
import com.khetigaadi.shop.model.Category
import com.khetigaadi.shop.model.CategoryListResp
import com.khetigaadi.user.SessionManager
import java.util.concurrent.TimeUnit
class CategoryRepository(context:Context?,private val shopAPIs:ShopAPIs,val appExecutors:AppExecutors,val session:SessionManager) {
    private val myDatabase:MyDatabase = MyDatabase.getDatabase(context)
    private val repoListRateLimit = RateLimiter<String>(12,TimeUnit.HOURS)
    fun getAllCategories():LiveData<List<Category>> { //LiveData<Resource<List<Category>>>
//        return object : NetworkBoundResource<List<Category>,CategoryListResp>(appExecutors) {
//            override fun saveCallResult(item:CategoryListResp) {
//                session.setEcomOther(item.others)
//                insertItems(item.categories)
//            }
//            override fun shouldFetch(data: List<Category>?):Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("categories")
//            }
//            override fun loadFromDb() = myDatabase.daoCategory().fetchAllItems()
//            override fun createCall() = shopAPIs.categories
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("categories")
//            }
//        }.asLiveData()
      return myDatabase.daoCategory().fetchAllItems();
    }
    fun getHomeCategories():LiveData<List<Category>> { // LiveData<Resource<List<Category>>>
//        return object : NetworkBoundResource<List<Category>, CategoryListResp>(appExecutors) {
//            override fun saveCallResult(item:CategoryListResp) {
//                session.setEcomOther(item.others)
//                insertItems(item.categories)
//            }
//            override fun shouldFetch(data:List<Category>?):Boolean {
//                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch("categories")
//            }
//            override fun loadFromDb() = myDatabase.daoCategory().fetchHomeItems()
//            override fun createCall() = shopAPIs.categories
//            override fun onFetchFailed() {
//                repoListRateLimit.reset("categories")
//            }
//        }.asLiveData()
//
    return myDatabase.daoCategory().fetchHomeItems();

    }
    fun insertItems(categories:List<Category>) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCategory().insertAll(categories)
        }
    }
    fun insertItem(categories:Category) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCategory().insertCartItem(categories)
        }
    }
    fun deleteItem(categories:Category) {
        MyDatabase.databaseWriteExecutor.execute {
            myDatabase.daoCategory().deleteProduct(categories)
        }
    }
}