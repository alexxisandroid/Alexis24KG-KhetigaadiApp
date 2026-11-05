package com.khetigaadi.isOwner;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.databinding.IsOwnerBrandScreenBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.room.repository.BrandRepository;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class IsOwnerTractorBrand extends BaseActivity {
    @Inject
    BrandRepository brandRepository;
    int brand_id = 0;
    @Inject
    SharedPreferenceUtils pref;
    private IsOwnerBrandScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);
        binding = IsOwnerBrandScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(),(v,insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarI.toolbar.setTitle(getString(R.string.owner));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.owner));
        iconColorChange();
        binding.etSellTractorBrand.setOnClickListener(v -> {
            Intent intent = new Intent(IsOwnerTractorBrand.this,BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE,BrandType.OLD_TRACTOR);
            startActivityForResult(intent,BrandSelectionActivity.BRAND_SELECTION_REQ);
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BrandSelectionActivity.BRAND_SELECTION_REQ) {
            if (resultCode == RESULT_OK) {
                Brand selectedBrand = data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND);
                if (selectedBrand != null) {
                    binding.etSellTractorBrand.setText(selectedBrand.getName());
                    brand_id = selectedBrand.getId();
                    searchBrandByModel(selectedBrand, brand_id);
                }
            }
        }
    }
    private void iconColorChange() {
        binding.tabBrand.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    private void initList() {
        String baseUrl = SharedPreferenceUtils.getInstance(this).getStringValue(Constants.BRAND_BASE_URL, "");
//        brandRepository.getOldTractorBrands().observe(this, listResource -> {
//            if (listResource.getStatus() == Status.SUCCESS) {
//                try {
//                    binding.llBrand.removeAllViews();
//                    // for (int i = 0; i < brandParents.size(); i = i + 3) {
//                    for (int i = 0; i < listResource.getData().size(); i = i + 4) {
//                        LinearLayout linearLayout = new LinearLayout(IsOwnerTractorBrand.this);
//                        //linearLayout.setWeightSum(3);
//                        linearLayout.setWeightSum(4);
//                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                        for (int j = 0; j <= 3 && i + j < listResource.getData().size(); j++) {
//                            BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
//                            final Brand brand = listResource.getData().get(i + j);
//                            if (brand.getImage() != null) {
//                                Glide.with(IsOwnerTractorBrand.this).load(baseUrl + brand.getImage()).into(rowBinding.image);
//                            } else {
//                                Glide.with(IsOwnerTractorBrand.this).load(R.mipmap.ic_launcher).into(rowBinding.image);
//                            }
//                            if (!pref.getIsOwnerBrandId().isEmpty()) {
//                                if (pref.getIsOwnerBrandId().equalsIgnoreCase(String.valueOf(brand.getId()))) {
//                                    rowBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
//                                }
//                            }
//                            rowBinding.getRoot().setOnClickListener(v -> {
//                                binding.etSellTractorBrand.setText(brand.getName());
//                                brand_id = brand.getId();
//                                rowBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
//                                pref.setIsOwnerBrandId(String.valueOf(brand.getId()));
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
//
//
//            }
//        });

        brandRepository.getOldTractorBrandsSell().observe(this,new Observer<List<com.khetigaadi.implement.newImplement.util.Brand>>() {
            @Override
            public void onChanged(@Nullable List<com.khetigaadi.implement.newImplement.util.Brand> brands) {
                try {
                    binding.llBrand.removeAllViews();
                    // for (int i = 0; i < brandParents.size(); i = i + 3) {
                    for(int i = 0; i < brands.size(); i = i + 3) {
                        LinearLayout linearLayout = new LinearLayout(IsOwnerTractorBrand.this);
                        //linearLayout.setWeightSum(3);
                        linearLayout.setWeightSum(3);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        for(int j = 0; j <= 2 && i + j < brands.size(); j++) {
                            BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                             rowBinding.getRoot().setBackgroundColor(Color.WHITE);
                            final Brand brand = brands.get(i + j);
                            if(brand.getImage() != null) {
                                Glide.with(IsOwnerTractorBrand.this).load(baseUrl + brand.getImage()).into(rowBinding.image);
                            }
                            else {
                                Glide.with(IsOwnerTractorBrand.this).load(R.mipmap.ic_launcher).into(rowBinding.image);
                            }
                            if(!pref.getIsOwnerBrandId().isEmpty()) {
                                if(pref.getIsOwnerBrandId().equalsIgnoreCase(String.valueOf(brand.getId()))) {
                                    rowBinding.ll.setBackground(ContextCompat.getDrawable(IsOwnerTractorBrand.this,R.drawable.button_selector_red));
                                }
                            }
                            rowBinding.getRoot().setOnClickListener(v -> {
                                binding.etSellTractorBrand.setText(brand.getName());
                                brand_id = brand.getId();
                                rowBinding.ll.setBackground(ContextCompat.getDrawable(IsOwnerTractorBrand.this, R.drawable.button_selector_red));
                                pref.setIsOwnerBrandId(String.valueOf(brand.getId()));
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
        });
    }
    private void searchBrandByModel(Brand brand,Integer brandId) {
        if(brandId != 0) {
            pref.setIsOwnerBrandId(String.valueOf(brandId));
            Intent intent = new Intent(this, IsOwnerTractorModel.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("brand", brand);
            startActivity(intent);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        initList();
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
        if(item.getItemId() == R.id.menu_close) {
            showAlerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlerDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_quit))
                .setNegativeButton(getString(R.string.no),(dialog,which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes),(dialog,which) -> {
                    dialog.cancel();
                    addSomething();
                }).show();
    }
    private void addSomething() {
        if(!pref.getIsOwnerBrandId().isEmpty()) {
            pref.removeKey("ISOWNERBRANDID");
        }
        Intent intent = new Intent(IsOwnerTractorBrand.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!pref.getIsOwnerBrandId().isEmpty()) {
            pref.removeKey("ISOWNERBRANDID");
        }
    }
}
