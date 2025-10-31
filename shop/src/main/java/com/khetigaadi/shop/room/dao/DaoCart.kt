package com.khetigaadi.shop.room.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.khetigaadi.shop.model.OrderProduct
import com.khetigaadi.shop.model.Product

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
@Dao
interface DaoCart {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun  insertCartItem(orderProduct:OrderProduct):Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product:Product):Long
    //  @Query("SELECT * FROM OrderProduct Where userId = :id ")
    // Flowable<List<OrderProduct>> getItemByUser(int id);
    @Query("SELECT * FROM OrderProduct Where userId = :id ")
    fun getItemByUser(id:Int):LiveData<List<OrderProduct>>
    // @Query("SELECT * FROM OrderProduct Where userId=:userId")
    // LiveData<List<OrderProduct>> getItemByUserr(int userId);
    @Delete
    fun deleteProduct(product:OrderProduct)
    @Delete
    fun deleteProducts(products: List<OrderProduct>)
    @Query("Update OrderProduct set quantity=:quantityd where ProductId=:productIdd and userId=:userIdd")
    fun updateQuantity(quantityd:Int ,productIdd:Int ,userIdd:Int)
    @Query("SELECT * FROM OrderProduct WHERE ProductId=:id and userId=:userId")
    fun getItemByProductIdAndUserId(id:Int,userId:Int):Single<OrderProduct>
  //  @Query("SELECT * FROM Product WHERE ProductId=:id ")
    //fun getProductById(id:Int):Single<Product>
    @Query("SELECT COUNT(id) FROM OrderProduct where userId=:userId")
    fun getCartCount(userId:Int):Flowable<Int>
    @Query("SELECT quantity FROM OrderProduct where ProductId=:productId and userId=:userId")
    fun getCartProductQuantity(productId:Int,userId:Int):Flowable<Int>
}

//@Dao
//public interface DaoCart {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Long insertCartItem(OrderProduct orderProduct);
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    Long insertProduct(Product product);
//    //  @Query("SELECT * FROM OrderProduct Where userId = :id ")
//    // Flowable<List<OrderProduct>> getItemByUser(int id);
//
//    @Query("SELECT * FROM OrderProduct Where userId = :id ")
//    LiveData<List<OrderProduct>> getItemByUser(@Path("id") int id);
//
//    // @Query("SELECT * FROM OrderProduct Where userId=:userId")
//    // LiveData<List<OrderProduct>> getItemByUserr(int userId);
//    @Delete
//    void deleteProduct(OrderProduct product);
//    @Delete
//    void deleteProducts(List<OrderProduct> products);
//    @Query("Update OrderProduct set quantity=:quantityd where ProductId=:productIdd and userId=:userIdd")
//    void updateQuantity(int quantityd,int productIdd,int userIdd);
//    @Query("SELECT * FROM OrderProduct WHERE ProductId=:id and userId=:userId")
//    Single<OrderProduct> getItemByProductIdAndUserId(int id,int userId);
//    @Query("SELECT * FROM Product WHERE ProductId=:id ")
//    Single<Product> getProductById(int id);
//    @Query("SELECT COUNT(id) FROM OrderProduct where userId=:userId")
//    Flowable<Integer> getCartCount(int userId);
//    @Query("SELECT quantity FROM OrderProduct where ProductId=:productId and userId=:userId")
//    Flowable<Integer> getCartProductQuantity(int productId,int userId);
//}
