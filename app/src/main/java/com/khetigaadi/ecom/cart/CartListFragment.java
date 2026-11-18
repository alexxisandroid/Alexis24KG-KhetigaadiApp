package com.khetigaadi.ecom.cart;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.khetigaadi.R;
import com.khetigaadi.databinding.CartListFragmentBinding;
import com.khetigaadi.ecom.AddressListActivity;
import com.khetigaadi.ecom.adapter.CartListAdapter;
import com.khetigaadi.ecom.address.AddAddressActivity;
import com.khetigaadi.ecom.address.AddressViewModel;
import com.khetigaadi.retrofit.ShopAPIs;
import com.khetigaadi.room.repository.CartRepository;
import com.khetigaadi.shop.model.Address;
//import com.khetigaadi.shop.model.CategoryListResp;
import com.khetigaadi.shop.model.DeliveryCharges;
import com.khetigaadi.shop.model.Order;
import com.khetigaadi.shop.model.OrderProduct;
import com.khetigaadi.user.SessionManager;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@SuppressWarnings({"deprecation", "EqualsBetweenInconvertibleTypes"})
@AndroidEntryPoint
public class CartListFragment extends Fragment {
    public static final String TAG = "CartListFragment";
    private FragmentActivity activity;
    private CartListFragmentBinding binding;
    private int totalAmt = 0;
    @Inject
    CartRepository cartRepository;
    @Inject
    SessionManager sessionManager;
    private CartListAdapter cartListAdapter;
    private AddressViewModel viewModel;
    private Address address;
    @Inject
    ShopAPIs shopAPIs;
    @SuppressWarnings("CallToPrintStackTrace")
    ActivityResultLauncher<Intent> addressActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        try {
                            //noinspection deprecation
                            address = data.getParcelableExtra(AddAddressActivity.SELECTED_ADDRESS);
                            updateView(address);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(activity,getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
    {
        binding = CartListFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        binding.btnChangeAddress.setOnClickListener(v -> addressActivityResultLauncher.launch(new Intent(activity,AddressListActivity.class)));
        binding.btnContinue.setOnClickListener(v -> {
            if(address == null) {
                Toast.makeText(activity, R.string.please_add_address,Toast.LENGTH_SHORT).show();
            }
            else if(cartListAdapter.getList().isEmpty())
                Toast.makeText(activity,R.string.please_add_products,Toast.LENGTH_SHORT).show();
            else {
                Order order = new Order();
                order.setAddress(address);
                order.setProducts(cartListAdapter.getList());
                order.setTotalBill(String.valueOf(totalAmt));
                Intent intent = new Intent(activity,PaymentActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);
            }
        });
        initAdapter();
        initAddress();
        getCartItems();
        return view;
    }
    @SuppressWarnings("CallToPrintStackTrace")
    private void initAddress()
    {
        viewModel.getAddresses(null).observe(getViewLifecycleOwner(),addressListRespStateData -> {
            switch(addressListRespStateData.getStatus()) {
                case LOADING:
                    binding.tvAddressLine1.setVisibility(View.GONE);
                    binding.tvAddressLine2.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    binding.tvAddressLine1.setVisibility(View.VISIBLE);
                    binding.tvAddressLine2.setVisibility(View.VISIBLE);
                    try {
                        if(addressListRespStateData.getData() != null && !addressListRespStateData.getData().getData().isEmpty()) {
                            updateView(addressListRespStateData.getData().getData().get(0));
                        }
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity,addressListRespStateData.getError(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressWarnings("CallToPrintStackTrace")
    private void updateView(Address address) {
        try {
            this.address = address;
            binding.tvAddressLine1.setText(address.getAddress());
            binding.tvAddressLine2.setText(String.format("%s, %s - %s",address.getCity().getName(),address.getState().getName(),address.getPinCode()));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void initAdapter() {
        binding.rvCartList.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
        cartListAdapter = new CartListAdapter(activity,binding.rvCartList);
        binding.rvCartList.setAdapter(cartListAdapter);
        cartListAdapter.setOthers(sessionManager.getEcomOther());
        cartListAdapter.setOnItemClickListener(new CartListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position,View v) {
            }
            @Override
            public void itemUpdate(OrderProduct item,int position) {
                cartRepository.insertItem(item);
            }
            @SuppressWarnings("deprecation")
            @Override
            public void onMinusClick(View view,int position) {
                OrderProduct product = cartListAdapter.getList().get(position);  //.getItem(position);
                if(product.getQuantity() > 1) {
                    product.setQuantity(product.getQuantity() - 1);
                    cartRepository.updateProductQuantity(product.getQuantity(),product.getProductId());

                    cartListAdapter.notifyItemChanged(position);
                    binding.btnContinue.setClickable(false);
                    Handler handler = new Handler();
                    final Runnable r = new Runnable() {
                        public void run() {
                            List<OrderProduct> ordProd = cartRepository.getAllItemss();
                            calculatePayable(ordProd);
                            handler.postDelayed(this,2000);
                        }
                    };
                    handler.postDelayed(r,2000);
                }
            }
            @Override
            public void onPlusClick(View view,int position) {
                OrderProduct product = cartListAdapter.getList().get(position);
                product.setQuantity(product.getQuantity() + 1);
                cartRepository.updateProductQuantity(product.getQuantity(),product.getProductId());
                cartListAdapter.notifyItemChanged(position);
                 binding.btnContinue.setClickable(false);
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        List<OrderProduct> ordProd = cartRepository.getAllItemss();
                        calculatePayable(ordProd);
                        handler.postDelayed(this,2000);
                    }
                };
                handler.postDelayed(r,2000);
            }
            @Override
            public void onDeleteClick(View view,int position) {
                cartRepository.deleteProduct(cartListAdapter.getList().get(position));
            }
        });
    }
    private void getCartItems() {
//        cartRepository.getAllItems().observe(getViewLifecycleOwner(),products -> {
//
//            if (products != null) { Log.d("xcvcxvcxvxv===",""+products.size());
//                cartListAdapter.updateItems(products);
//                calculatePayable(products);
//
//            }
//            else Log.d("xcvcxvcxvxv===",""+products);
//        });
        List<OrderProduct> products = cartRepository.getAllItemss();
        if(products != null) {
            //Log.d("xcvcxvcxvxv===",""+products.size()+"=="+products.get(0).getProduct().toString());
            cartListAdapter.updateItems(products);
            calculatePayable(products);
        }
    }
    @SuppressLint("DefaultLocale")
    private void calculatePayable(List<OrderProduct> products) {
        totalAmt = 0;
        for(OrderProduct product:products) {
           // if(product.getProduct()!=null && !product.getProduct().equals(null) && !product.getProduct().equals("null"))
          if(product.getProduct() != null && !product.getProduct().equals("null"))
               totalAmt += (product.getProduct().getFinalPrice()*product.getQuantity());
        }
        binding.tvSubTotalAmount.setText(String.format("%s %d",getString(R.string.rs),totalAmt));
        if(totalAmt < 1000) {
            binding.tvShippingAmount.setText(String.format("%s 100",getString(R.string.rs)));
            totalAmt+= 100;
        }
        else {
            binding.tvShippingAmount.setText(String.format("%s 0", getString(R.string.rs)));
        }
        binding.tvTotalAmount.setText(String.format("%s %d",getString(R.string.rs),totalAmt));
        binding.btnContinue.setClickable(true);
    }
//    @SuppressLint("CheckResult")
//    public void delvCharges()
//    {
//        //noinspection ResultOfMethodCallIgnored
//        shopAPIs.getDeliveryCharges()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<DeliveryCharges>() {
//                    @SuppressLint("CheckResult")
//                    @Override
//                    public void onSuccess(@NonNull DeliveryCharges productListResp) {
////                        Log.d("cvxcxxcvxv234===",""+productListResp.isError()+"=="+productListResp.getCategories().size());
////                        if(productListResp.isError()) {
////                           // binding.rvProductList.setVisibility(View.GONE);
////                        }
////                        else {
////                          //  binding.rvProductList.setVisibility(View.VISIBLE);
////                            Log.d("cvxcxxcvxv===",""+productListResp.getCategories().size());
////                          //  categoryRepository.insertItems(productListResp.getCategories());
////                          //  binding.rvProductList.setVisibility(View.VISIBLE);
////                          //  categoriesAdapter.addItems(productListResp.getCategories(),1,sessionManager.getEcomOther());
////                        }
//                    }
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Timber.e(e,"onError: ");
//                        // binding.rvProductList.setVisibility(View.GONE);
//                        // gardenTools.postError(e.getMessage());
//                    }
//                });
//    }
}
