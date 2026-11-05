package com.khetigaadi.tractor.rent.rentTractorNewUi;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityRentTractorBrandBinding;
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.room.repository.BrandRepository;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class RentTractorBrand extends BaseActivity {
    private static final String TAG = RentTractorBrand.class.getSimpleName();
    @Inject
    BrandRepository brandRepository;
    private Brand selectedBrand;
    int brand_id = 0;
    @Inject
    SharedPreferenceUtils pref;
    private ActivityRentTractorBrandBinding binding;
    // private Boolean firstCall = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        binding = ActivityRentTractorBrandBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(),(v,insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
//        EdgeToEdge.enable(this);
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.rentTractor));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.rentTractor));
        iconColorChange();
        binding.etSellTractorBrand.setOnClickListener(v -> {
            Intent intent = new Intent(RentTractorBrand.this, BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE, BrandType.OLD_TRACTOR);
            startActivityForResult(intent, BrandSelectionActivity.BRAND_SELECTION_REQ);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close_button_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.menu_close) {
            showAlerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == BrandSelectionActivity.BRAND_SELECTION_REQ) {
            if(resultCode == RESULT_OK) {
                selectedBrand = data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND);
                if(selectedBrand != null) {
                    binding.etSellTractorBrand.setText(selectedBrand.getName());
                    brand_id = selectedBrand.getId();
                    searchBrandByModel(selectedBrand, brand_id);
                }
            }
        }
    }
    private void iconColorChange() {
        binding.tabs.tvBrand.setTextColor(ContextCompat.getColor(this,R.color.red));
    }
    private void initList() {
//        brandRepository.getOldTractorBrands().observe(this,listResource -> {
//            if (listResource.getStatus() == Status.SUCCESS) {
//                List<Brand> brandParents = listResource.getData();
//                String baseUrl = pref.getStringValue(Constants.BRAND_BASE_URL,"");
//                Timber.e("onChanged: %s", brandParents.toString());
//                try {
//                    binding.llBrand.removeAllViews();
//                    for (int i = 0; i < brandParents.size(); i = i + 4) {
//                        LinearLayout linearLayout = new LinearLayout(RentTractorBrand.this);
//                        linearLayout.setWeightSum(4);
//                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                        for (int j = 0; j <= 3 && i + j < brandParents.size(); j++) {
//                            BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
//                            final Brand brand = brandParents.get(i + j);
//                            Glide.with(RentTractorBrand.this)
//                                    .load(brand.getImage() != null ? baseUrl + brand.getImage() : "")
//                                    .placeholder(R.mipmap.no_image)
//                                    .error(R.mipmap.no_image)
//                                    .into(rowBinding.image);
//
//                            if (!pref.getRentBrandId().isEmpty()) {
//                                if (pref.getRentBrandId().equalsIgnoreCase(String.valueOf(brand.getId()))) {
//                                    rowBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
//                                }
//                            }
//
//                            rowBinding.getRoot().setOnClickListener(v -> {
//                                binding.etSellTractorBrand.setText(brand.getName());
//                                brand_id = brand.getId();
//                                rowBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
//                                pref.setRentBrandId(String.valueOf(brand.getId()));
//                                initList();
//                                searchBrandByModel(brand, brand_id);
//                            });
//                            linearLayout.addView(rowBinding.getRoot());
//                        }
//                        binding.llBrand.addView(linearLayout);
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//

        brandRepository.getOldTractorBrands().observe(this,new Observer<List<com.khetigaadi.implement.newImplement.util.Brand>>() {
            @Override
            public void onChanged(@Nullable List<com.khetigaadi.implement.newImplement.util.Brand> brands) {
                try {
                    assert brands != null;
                    if(!brands.isEmpty()) {

                List<Brand> brandParents = brands;
                String baseUrl = pref.getStringValue(Constants.BRAND_BASE_URL,"");
                Timber.e("onChanged: %s",brandParents.toString());
                try {
                    binding.llBrand.removeAllViews();
                    for (int i = 0; i < brandParents.size(); i = i + 4) {
                        LinearLayout linearLayout = new LinearLayout(RentTractorBrand.this);
                        linearLayout.setWeightSum(4);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        for (int j = 0; j <= 3 && i + j < brandParents.size(); j++) {
                            BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                            final Brand brand = brandParents.get(i + j);
                            Glide.with(RentTractorBrand.this)
                                    .load(brand.getImage() != null ? baseUrl + brand.getImage() : "")
                                    .placeholder(R.mipmap.no_image)
                                    .error(R.mipmap.no_image)
                                    .into(rowBinding.image);

                            if (!pref.getRentBrandId().isEmpty()) {
                                if (pref.getRentBrandId().equalsIgnoreCase(String.valueOf(brand.getId()))) {
                                    rowBinding.ll.setBackground(ContextCompat.getDrawable(RentTractorBrand.this,R.drawable.button_selector_red));
                                }
                            }
                            rowBinding.getRoot().setOnClickListener(v -> {
                                binding.etSellTractorBrand.setText(brand.getName());
                                brand_id = brand.getId();
                                rowBinding.ll.setBackground(ContextCompat.getDrawable(RentTractorBrand.this,R.drawable.button_selector_red));
                                pref.setRentBrandId(String.valueOf(brand.getId()));
                                initList();
                                searchBrandByModel(brand, brand_id);
                            });
                            linearLayout.addView(rowBinding.getRoot());
                        }
                        binding.llBrand.addView(linearLayout);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void searchBrandByModel(Brand brand,Integer brandId) {
        if(brandId != 0) {
            pref.setRentBrandId(String.valueOf(brandId));
            Intent intent = new Intent(this, RentTractorModel.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("brand",brand);
            //intent.putExtra("brand_id", String.valueOf(brandId));
            startActivity(intent);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
    private void showAlerDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_quit))
                .setNegativeButton(getString(R.string.no),(dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes),(dialog, which) -> {
                    dialog.cancel();
                    addSomething();
                }).show();
    }
    private void addSomething() {
        if(!pref.getRentBrandId().isEmpty()) {
            pref.removeKey("RENTBRANDID");
        }
        Intent intent = new Intent(RentTractorBrand.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Check","1");
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!pref.getRentBrandId().isEmpty()) {
            pref.removeKey("RENTBRANDID");
        }
    }
}
