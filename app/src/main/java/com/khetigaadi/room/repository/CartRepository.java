package com.khetigaadi.room.repository;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.khetigaadi.room.db.MyDatabase;
import com.khetigaadi.shop.model.OrderProduct;
import com.khetigaadi.user.SessionManager;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
public class CartRepository {
    private final MyDatabase noteDatabase;
    private SessionManager sessionManager;
    public CartRepository(Context context,SessionManager sessionManager) {
        noteDatabase = MyDatabase.getDatabase(context);
        this.sessionManager = sessionManager;
    }
    public void insertItem(final OrderProduct orderProduct) {
       try {
           MyDatabase.databaseWriteExecutor.execute(() -> {
               if (orderProduct.getQuantity() == 0)
                   orderProduct.setQuantity(1);
               String id = sessionManager.getCustomer().getId();
               orderProduct.setUserId(TextUtils.isEmpty(id) ? 0 : Integer.parseInt(id));
               orderProduct.setProductId(orderProduct.getProduct().getProductId());
               long opdCart = noteDatabase.daoCart().insertCartItem(orderProduct);
               long insProd = noteDatabase.daoCart().insertProduct(orderProduct.getProduct());


               Log.d("cvxvcxvxvcxv===", "" + opdCart + "==" + insProd+"=="+orderProduct.toString()+"=="+orderProduct.getProduct());
           });
       }
       catch(Exception e) {
        Log.d("fgdgfdgdfgcvbb122232====",""+e.getLocalizedMessage());
       }
    }
    public void updateProductQuantity(int quantity,int productId) {
        MyDatabase.databaseWriteExecutor.execute(() -> {
            String userId = sessionManager.getCustomer().getId();
           // noteDatabase.daoCart().updateQuantity(quantity,productId,TextUtils.isEmpty(userId) ? 0 : Integer.parseInt(userId));
            if(userId!=null)
                noteDatabase.daoCart().updateQuantity(quantity,productId,Integer.parseInt(userId));
        });
    }
    public LiveData<List<OrderProduct>> getAllItems() {
        String id = sessionManager.getUser().getId();
        MutableLiveData<List<OrderProduct>> listLiveData = new MutableLiveData<>();
     //  noteDatabase.daoCart().getItemByUser(Integer.parseInt(id));
       // noteDatabase.daoCart().getItemByUser(TextUtils.isEmpty(id) ? 0 : Integer.parseInt(sessionManager.getUser().getId()))
        int dVal = Integer.parseInt(sessionManager.getUser().getId());
        Log.d("xcxvcvxcvcvv===",""+dVal+"=="+"=="+noteDatabase.daoCart().getItemByUsers().getValue()+"=="+noteDatabase.daoCart().getItemByUsersCount()+"=="+noteDatabase.daoCart().getItemByUserss());
       // listLiveData.postValue(noteDatabase.daoCart().getItemByUser(dVal).getValue());
        listLiveData.postValue(noteDatabase.daoCart().getItemByUsers().getValue());
//        noteDatabase.daoCart().getItemByUser(dVal)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableSubscriber<List<OrderProduct>>() {
//                    @Override
//                    public void onNext(List<OrderProduct> orderProducts) {
//                        if (orderProducts.size() == 0) {
//                            listLiveData.postValue(orderProducts);
//                            return;
//                        }
//                        for (OrderProduct orderProduct : orderProducts) {
//                            noteDatabase.daoCart().getProductById(orderProduct.getProductId())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribeOn(Schedulers.io())
//                                    .subscribe(new DisposableSingleObserver<Product>() {
//                                        @Override
//                                        public void onSuccess(@NonNull Product product) {
//                                            orderProduct.setProduct(product);
//                                            listLiveData.postValue(orderProducts);
//                                        }
//                                        @Override
//                                        public void onError(@NonNull Throwable e) {
//                                        }
//                                    });
//                        }
//                    }
//                    @Override
//                    public void onError(Throwable t) {
//                    }
//                    @Override
//                    public void onComplete() {
//                    }
//                });
        return listLiveData;
    }
    public List<OrderProduct> getAllItemss() {
        String id = sessionManager.getUser().getId();
        List<OrderProduct> listLiveData = new ArrayList<OrderProduct>();
        //  noteDatabase.daoCart().getItemByUser(Integer.parseInt(id));
        // noteDatabase.daoCart().getItemByUser(TextUtils.isEmpty(id) ? 0 : Integer.parseInt(sessionManager.getUser().getId()))
        int dVal = Integer.parseInt(sessionManager.getUser().getId());
        Log.d("xcxvcvxcvcvv===",""+dVal+"=="+"=="+noteDatabase.daoCart().getItemByUsers().getValue()+"=="+noteDatabase.daoCart().getItemByUsersCount()+"=="+noteDatabase.daoCart().getItemByUserss());
        // listLiveData.postValue(noteDatabase.daoCart().getItemByUser(dVal).getValue());
        //listLiveData.postValue(noteDatabase.daoCart().getItemByUserss());
//        noteDatabase.daoCart().getItemByUser(dVal)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableSubscriber<List<OrderProduct>>() {
//                    @Override
//                    public void onNext(List<OrderProduct> orderProducts) {
//                        if (orderProducts.size() == 0) {
//                            listLiveData.postValue(orderProducts);
//                            return;
//                        }
//                        for (OrderProduct orderProduct : orderProducts) {
//                            noteDatabase.daoCart().getProductById(orderProduct.getProductId())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribeOn(Schedulers.io())
//                                    .subscribe(new DisposableSingleObserver<Product>() {
//                                        @Override
//                                        public void onSuccess(@NonNull Product product) {
//                                            orderProduct.setProduct(product);
//                                            listLiveData.postValue(orderProducts);
//                                        }
//                                        @Override
//                                        public void onError(@NonNull Throwable e) {
//                                        }
//                                    });
//                        }
//                    }
//                    @Override
//                    public void onError(Throwable t) {
//                    }
//                    @Override
//                    public void onComplete() {
//                    }
//                });
        return noteDatabase.daoCart().getItemByUserss();
        //  return listLiveData;
    }
    public Single<OrderProduct> getItemByProductId(int id) {
        String userId = sessionManager.getUser().getId();
       // return null;
       // return noteDatabase.daoCart().getItemByProductIdAndUserId(id, TextUtils.isEmpty(userId) ? 0 : Integer.parseInt(userId));
        if(userId.isEmpty())
            userId = "0";
        return noteDatabase.daoCart().getItemByProductIdAndUserId(id,Integer.parseInt(userId));

    }
    public void deleteProduct(final OrderProduct product) {
        if (product != null) {
            MyDatabase.databaseWriteExecutor.execute(() -> noteDatabase.daoCart().deleteProduct(product));
        }
    }
    public void deleteProducts(final List<OrderProduct> products) {
        if (products != null) {
            MyDatabase.databaseWriteExecutor.execute(() -> noteDatabase.daoCart().deleteProducts(products));
        }
    }
    public Flowable<Integer> getCartCount() {
        String userId = sessionManager.getCustomer().getId();
        //return null;
        return noteDatabase.daoCart().getCartCount(TextUtils.isEmpty(userId) ? 0 : Integer.parseInt(userId));
    }
    public Flowable<Integer> getProductQuantity(int productId) {
        String userId = sessionManager.getCustomer().getId();
       // return null;
        return noteDatabase.daoCart().getCartProductQuantity(productId, TextUtils.isEmpty(userId) ? 0 : Integer.parseInt(userId));
    }
}
