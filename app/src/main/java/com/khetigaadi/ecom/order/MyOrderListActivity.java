package com.khetigaadi.ecom.order;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.khetigaadi.R;
import com.khetigaadi.databinding.ActivityMyOrderListBinding;
import com.khetigaadi.user.SessionManager;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class MyOrderListActivity extends AppCompatActivity {
    private ActivityMyOrderListBinding binding;
    private MyOrderListViewModel viewModel;
    private MyOrderListAdapter adapter;
    @Inject
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = ActivityMyOrderListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(MyOrderListViewModel.class);
        toolbarInit();
        initAdapter();
        getItems();
    }
    private void getItems() {
        viewModel.getMyOrders().observe(this, myOrderRespStateData -> {
            Log.d("vcbcvbcvb===",""+myOrderRespStateData.getStatus()+"=="+myOrderRespStateData.getData());
            switch (myOrderRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    if (myOrderRespStateData.getData() != null) {
                       try {
                           binding.llProgresss.llProgress.setVisibility(View.GONE);
                           adapter.setOthers(myOrderRespStateData.getData().getOthers());
                           adapter.updateItems(myOrderRespStateData.getData().getData());
                       }
                       catch(Exception e) {
                           e.printStackTrace();
                       }
                    }
                    break;
                case ERROR:
                    Toast.makeText(this,myOrderRespStateData.getError(),Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
    private void toolbarInit() {
      try {
          setSupportActionBar(binding.toolbars.toolbar);
          binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
          binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
          binding.toolbars.toolbar.setTitle(getString(R.string.my_orders));
          ActionBar actionBar = getSupportActionBar();
          if (actionBar != null) {
              actionBar.setDisplayHomeAsUpEnabled(true);
              actionBar.setDisplayShowHomeEnabled(true);
              actionBar.setTitle(getResources().getString(R.string.my_orders));
          }
      }
      catch(Exception e) {
          e.printStackTrace();
      }
    }
    private void initAdapter() {
      try {
          binding.rvMyOrderList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
          adapter = new MyOrderListAdapter(this, binding.rvMyOrderList);
          binding.rvMyOrderList.setAdapter(adapter);
          adapter.setOthers(sessionManager.getEcomOther());
      }
      catch (Exception e){
          e.printStackTrace();
      }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close this activity and return to the previous activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}