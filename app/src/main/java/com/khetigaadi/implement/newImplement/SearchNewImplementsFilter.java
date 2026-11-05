package com.khetigaadi.implement.newImplement;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
import com.khetigaadi.base.adapter.TwoBannerSlideAdapter;
import com.khetigaadi.base.model.Banner;
import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.HpSelectionActivity;
import com.khetigaadi.commons.activities.TypeSelectionActivity;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.databinding.NewImplementsFilterBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.tractor.newTractor.model.Filter;
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
public class SearchNewImplementsFilter extends BaseFragment {
    private FragmentActivity activity;
    private final String TAG = SearchNewImplementsFilter.class.getSimpleName();
    @Inject
    Tracker mTracker;
    private List<Banner> bannerList;
    private Handler handler;
    private Runnable animateViewPager;
    private boolean stopSliding = false;
    private boolean _areApiLoaded = false;
    private boolean forBrand;
    @Inject
    BrandRepository brandRepository;
    private Type selectedType;
    private NewImplementsFilterBinding binding;
    private SearchNewImplementsFilterViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    public static Fragment getInstance(boolean forBrand) {
        SearchNewImplementsFilter fragment = new SearchNewImplementsFilter();
        Bundle bundle = new Bundle();
        bundle.putBoolean("forBrand", forBrand);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        handler = new Handler(Looper.getMainLooper());
        if (getArguments() != null)
            forBrand = getArguments().getBoolean("forBrand", false);
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = NewImplementsFilterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchNewImplementsFilterViewModel.class);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.implement));
        viewModel.getSelectedBrand().observe(getViewLifecycleOwner(), brand -> binding.etBrand.setText(brand != null ? brand.getName() : ""));
        binding.edSearchImpByName.setOnClickListener(v -> {
            try {
                FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                fragmentTransaction.replace(contentView.getId(), new NewImpSearchByName()).addToBackStack(TAG).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.etBrand.requestFocus();
        binding.etBrand.setOnClickListener(v -> {
            Intent intent = new Intent(activity, BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE, BrandType.IMPLEMENT);
            startActivityForResult(intent, BrandSelectionActivity.BRAND_SELECTION_REQ);
        });
        binding.etType.requestFocus();
        binding.etType.setOnClickListener(v -> {
            if (viewModel.getSelectedBrand().getValue() != null) {
                Intent intent = new Intent(activity, TypeSelectionActivity.class);
                intent.putExtra(TypeSelectionActivity.SELECTED_BRAND, viewModel.getSelectedBrand().getValue());
                startActivityForResult(intent, TypeSelectionActivity.TYPE_SELECTION_REQ);
            } else {
                Toast.makeText(activity, getString(R.string.please_select_brand), Toast.LENGTH_SHORT).show();
            }
        });
        binding.etHp.requestFocus();
        binding.etHp.setOnClickListener(v -> startActivityForResult(new Intent(getActivity(), HpSelectionActivity.class), HpSelectionActivity.HP_SELECTION_REQ));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areApiLoaded && isResumed()) {
            _areApiLoaded = true;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        brandRepository.getImplementBrands().observe(getViewLifecycleOwner(),listResource -> {
            if (listResource.size()>0) {
                List<Brand> brands = listResource;
                if (brands != null) {
                    String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
                    try {
                        binding.llBrand.removeAllViews();
                        for (int i = 0; i < brands.size(); i = i + 3) {
                            LinearLayout linearLayout = new LinearLayout(activity);
                            linearLayout.setWeightSum(3);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            for (int j = 0; j <= 2 && i + j < brands.size(); j++) {
                                BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                                rowBinding.getRoot().setBackgroundColor(Color.WHITE);
                                Glide.with(activity)
                                        .load(brands.get(i + j).getImage() != null ? Constants.decodeURL(baseUrl + brands.get(i + j).getImage()) : null)
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into( rowBinding.image);
                                final int finalJ = j;
                                final int finalI = i;
                                rowBinding.getRoot().setOnClickListener(v ->
                                        callList(brands.get(finalI + finalJ),null,null));
                                linearLayout.addView(rowBinding.getRoot());
                            }
                            binding.llBrand.addView(linearLayout);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (forBrand) {
                        binding.container.post(() -> binding.container.fullScroll(ScrollView.FOCUS_DOWN));
                        forBrand = false;
                    }
                }
            }
            else{
              //  getBrands();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        addFabButtonItems();
        if(bannerList == null)
            bannerList = ((MainActivity) activity).allBanner.getBanners().getNewImplement();
        if(bannerList != null) {
            if(bannerList.size() != 0) {
                binding.viewPager.setAdapter(new TwoBannerSlideAdapter(activity, bannerList));
                binding.viewPager.setClipToPadding(false);
                binding.viewPager.setPadding(60,0,60,0);
                binding.indicator.setViewPager(binding.viewPager);
                runnable(bannerList.size());
                handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY);
            }
        }
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void runnable(final int size) {
        animateViewPager = () -> {
            if (!stopSliding) {
                if (binding.viewPager.getCurrentItem() == size - 1) {
                    binding.viewPager.setCurrentItem(0);
                } else {
                    binding.viewPager.setCurrentItem(
                            binding.viewPager.getCurrentItem() + 1, true);
                }
                handler.postDelayed(animateViewPager, Constants.ANIM_VIEWPAGER_DELAY);
            }
        };
    }
    private void callList(Brand brand,Type type,Hp hp) {
        try {
            Filter filter = new Filter();
            if (brand != null) {
                filter.setBrands(Collections.singletonList(brand));
            }
            if (type != null) {
                filter.setTypes(Collections.singletonList(type));
            }
            if (hp != null) {
                filter.setHps(Collections.singletonList(hp));
            }
            viewModel.setSelectedBrand(null);
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
            FrameLayout contentView = activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(),NewImplementsList.getInstance(filter)).addToBackStack(TAG).commit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode) {
            case HpSelectionActivity.HP_SELECTION_REQ: {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Hp selectedHP = data.getParcelableExtra(HpSelectionActivity.SELECTED_HP);
                        callList(null,null,selectedHP);
                    }
                }
                break;
            }
            case BrandSelectionActivity.BRAND_SELECTION_REQ: {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.setSelectedBrand(data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND));
                    if (viewModel.getSelectedBrand().getValue() != null) {
                        binding.etType.setText("");
                        selectedType = null;
                    }
                }
                break;
            }
            case TypeSelectionActivity.TYPE_SELECTION_REQ: {
                if (resultCode == Activity.RESULT_OK) {
                    selectedType = (data.getParcelableExtra(TypeSelectionActivity.SELECTED_TYPE));
                    if (selectedType != null) {
                        binding.etType.setText(selectedType.getName());
                        binding.etType.setText("");
                        callList(viewModel.getSelectedBrand().getValue(), selectedType, null);
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
        try {
            int cnfg = brandRepository.getCountt();
            if (cnfg > 0)
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
                                    Timber.e("onChanged: %s", "" + myResponse);
                                    try {
                                        Log.d("cvbcvbcvbcfdgd===", "" + myResponse.getBrand().size() + "==" + myResponse.isError());
                                        binding.llBrand.removeAllViews();
                                        if (!myResponse.isError()) {
                                            String lpath = myResponse.getOthers().getBrandLogoPath(); // "https://khetigaadi.com/images/brandlogos/";
                                            List<Brand> brands = new ArrayList<Brand>();
                                            for (int i = 0; i < myResponse.getBrand().size(); i++) {
                                                final Brand brandd = myResponse.getBrand().get(i);
                                                int brandType = brandd.getBrand_type();
                                                int brandStatus = brandd.getBrand_status();

                                                brandRepository.deleteBrand(brandd);
                                                brandRepository.insertBrand(brandd);
                                                if ((brandType == 0 || brandType == 2) && brandStatus == 1) {
                                                    brands.add(brandd);
                                                }
                                            }
                                            Log.d("vcbcvbcv34324242====", "" + brands.size());
                                            for (int i = 0; i < brands.size(); i = i + 4) {
                                                LinearLayout linearLayout = new LinearLayout(activity);
                                                linearLayout.setWeightSum(4);
                                                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                                Log.d("vbvcbcvbcvbvcvbcvbvbvbcvbc===", myResponse.getMsg().get(0));
                                                for (int j = 0; j <= 3 && i + j < brands.size(); j++) {
                                                    Log.d("vbvcbcvbcvbvcxv11===", "" + myResponse);
                                                    BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                                                    rowBinding.getRoot().setBackgroundColor(Color.WHITE);
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
                                                    // rowBinding.getRoot().setOnClickListener(v -> callListPage(brand));
                                                    linearLayout.addView(rowBinding.getRoot());

                                                    //    }

                                                }
                                                binding.llBrand.addView(linearLayout);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Log.d("cvbcvbcvbcbvbvcv===", "" + e.getMessage());
                                        // e.printStackTrace();
                                    }
//                                if (forBrand) {
//                                    binding.container.post(() -> binding.container.fullScroll(ScrollView.FOCUS_DOWN));
//                                    forBrand = false;
//                                }
                                } else {
                                    for (int i = 0; i < myResponse.getMsg().size(); i++) {
                                        Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception e) {
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
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
