package com.khetigaadi.tractor.sellTractorNewUi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
//import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivitySellTractorNewScreensBinding;
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.tractor.rent.rentTractorNewUi.RentTractorBrand;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class SellTractorBrand extends BaseFragment {
    private static final String TAG = SellTractorBrand.class.getSimpleName();
    @Inject
    BrandRepository brandRepository;
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private ActivitySellTractorNewScreensBinding binding;
    private FragmentActivity activity;
    ActivityResultLauncher<Intent> brandSelectResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Brand selectedBrand = data != null ? data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND) : null;
                    if (selectedBrand != null) {
                        binding.etSellTractorBrand.setText(selectedBrand.getName());
                        searchBrandByModel(selectedBrand);
                    }
                }
            });
    public static SellTractorBrand getInstance() {
        return new SellTractorBrand();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = ActivitySellTractorNewScreensBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider((SellTractorActivity)activity).get(SellViewModel.class);
        binding.etSellTractorBrand.setOnClickListener(v -> {
            Intent intent = new Intent(activity,BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE,BrandType.OLD_TRACTOR);
            brandSelectResultLauncher.launch(intent);
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(1);
    }
    private void initList() {
//        brandRepository.getOldTractorBrands().observe(this, listResource -> {
//            if (listResource.getStatus() == Status.SUCCESS) {
//                List<Brand> brandParents = listResource.getData();
//                String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
//                try {
//                    binding.llBrand.removeAllViews();
//                    if (brandParents != null)
//                        for (int i = 0; i < brandParents.size(); i = i + 4) {
//                            LinearLayout linearLayout = new LinearLayout(activity);
//                            linearLayout.setWeightSum(4);
//                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                            for (int j = 0; j <= 3 && i + j < brandParents.size(); j++) {
//                                BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
//                                final Brand brand = brandParents.get(i + j);
//                                Glide.with(SellTractorBrand.this)
//                                        .load(brand.getImage() != null ? baseUrl + brand.getImage() : "")
//                                        .placeholder(R.mipmap.no_image)
//                                        .error(R.mipmap.no_image)
//                                        .into(rowBinding.image);
//                                if (viewModel.getUsedTractor().getBrand() != null) {
//                                    if (viewModel.getUsedTractor().getBrand().equals(brand)) {
//                                        rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
//                                    }
//                                }
//                                rowBinding.getRoot().setOnClickListener(v -> {
//                                    binding.etSellTractorBrand.setText(brand.getName());
//                                    rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
//                                    viewModel.getUsedTractor().setBrand(brand);
//                                    initList();
//                                    searchBrandByModel(brand);
//                                });
//                                linearLayout.addView(rowBinding.getRoot());
//                            }
//                            binding.llBrand.addView(linearLayout);
//                        }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        brandRepository.getOldTractorBrandsSell().observe(this,new Observer<List<com.khetigaadi.implement.newImplement.util.Brand>>() {
            @Override
            public void onChanged(@Nullable List<com.khetigaadi.implement.newImplement.util.Brand> brands) {
                try {
                    assert brands != null;
                    if(!brands.isEmpty()) {

                 List<Brand> brandParents = brands;
                String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
                try {
                    binding.llBrand.removeAllViews();
                    if (brandParents != null)
                        for (int i = 0; i < brandParents.size(); i = i + 3) {
                            LinearLayout linearLayout = new LinearLayout(activity);
                            linearLayout.setWeightSum(3);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            for (int j = 0; j <= 2 && i + j < brandParents.size(); j++) {
                                BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                                rowBinding.getRoot().setBackgroundColor(Color.WHITE);
                                final Brand brand = brandParents.get(i + j);
                                Glide.with(SellTractorBrand.this)
                                        .load(brand.getImage() != null ? baseUrl + brand.getImage() : "")
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into(rowBinding.image);
                                if (viewModel.getUsedTractor().getBrand() != null) {
                                    if (viewModel.getUsedTractor().getBrand().equals(brand)) {
                                        rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                                    }
                                }
                                rowBinding.getRoot().setOnClickListener(v -> {
                                    binding.etSellTractorBrand.setText(brand.getName());
                                    rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                                    viewModel.getUsedTractor().setBrand(brand);
                                    initList();
                                    searchBrandByModel(brand);
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
    private void searchBrandByModel(Brand brand) {
        if(brand != null) {
            viewModel.getUsedTractor().setBrand(brand);
            ((SellTractorActivity) activity).callFragments(2);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
}
