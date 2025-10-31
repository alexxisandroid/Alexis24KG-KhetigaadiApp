package com.khetigaadi.ecom;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.khetigaadi.R;
import com.khetigaadi.base.BaseActivity;
import com.khetigaadi.commons.activities.SearchActivity;
import com.khetigaadi.databinding.CartOptionWithBadgeBinding;
import com.khetigaadi.ecom.cart.CartActivity;
import com.khetigaadi.ecom.order.MyOrderListActivity;
import com.khetigaadi.room.repository.CartRepository;
import javax.inject.Inject;
public abstract class CartBaseActivity extends BaseActivity {
    @Inject
    CartRepository cartRepository;
    private ProgressDialog progressDialog;
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ecom_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem saveMenu = menu.findItem(R.id.cart_menu);
        if (saveMenu != null) {
            cartRepository.getAllItems().observe(this, products -> {
                if (products != null && products.size() != 0) {
                    CartOptionWithBadgeBinding binding = CartOptionWithBadgeBinding.inflate(getLayoutInflater());
                    saveMenu.setActionView(binding.getRoot());
                    saveMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                    binding.tvCartItemCount.setText(String.valueOf(products.size()));
                    binding.getRoot().setOnClickListener(v -> navigateToCart());
                }
            });
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            startActivity(new Intent(this,SearchActivity.class));
            return true;
        } else if (item.getItemId() == R.id.cart_menu) {
            navigateToCart();
            return true;
        } else if (item.getItemId() == R.id.my_order_menu) {
            navigateToMyOrders();
            return true;
        }
        return false;
    }
    protected void navigateToMyOrders() {
        startActivity(new Intent(this,MyOrderListActivity.class));
    }
    protected void navigateToCart() {
        startActivity(new Intent(this,CartActivity.class));
    }
//    protected void showProgressDialog(String message) {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(getApplicationContext());
//        }
//        if (!progressDialog.isShowing()) {
//            progressDialog = new ProgressDialog(getApplicationContext());
//            progressDialog.setMessage(message);
//            progressDialog.show();
//        }
//    }
//    //showOrUpdateProgressDialog
//    protected void showOrUpdateProgressDialog(String message) {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(getApplicationContext());
//        }
//        if (!progressDialog.isShowing()) {
//            progressDialog = new ProgressDialog(getApplicationContext());
//            progressDialog.setMessage(message);
//            progressDialog.show();
//        }
//    }
//    protected void dismissProgressDialog()
//    {
//        try {
//            if (progressDialog != null && progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
