package com.khetigaadi.shop.retrofit;
import androidx.lifecycle.LiveData;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.base.room.repository.utils.ApiResponse;
import com.khetigaadi.shop.model.Address;
import com.khetigaadi.shop.model.AddressListResp;
import com.khetigaadi.shop.model.AddressResp;
import com.khetigaadi.shop.model.CategoryListResp;
import com.khetigaadi.shop.model.HomeProductResp;
import com.khetigaadi.shop.model.MyOrderResp;
import com.khetigaadi.shop.model.Order;
import com.khetigaadi.shop.model.OrderResp;
import com.khetigaadi.shop.model.ProductListFilter;
import com.khetigaadi.shop.model.ProductListResp;
import com.khetigaadi.shop.model.ProductResp;
import java.util.Map;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface ShopParentAPIs {
    @GET("mobile/api/v2/shop/products/{id}")
    Single<ProductListResp> getProduct(@Path("id") int id);
    @POST("mobile/api/v2/shop/products")
    Single<ProductListResp> getProducts(@Body ProductListFilter productListFilter,@Query("page") int page);
    @POST("mobile/api/v3/user/address")
    Single<AddressListResp> getAddresses(@Body Map<String,String> params);
    @POST("mobile/api/v2/user/address/{id}/default")
    Single<AddressResp> setAsDefault(@Path("id") String id,@Body Map<String,String> params);
    @POST("mobile/api/v3/user-address")
    Single<AddressResp> addUpdateAddress(@Body Address address);
    @DELETE("mobile/api/v2/user/address/{id}")
    Single<AddressResp> deleteAddress(@Path("id") String id);
    @POST("mobile/api/v5/shop/order")
    Single<MyResponse> placeOrder(@Body Order order);
    @GET("mobile/api/v2/shop/order")
    Single<MyOrderResp> getMyOrders();
    @GET("mobile/api/v4/shop/categories")
    LiveData<ApiResponse<CategoryListResp>> getCategories();
    @GET("mobile/api/v2/shop")
    Single<HomeProductResp> getHomeList();
    @POST("mobile/api/v2/shop/order")
    Single<MyOrderResp> sendBannerClickEnq(@Body Map<String,String> params);
    @POST("mobile/api/v2/shop/enquiry/expert-advise")
    Single<MyResponse> sendExpertAdviseEnq(@Body Map<String,Integer> params);
    @POST("mobile/api/v3/shop/order/list")
    Single<OrderResp> getOrders(@Body Map<String,String> params);
    @POST("mobile/api/v3/shop/product/search")
    Single<ProductListResp> getProductsByName(@Body Map<String,String> params);
}
