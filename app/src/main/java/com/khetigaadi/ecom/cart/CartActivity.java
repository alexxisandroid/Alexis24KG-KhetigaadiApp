package com.khetigaadi.ecom.cart;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.activities.SearchActivity;
import com.khetigaadi.databinding.ActivityCartBinding;
import com.khetigaadi.ecom.order.MyOrderListActivity;
import com.khetigaadi.room.repository.CartRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;
@AndroidEntryPoint
public class CartActivity extends BaseActivity {
    public static final String TAG = "CartActivity";
    private ActivityCartBinding binding;
    @Inject
    CartRepository cartRepository;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        toolbarInit();
        callFragment();
        registerReceiverForFinish();
    }
    private void toolbarInit() {
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarI.toolbar.setTitle(getString(R.string.my_cart));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

            actionBar.setTitle(getResources().getString(R.string.my_cart));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@androidx.annotation.NonNull Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ecom_menu, menu);
        menu.findItem(R.id.cart_menu).setVisible(false);
        menu.findItem(R.id.my_order_menu).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        if(item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous activity
            return true;
        }
        else if(item.getItemId() == R.id.search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        else if(item.getItemId() == R.id.cart_menu) {
            startActivity(new Intent(this,CartActivity.class));
            return true;
        }
        else if(item.getItemId() == R.id.my_order_menu) {
            startActivity(new Intent(this, MyOrderListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void registerReceiverForFinish() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TAG.equals(intent.getAction())) {
                    finish();
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(TAG));
    }
    private void callFragment() {
        disposable.add(cartRepository.getCartCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer count) {
                        Log.d("cxvxcvxvv====",""+count);
                        try {
                            if (count == null || count == 0) {
                                binding.fragmentContainerView.setVisibility(View.VISIBLE);
                                binding.fragmentContainerViewList.setVisibility(View.GONE);
                            } else {
                                binding.fragmentContainerView.setVisibility(View.GONE);
                                binding.fragmentContainerViewList.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                                       }
                                   }

                                   @Override
                                   public void onError(Throwable t) {

                                   }

                                   @Override
                                   public void onComplete() {

                                   }
                               }
                ));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        if (broadcastReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}
