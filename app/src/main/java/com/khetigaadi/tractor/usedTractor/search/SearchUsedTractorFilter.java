package com.khetigaadi.tractor.usedTractor.search;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
import com.khetigaadi.base.adapter.TwoBannerSlideAdapter;
import com.khetigaadi.base.model.Banner;
import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.ecom.EcomHomeActivity;
import com.khetigaadi.implement.newImplement.util.Brand;
//import com.khetigaadi.base.model.Model;

import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.base.model.Price;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.ModelSelectionActivity;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.databinding.SearchUsedTractorFilterBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.usedTractor.UsedTractorsListFragment;
import com.khetigaadi.util.NumberFormatting;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class SearchUsedTractorFilter extends BaseFragment {
    private static final String TAG = SearchUsedTractorFilter.class.getSimpleName();
    private Runnable animateViewPager;
    private final boolean stopSliding = false;
    private Brand selectedBrand;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private List<Banner> bannerList;
    private Handler handler;
    @Inject
    BrandRepository brandRepository;
    private Model selectedModel;
    private SearchUsedTractorFilterBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    private String brand_idV="0";
    private String brand_nameV = "";
    private SearchUsedTractorFilter viewModel;
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        selectedBrand = data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND);

                        String brand_name = data.getStringExtra("brand_name").toString();
                        String brand_id   = data.getStringExtra("brand_id");
                       // fg.setName(brand_name);
                       // fg.setId(Integer.parseInt(brand_id));
                        // fg.setId(Integer.parseInt(brand_id));
                       // Log.d("cvbcvbcvbcvbc1===",""+(resultCode == Activity.RESULT_OK)+"=="+fg+"=="+fg.getName()+"=="+"=="+fg.getName()+"=="+fg.getId()+"=="+brand_id);
                        brand_idV = brand_id;
                        brand_nameV = brand_name;

                        if (selectedBrand != null) {
                            binding.etBrand.setText(selectedBrand.getName());
                            binding.etModel.setText("");
                            selectedModel = null;
                        }
                    }
                }
            });
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        handler = new Handler(Looper.getMainLooper());
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = SearchUsedTractorFilterBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // viewModel = new ViewModelProvider(this).get(SearchUsedTractorFilterBinding.class);
       // viewModel.getSelectedBrand().observe(getViewLifecycleOwner(), brand -> binding.edSearchTractorByName.setText(brand != null ? "" : ""));
        if(activity instanceof  MainActivity)
            ((MainActivity) activity).setToolbarTitle(getString(R.string.old_tractor));
        else if(activity instanceof EcomHomeActivity)
            ((EcomHomeActivity)activity).setToolbarTitle(getString(R.string.old_tractor));
        else {
          //  ((AppCompatActivity) activity).setToolbarTitle(getString(R.string.old_tractor));
        }
        binding.edSearchTractorByName.requestFocus();
        binding.edSearchTractorByName.setOnClickListener(v -> {
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            mFragmentManager.popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentTransaction.replace(contentView.getId(), new UsedTractorSearchByName()).addToBackStack(TAG).commit();
        });
        binding.btnSearchUsedTractor.setOnClickListener(rippleView -> {
            Price price = new Price(NumberFormatting.getPriceFormIndex(Math.round(binding.rangeBarPrice.getValues().get(0))), NumberFormatting.getPriceFormIndex(Math.round(binding.rangeBarPrice.getValues().get(1))));
            getList(null, null, price);
        });
        binding.radio10To2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                binding.rangeBarPrice.setValues(0f, 11f);
        });
        binding.radio2To3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                binding.rangeBarPrice.setValues(11f, 13f);
        });
        binding.radio3To5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                binding.rangeBarPrice.setValues(13f, 17f);
        });
        binding.radioAbove5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                binding.rangeBarPrice.setValues(17f, 37f);
        });
        binding.rangeBarPrice.setValues(0f, 37f);
        binding.tvMinPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.getShortPriceFromIndex(Math.round(binding.rangeBarPrice.getValues().get(0))))+" ");
        binding.tvMaxPrice.setText(String.format("%s%s", " "+getString(R.string.rs), NumberFormatting.getShortPriceFromIndex(Math.round(binding.rangeBarPrice.getValues().get(1)))));
        binding.rangeBarPrice.addOnChangeListener((slider, value, fromUser) -> {
            binding.tvMinPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.getShortPriceFromIndex(Math.round(slider.getValues().get(0)))));
            binding.tvMaxPrice.setText(String.format("%s%s", getString(R.string.rs), NumberFormatting.getShortPriceFromIndex(Math.round(slider.getValues().get(1)))));
            selectPriceButton(Math.round(slider.getValues().get(0)), Math.round(slider.getValues().get(1)));
        });
        binding.rangeBarPrice.setLabelFormatter(value -> String.format("%s%s", getString(R.string.rs), NumberFormatting.getShortPriceFromIndex(Math.round(value))));
        binding.etBrand.requestFocus();
        binding.etBrand.setOnClickListener(v -> {
            Intent intent = new Intent(activity, BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE, BrandType.OLD_TRACTOR);
            filterActivityResultLauncher.launch(intent);
        });
        binding.etModel.requestFocus();
        binding.etModel.setOnClickListener(view1 -> {
            if (selectedBrand != null) {
                Intent intent = new Intent(activity,ModelSelectionActivity.class);
                intent.putExtra(ModelSelectionActivity.SELECTED_BRAND,selectedBrand);
                intent.putExtra(BrandSelectionActivity.BRAND_TYPE,BrandType.OLD_TRACTOR);
                intent.putExtra("brand_idV",brand_idV);
                intent.putExtra("brand_nameV",brand_nameV);

                startActivityForResult(intent,ModelSelectionActivity.MODEL_SELECTION_REQ);
            } else {
                Toast.makeText(activity,getString(R.string.please_select_brand),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void selectPriceButton(int leftPinIndex,int rightPinIndex) {
        if (leftPinIndex == 0 && rightPinIndex == 11) {
            binding.radio10To2.setChecked(true);
        } else if (leftPinIndex == 11 && rightPinIndex == 13) {
            binding.radio2To3.setChecked(true);
        } else if (leftPinIndex == 13 && rightPinIndex == 17) {
            binding.radio3To5.setChecked(true);
        } else if (leftPinIndex == 17 && rightPinIndex == 37)
            binding.radioAbove5.setChecked(true);
        else {
            binding.radio10To2.setChecked(false);
            binding.radio2To3.setChecked(false);
            binding.radio3To5.setChecked(false);
            binding.radioAbove5.setChecked(false);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)activity).reinstBNav();
        brandRepository.getOldTractorBrands().observe(getViewLifecycleOwner(),listResource -> {
            if (listResource.size()>0) {
                List<Brand> brands = listResource;
                String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
                baseUrl = "https://khetigaadi.com/images/brandlogos/";

                try {
                    binding.llBrand.removeAllViews();
                    if (brands != null) {
                        for (int i = 0; i < brands.size(); i = i + 4) {
                            LinearLayout linearLayout = new LinearLayout(activity);
                            linearLayout.setWeightSum(4);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            for (int j = 0; j <= 3 && i + j < brands.size(); j++) {
                                BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                                final Brand brand = brands.get(i + j);
                                Glide.with(activity)
                                        //.load(brand.getImage() != null ? Constants.decodeURL(baseUrl + brand.getImage()) : null)
                                        .load(baseUrl+brand.getImage())
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into(rowBinding.image);
                                rowBinding.getRoot().setOnClickListener(v -> getList(brand, null, null));
                                linearLayout.addView(rowBinding.getRoot());
                            }
                            binding.llBrand.addView(linearLayout);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
               // getBrands();
            }
        });
//        brandRepository.getOldTractorBrands().observe(this,new Observer<List<com.khetigaadi.implement.newImplement.util.Brand>>() {
//            @Override
//            public void onChanged(@Nullable List<com.khetigaadi.implement.newImplement.util.Brand> brands) {
//                try {
//                    assert brands != null;
//                    if(!brands.isEmpty()) {
//
//                       // List<Brand> brands = listResource.getData();
//                        String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
//                        try {
//                            binding.llBrand.removeAllViews();
//                            if (brands != null) {
//                                for (int i = 0; i < brands.size(); i = i + 4) {
//                                    LinearLayout linearLayout = new LinearLayout(activity);
//                                    linearLayout.setWeightSum(4);
//                                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                                    for (int j = 0; j <= 3 && i + j < brands.size(); j++) {
//                                        BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
//                                        final Brand brand = brands.get(i + j);
//                                        Glide.with(activity)
//                                                .load(brand.getImage() != null ? Constants.decodeURL(baseUrl + brand.getImage()) : null)
//                                                .placeholder(R.mipmap.no_image)
//                                                .error(R.mipmap.no_image)
//                                                .into(rowBinding.image);
//                                        rowBinding.getRoot().setOnClickListener(v -> getList(brand, null, null));
//                                        linearLayout.addView(rowBinding.getRoot());
//                                    }
//                                    binding.llBrand.addView(linearLayout);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//                catch(Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
    @Override
    public void onResume() {
        super.onResume();
        if (bannerList == null) {
            if (activity instanceof MainActivity)
               bannerList = ((MainActivity) activity).allBanner.getBanners().getOldTractor();
            else if(activity instanceof EcomHomeActivity)
                bannerList = ((EcomHomeActivity) activity).allBanner.getBanners().getOldTractor();
        }
        if (bannerList != null) {
            if (bannerList.size() != 0) {
                binding.viewPager.setAdapter(new TwoBannerSlideAdapter(activity, bannerList));
                binding.viewPager.setClipToPadding(false);
                binding.viewPager.setPadding(60, 0,60, 0);
                binding.indicator.setViewPager(binding.viewPager);
                runnable(bannerList.size());
                handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY);
            }
        }
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        addFabButtonItems();
    }
    private void getList(Brand brand,Model selectedModel,Price price) {
        try {
            Filter filter = new Filter();
            if(price != null)
                filter.setPrice(price);
            if(brand != null) {
                filter.setBrands(Collections.singletonList(brand));
                if(selectedModel != null) {
                    filter.setModels(Collections.singletonList(selectedModel));
                }
            }
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(),UsedTractorsListFragment.getInstance(filter)).addToBackStack(TAG).commit();
            binding.rangeBarPrice.invalidate();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void runnable(final int size) {
        animateViewPager = () -> {
            if (!stopSliding) {
                if (binding.viewPager.getCurrentItem() == size - 1) {
                    binding.viewPager.setCurrentItem(0);
                } else {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1,true);
                }
                handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY);
            }
        };
    }
    /** @noinspection deprecation*/
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case ModelSelectionActivity.MODEL_SELECTION_REQ: {
                if (resultCode == Activity.RESULT_OK) {
                    selectedModel = data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL);
                    if (selectedModel != null) {
                        binding.etModel.setText(selectedModel.getName());
                        getList(selectedBrand, selectedModel, null);
                    }
                }
                break;
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    void getBrands()
    {
        int cnfg = brandRepository.getCountt();
        if (cnfg>0)
            return;
        khetiAPIs.getBrands()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BrandResp myResponse) {
                        // binding.llProgress.llProgress.setVisibility(GONE);
                        try {
                            if (!myResponse.isError()) {
                                String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
                                Timber.e("onChanged: %s",""+myResponse);
                                try {
                                    Log.d("cvbcvbcvbcfdgd===",""+myResponse.getBrand().size()+"=="+myResponse.isError());

                                    binding.llBrand.removeAllViews();

                                    if (!myResponse.isError()) {
                                        brandRepository.deleteAll();
                                        String lpath = myResponse.getOthers().getBrandLogoPath(); // "https://khetigaadi.com/images/brandlogos/";
                                        List<Brand> brands = new ArrayList<Brand>();
                                        for (int i = 0; i < myResponse.getBrand().size(); i++) {
                                            final Brand brandd = myResponse.getBrand().get(i);
                                            int brandType = brandd.getBrand_type();
                                            int brandStatus = brandd.getBrand_status();


                                            if (brandType == 0 || brandType == 1) {
                                                brands.add(brandd);

                                                brandRepository.insertBrand(brandd);
                                            }
                                        }
                                        Log.d("vcbcvbcv34324242====",""+brands.size());
                                        for (int i = 0; i < brands.size(); i = i + 4) {
                                            LinearLayout linearLayout = new LinearLayout(activity);
                                            linearLayout.setWeightSum(4);
                                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                            Log.d("vbvcbcvbcvbvcvbcvbvbvbcvbc===", myResponse.getMsg().get(0));
                                            for (int j = 0; j <= 3 && i + j < brands.size(); j++) {
                                                Log.d("vbvcbcvbcvbvcxv11===", "" + myResponse);
                                                BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                                                final Brand brand = brands.get(i + j);
                                                String manufacturerType = brand.getBrand_type() + "==" + brand.getBrand_status();
                                                Log.d("vbvcbcvbcvbvxvcx===", "" + manufacturerType + "==" + baseUrl + brand.getImage());
                                                // if(manufacturerType.contains("0") || manufacturerType.contains("1")) {
                                                Glide.with(activity)
                                                        //.load(brand.getImage() != null ? Constants.decodeURL(baseUrl + brand.getImage()) : null)
                                                        .load(lpath + brand.getImage())
                                                        .placeholder(R.mipmap.no_image)
                                                        .error(R.mipmap.no_image)
                                                        .into(rowBinding.image);
                                             //  rowBinding.getRoot().setOnClickListener(v -> callListPage(brand));
                                                linearLayout.addView(rowBinding.getRoot());
                                                //    }
                                            }
                                            binding.llBrand.addView(linearLayout);
                                        }
                                    }
                                }
                                catch (Exception e) {
                                    Log.d("cvbcvbcvbcbvbvcv===",""+e.getMessage());
                                    // e.printStackTrace();
                                }
//                                if (forBrand) {
//                                    binding.container.post(() -> binding.container.fullScroll(ScrollView.FOCUS_DOWN));
//                                    forBrand = false;
//                                }
                            }
                            else {
                                for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                    Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e("response Error: %s", e);
                        // binding.llProgress.llProgress.setVisibility(GONE);
                    }
                });
    }
    //    private void callListPage(Brand selectedBrand,Model selectedModel) {
//        if (viewModel.getSelectedBrand().getValue() != null) {
//            if (this.selectedModel != null) {
//                try {
//                    binding.etModel.setText("");
//                    Filter filter = new Filter();
//                    filter.setBrands(Collections.singletonList(selectedBrand));
//                    filter.setModels(Collections.singletonList(selectedModel));
//                    viewModel.setSelectedBrand(null);
//                    this.selectedModel = null;
//                    ((MainActivity) activity).hideSoftKeyboard();
//                    FragmentManager mFragmentManager = activity.getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//                    fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
//                    FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
//                    fragmentTransaction.replace(contentView.getId(), NewTractorsListFragment.getInstance(filter)).addToBackStack(TAG).commit();
//                }
//                catch(Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            else {
//                Toast.makeText(activity, getString(R.string.please_select_model), Toast.LENGTH_SHORT).show();
//            }
//        }
//        else {
//            Toast.makeText(activity, getString(R.string.please_select_brand), Toast.LENGTH_SHORT).show();
//        }
//    }
}
