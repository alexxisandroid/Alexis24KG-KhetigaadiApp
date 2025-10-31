package com.khetigaadi.ecom;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
//import com.khetigaadi.databinding.ActivityAddressListBinding;
import com.khetigaadi.databinding.ActivityAddressListBinding;
import com.khetigaadi.ecom.adapter.AddressListAdapter;
import com.khetigaadi.ecom.address.AddAddressActivity;
import com.khetigaadi.ecom.address.AddressViewModel;
//import com.khetigaadi.shop.databinding.ActivityAddressListBinding;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class AddressListActivity extends AppCompatActivity {
    private ActivityAddressListBinding binding;
    private AddressListAdapter addressListAdapter;
    private AddressViewModel viewModel;
    ActivityResultLauncher<Intent> addAddressActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        try {
                            setResult(RESULT_OK, data);
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityAddressListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        viewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        initToolbar();
        Glide.with(this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
        initAdapter();
        binding.btnAddAddress.setOnClickListener(v -> addAddressActivityResultLauncher.launch(new Intent(AddressListActivity.this,AddAddressActivity.class)));
        binding.btnContinue.setOnClickListener(v -> {
            if(!addressListAdapter.getList().isEmpty()) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(AddAddressActivity.SELECTED_ADDRESS,addressListAdapter.getDefaultAddress());
                setResult(RESULT_OK,returnIntent);
                finish();
            }
            else
                Toast.makeText(this,R.string.please_add_address,Toast.LENGTH_SHORT).show();
        });
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarI.toolbar.setTitle(getString(R.string.select_address));
    }
    private void initAdapter() {
        binding.rvAddressList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addressListAdapter = new AddressListAdapter(this, binding.rvAddressList);
        binding.rvAddressList.setAdapter(addressListAdapter);
        addressListAdapter.setOnItemClickListener(new AddressListAdapter.ClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                callSetAsDefaultApi(position);
            }
            @Override
            public void onEditClick(View view, int position) {
                Intent intent = new Intent(AddressListActivity.this, AddAddressActivity.class);
                intent.putExtra("address",addressListAdapter.getItem(position));
                addAddressActivityResultLauncher.launch(intent);
            }
            @Override
            public void onDeleteClick(View view, int position) {
                callDeleteApi(position);
            }
        });
        viewModel.getAddresses(null).observe(this, addressListRespStateData -> {
            switch (addressListRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.llProgresss.llProgress.setVisibility(View.GONE);
                    if (addressListRespStateData.getData() != null) {
                        addressListAdapter.updateItems(addressListRespStateData.getData().getData());
                    }
                    break;
                case ERROR:
                    onBackPressed();
                    Toast.makeText(this, addressListRespStateData.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void callDeleteApi(int position) {
        viewModel.deleteAddress(""+addressListAdapter.getItem(position).getId()).observe(this, addressListRespStateData -> {
            switch (addressListRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.llProgresss.llProgress.setVisibility(View.GONE);
                    addressListAdapter.itemRemove(position);
                    break;
                case ERROR:
                    onBackPressed();
                    Toast.makeText(AddressListActivity.this, addressListRespStateData.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void callSetAsDefaultApi(int position) {
        viewModel.setAsDefault(""+addressListAdapter.getItem(position).getId(), null).observe(this, addressListRespStateData -> {
            switch (addressListRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.llProgresss.llProgress.setVisibility(View.GONE);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(AddAddressActivity.SELECTED_ADDRESS,addressListAdapter.getItem(position));
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    break;
                case ERROR:
                    onBackPressed();
                    Toast.makeText(AddressListActivity.this,addressListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}