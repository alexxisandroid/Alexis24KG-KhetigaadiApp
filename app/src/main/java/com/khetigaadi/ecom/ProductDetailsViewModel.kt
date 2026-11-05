package com.khetigaadi.ecom
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.khetigaadi.base.model.MyResponse
import com.khetigaadi.base.util.StateMutableLiveData
import com.khetigaadi.retrofit.ShopAPIs
import com.khetigaadi.room.repository.CartRepository
import com.khetigaadi.shop.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subscribers.DisposableSubscriber
import timber.log.Timber
import javax.inject.Inject
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val shopAPIs:ShopAPIs,private val cartRepository:CartRepository,private val disposable:CompositeDisposable,savedStateHandle:SavedStateHandle):ViewModel() {
    var product: Product?
    var imagePath: String?
    private val productListFilter: ProductListFilter
    var quantity = MutableLiveData<Int>()
    fun onResume() {
        if (product != null) {
            disposable.add(cartRepository.getProductQuantity(product!!.productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<Int>() {
                    override fun onNext(integer: Int) {
                        if (integer != 0) quantity.postValue(integer) else quantity.postValue(0)
                    }

                    override fun onError(t: Throwable) {
                        quantity.postValue(0)
                    }

                    override fun onComplete() {}
                }))
        } else {
            quantity.setValue(0)
        }
    }
    //        disposable.add(shopAPIs.getProduct())
    val productDetails:StateMutableLiveData<OrderProduct>
        get() {
            val orderProduct = OrderProduct()
            orderProduct.product = product
            val value = quantity.value
            if(value != null) {
                orderProduct.quantity = value
            }
            val gardenTools = StateMutableLiveData<OrderProduct>()
            gardenTools.postSuccess(orderProduct)
            disposable.add(shopAPIs.getProduct(product!!.productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<ProductResp>() {
                    override fun onSuccess(productListResp: ProductResp) {
                        if(productListResp.isError) gardenTools.postError(""+productListResp.msg[0])
                        else {
                            orderProduct.product = productListResp.data
                            imagePath = productListResp.others.shopProductImagePath
                            gardenTools.postSuccess(orderProduct)
                        }
                    }
                    override fun onError(e:Throwable) {
                        Timber.e(e,"onError: ")
                        gardenTools.postError(e.message)
                    }
                }))
            return gardenTools
            //        disposable.add(shopAPIs.getProduct())
        }
    val similarProducts:StateMutableLiveData<ProductListResp>
        get() {
            val gardenTools = StateMutableLiveData<ProductListResp>()
            gardenTools.postLoading()
            disposable.add(shopAPIs.getProducts(productListFilter,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<ProductListResp>() {
                    override fun onSuccess(productListResp:ProductListResp) {
                        if (productListResp.isError) gardenTools.postError(""+productListResp.msg[0])
                        else gardenTools.postSuccess(productListResp)
                    }
                    override fun onError(e:Throwable) {
                        Timber.e(e, "onError: ")
                        gardenTools.postError(e.message)
                    }
                }))
            return gardenTools
        }
    fun plusClick(quantity:Int) {
        cartRepository.updateProductQuantity(quantity, product!!.productId)
    }
    fun minusClick(quantity:Int) {
        if (quantity >= 1) cartRepository.updateProductQuantity(quantity,product!!.productId)
    }
    fun onExpertAdviceClicked():StateMutableLiveData<MyResponse> {
        val gardenTools = StateMutableLiveData<MyResponse>()
        gardenTools.postLoading()
        val params: MutableMap<String, Int> = HashMap()
        params["product_id"] = product!!.productId
        shopAPIs.sendExpertAdviseEnq(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableSingleObserver<MyResponse>() {
                override fun onSuccess(myResponse: MyResponse) {
                    Timber.e("onSuccess: %s", myResponse.msg)
                    gardenTools.postError(myResponse.msg[0])
                }
                override fun onError(e: Throwable) {
                    Timber.e("onError: %s", e.message)
                    gardenTools.postError(e.message)
                }
            })
        return gardenTools
    }
    init {
        product = savedStateHandle["product"]
        imagePath = savedStateHandle["imagePath"]
        val productId = savedStateHandle.get<Int>("productId")
        if(productId != null) {
            product = Product()
            product!!.productId = productId
        }
        productListFilter = ProductListFilter()
    }
}