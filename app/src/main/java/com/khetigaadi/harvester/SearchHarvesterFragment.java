package com.khetigaadi.harvester;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.khetigaadi.base.adapter.TwoBannerSlideAdapter;
import com.khetigaadi.base.model.Banner;
//import com.khetigaadi.base.model.Brand;

import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.room.repository.utils.Status;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.databinding.SearchHarvesterFragmentBinding;
import com.khetigaadi.databinding.TypeItemBinding;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.room.repository.TypeRepository;
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
public class SearchHarvesterFragment extends BaseFragment {
    private static final String TAG = "SearchHarvesterFragment";
    private SearchHarvesterFragmentBinding binding;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private List<Banner> bannerList;
    private Handler handler;
    private Runnable animateViewPager;
    private final boolean stopSliding = false;
    private boolean _areApiLoaded = false;
    @Inject
    BrandRepository brandRepository;
    @Inject
    TypeRepository typeRepository;
    @Inject
    SharedPreferenceUtils sharedPreferenceUtils;
    @Inject
    KhetiAPIs khetiAPIs;
    public SearchHarvesterFragment() {
    }
    public static SearchHarvesterFragment newInstance() {
        return new SearchHarvesterFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        handler = new Handler(Looper.getMainLooper());
    }
    @Override
    public void onStart() {
        super.onStart();
        initList();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !_areApiLoaded && isResumed()) {
            _areApiLoaded = true;
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = SearchHarvesterFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.harvester));
    }
    @Override
    public void onResume() {
        super.onResume();
        if (bannerList == null)
            bannerList = ((MainActivity) activity).allBanner.getBanners().getHarvester();
        if (bannerList != null) {
            if (bannerList.size() != 0) {
                binding.viewPager.setAdapter(new TwoBannerSlideAdapter(activity,bannerList));
                binding.viewPager.setClipToPadding(false);
                binding.viewPager.setPadding(0,0,120,0);
                binding.indicator.setViewPager(binding.viewPager);
                runnable(bannerList.size());
                handler.postDelayed(animateViewPager,Constants.ANIM_VIEWPAGER_DELAY);
            }
        }
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void initList() {
        brandRepository.getHarvesterBrands().observe(getViewLifecycleOwner(), brandParents -> {
          // Log.d("bvcbvbcvbcdfg===",""+brandParents.size());
            if (brandParents.size()>0) {
                String baseUrl = sharedPreferenceUtils.getStringValue(Constants.BRAND_BASE_URL, "");
                Log.d("bvcbvbcvbcdfg===",""+brandParents.size()+"=="+baseUrl);
                if(baseUrl.equalsIgnoreCase("") || baseUrl.isEmpty() || baseUrl.length()==0)
                    baseUrl = "https://khetigaadi.com/images/brandlogos/";
                Timber.e("onChanged: %s", brandParents.toString());
                try {
                    binding.llBrand.removeAllViews();
                    if (brandParents != null) {
                        for (int i = 0; i < brandParents.size(); i = i + 4) {
                            LinearLayout linearLayout = new LinearLayout(activity);
                            linearLayout.setWeightSum(4);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            for (int j = 0; j <= 3 && i + j < brandParents.size(); j++) {
                                BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                                final Brand brand = brandParents.get(i + j);
                                Glide.with(activity)
                                        //.load(brand.getImage() != null ? Constants.decodeURL(baseUrl + brand.getImage()) : null)
                                        .load(baseUrl+brand.getImage())
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into(rowBinding.image);
                                Log.d("bnvvnbvnhjfghbgh==",""+baseUrl+brand.getImage());
                                rowBinding.getRoot().setOnClickListener(v -> callListPage(brand, null));
                                linearLayout.addView(rowBinding.getRoot());
                            }
                            binding.llBrand.addView(linearLayout);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                getBrands();
            }
        });
        typeRepository.getHarvesterTypes().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource.size()>0) {
                binding.cvTypes.setVisibility(View.VISIBLE);
                String baseUrl = sharedPreferenceUtils.getStringValue(Constants.TYPES_BASE_URL, "");
                Timber.e("onChanged: %s", listResource.toString());
                try {
                    binding.llTypes.removeAllViews();
                    if (listResource != null)
                        for (int i = 0; i < listResource.size(); i = i + 3) {
                            LinearLayout linearLayout = new LinearLayout(activity);
                            linearLayout.setWeightSum(3);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            for (int j = 0; j <= 2 && i + j < listResource.size(); j++) {
                                @NonNull TypeItemBinding rowBinding = TypeItemBinding.inflate(getLayoutInflater(), linearLayout, false);
                                final Type type = listResource.get(i + j);
                                Glide.with(activity)
                                        .load(type.getImage() != null ? Constants.decodeURL(baseUrl + type.getImage()) : null)
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into(rowBinding.image);
                                rowBinding.tvName.setText(type.getName());
                                rowBinding.getRoot().setOnClickListener(v -> callListPage(null, type));
                                linearLayout.addView(rowBinding.getRoot());
                            }
                            binding.llTypes.addView(linearLayout);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
               // getBrands();
            }
        });
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
                                String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL,"");
                                baseUrl =myResponse.getOthers().getBrandLogoPath();
                                Timber.e("onChanged: %s",""+myResponse);
                                try {
                                    Log.d("cvbcvbcvbcfdgd===",""+myResponse.getBrand().size()+"=="+myResponse.isError());

                                    binding.llBrand.removeAllViews();

                                    if (!myResponse.isError()) {
                                        String lpath = myResponse.getOthers().getBrandLogoPath(); // "https://khetigaadi.com/images/brandlogos/";
                                        List<Brand> brands = new ArrayList<Brand>();

                                        for (int i = 0; i < myResponse.getBrand().size(); i++) {
                                            final Brand brandd = myResponse.getBrand().get(i);
                                            String brandType = String.valueOf(brandd.getIsHarvesterBrand());
                                            int brandStatus = brandd.getBrand_status();
                                            brandRepository.insertBrand(brandd);
                                            if(brandType.equalsIgnoreCase("1")) {
                                                brands.add(brandd);

                                            }
                                            Log.d("xcvbcxbcvbvb===",""+brandType);
                                        }
                                        Log.d("vcbcvbcv34324242====",""+brands.size());
                                        for (int i = 0; i < brands.size(); i = i + 3) {
                                            LinearLayout linearLayout = new LinearLayout(activity);
                                            linearLayout.setWeightSum(3);
                                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                            for (int j = 0; j <= 2 && i + j < brands.size(); j++) {
                                                @NonNull TypeItemBinding rowBinding = TypeItemBinding.inflate(getLayoutInflater(), linearLayout, false);
                                                final Brand type = brands.get(i + j);
                                                Glide.with(activity)
                                                        //.load(type.getImage() != null ? Constants.decodeURL(baseUrl + type.getImage()) : null)
                                                        .load(lpath+type.getImage())
                                                        .placeholder(R.mipmap.no_image)
                                                        .error(R.mipmap.no_image)
                                                        .into(rowBinding.image);
                                                Log.d("bvcxbcvbvnbb===",""+lpath+type.getImage());
                                                rowBinding.tvName.setText(type.getName());
                                               // rowBinding.getRoot().setOnClickListener(v -> callListPage(null, type));
                                                linearLayout.addView(rowBinding.getRoot());
                                            }
                                            binding.llTypes.addView(linearLayout);
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

                            }
                            else {
                                for(int i = 0; i < myResponse.getMsg().size(); i++) {
                                    Toast.makeText(activity, myResponse.getMsg().get(i), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        catch(Exception e) {
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
    private void callListPage(Brand brand,Type type) {
        Filter newTractorListParam = new Filter();
        if (brand != null)
            newTractorListParam.setBrands(Collections.singletonList(brand));
        if (type != null)
            newTractorListParam.setTypes(Collections.singletonList(type));
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
        fragmentTransaction.replace(contentView.getId(), HarvesterListFragment.getInstance(newTractorListParam)).addToBackStack(TAG).commit();
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
}
