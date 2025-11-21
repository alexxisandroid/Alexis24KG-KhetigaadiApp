package com.khetigaadi.tractor.newTractor.search;
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
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
import com.khetigaadi.base.adapter.TwoBannerSlideAdapter;
import com.khetigaadi.base.model.Banner;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.implement.newImplement.util.Brand;
//import com.khetigaadi.base.model.Model;

import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.commons.activities.HpSelectionActivity;
import com.khetigaadi.commons.activities.ModelSelectionActivity;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.databinding.NewTractorFilterBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import com.khetigaadi.tractor.newTractor.NewTractorsListFragment;
import com.khetigaadi.tractor.newTractor.model.Filter;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class SearchNewTractorFilter extends BaseFragment {
    private static final String TAG = SearchNewTractorFilter.class.getSimpleName();
    private int selectedDrive = 0;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private List<Banner> bannerList;
    private Handler handler;
    private Runnable animateViewPager;
    private boolean stopSliding = false;
    private boolean _areApiLoaded = false;
    @Inject
    BrandRepository brandRepository;
    private Model selectedModel;
    private NewTractorFilterBinding binding;
    private SearchNewTractorFilterViewModel viewModel;
    private boolean forBrand;
    @Inject
    KhetiAPIs khetiAPIs;
    List<Brand> brands = new ArrayList<Brand>();
    private String brand_idV;
    private String brand_nameV;
    public static Fragment getInstance(boolean forBrand) {
        SearchNewTractorFilter fragment = new SearchNewTractorFilter();
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
    /** @noinspection deprecation*/
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = NewTractorFilterBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
         if(activity instanceof MainActivity)
            ((MainActivity) activity).setToolbarTitle(getString(R.string.new_tractor));
        binding.btn2WD.setOnClickListener(v -> {
            binding.btn2WD.setTextColor(ContextCompat.getColor(activity, R.color.red));
            binding.btn4WD.setTextColor(ContextCompat.getColor(activity, R.color.navy_blue));
            selectedDrive = 1;//"2WD";
            callList();
        });
        binding.btn4WD.setOnClickListener(v -> {
            binding.btn4WD.setTextColor(ContextCompat.getColor(activity,R.color.red));
            binding.btn2WD.setTextColor(getResources().getColor(R.color.navy_blue));
            selectedDrive = 2;//"4WD";
            callList();
        });
        binding.etHp.requestFocus();
        binding.etHp.setOnClickListener(v -> startActivityForResult(new Intent(activity,HpSelectionActivity.class),HpSelectionActivity.HP_SELECTION_REQ));
        binding.etBrand.requestFocus();
        binding.etBrand.setOnClickListener(v -> {
            Intent intent = new Intent(activity, BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE, BrandType.NEW_TRACTOR);
            startActivityForResult(intent, BrandSelectionActivity.BRAND_SELECTION_REQ);
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchNewTractorFilterViewModel.class);
        viewModel.getSelectedBrand().observe(getViewLifecycleOwner(), brand -> binding.etBrand.setText(brand != null ? brand.getName() : ""));
        binding.edSearchTractorByName.requestFocus();
        binding.edSearchTractorByName.setOnClickListener(v -> {
            viewModel.setSelectedBrand(null);
            selectedModel = null;
            FragmentManager mFragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            mFragmentManager.popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
            fragmentTransaction.replace(contentView.getId(), new NewTractorSearchByName()).addToBackStack(TAG).commit();
        });
        binding.etModel.requestFocus();
        binding.etModel.setOnClickListener(view1 -> {
            if (viewModel.getSelectedBrand().getValue() != null) {
                Intent intent = new Intent(activity,ModelSelectionActivity.class);
                intent.putExtra(ModelSelectionActivity.SELECTED_BRAND,viewModel.getSelectedBrand().getValue());
                intent.putExtra("brand_idV",brand_idV);
                intent.putExtra("brand_nameV",brand_nameV);
                startActivityForResult(intent,ModelSelectionActivity.MODEL_SELECTION_REQ);
            } else {
                Toast.makeText(activity,getString(R.string.please_select_brand),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void callListPage(Brand selectedBrand,Model selectedModel) {
        if (viewModel.getSelectedBrand().getValue() != null) {
            if (this.selectedModel != null) {
                try {
                    binding.etModel.setText("");
                    Filter filter = new Filter();
                    List<Brand> bdg = new ArrayList<Brand>();
                     bdg.add(selectedBrand);
                    List<Model> mdll = new ArrayList<Model>();
                    mdll.add(selectedModel);
                    filter.setBrands(bdg);  //Collections.singletonList(selectedBrand));
                    filter.setModels(mdll); //Collections.singletonList(selectedModel));
                    viewModel.setSelectedBrand(null);
                    this.selectedModel = null;
                    ((MainActivity) activity).hideSoftKeyboard();
                    FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
                    FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                    fragmentTransaction.replace(contentView.getId(),NewTractorsListFragment.getInstance(filter)).addToBackStack(TAG).commit();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(activity, getString(R.string.please_select_model), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(activity, getString(R.string.please_select_brand), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        addFabButtonItems();
        if (bannerList == null)
            bannerList = ((MainActivity) activity).allBanner.getBanners().getNewTractor();
        if (bannerList != null) {
            if (bannerList.size() != 0) {
                binding.viewPager.setAdapter(new TwoBannerSlideAdapter(activity, bannerList));
                binding.viewPager.setClipToPadding(false);
                binding.viewPager.setPadding(60,0,60,0);
                binding.indicator.setViewPager(binding.viewPager);
                runnable(bannerList.size());
                handler.postDelayed(animateViewPager, Constants.ANIM_VIEWPAGER_DELAY);
            }
        }
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void initList() {
    // brandRepository.deleteAll();
        binding.tvSearchByBrand.setVisibility(View.GONE);
        Log.d("bcvvbfvdfdvbcvxb===",""+brandRepository.getCountt()+"=="+brandRepository.fetchAllBrandss().size());
        brandRepository.getNewTractorBrands().observe(getViewLifecycleOwner(),brandParents -> {
        Log.d("cvbcvbcvbzcxxz===",""+brandParents.size());
            if (brandParents.size()>0) {
                String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
               // if(baseUrl==null || baseUrl.isEmpty() || baseUrl.equalsIgnoreCase("") || baseUrl.equalsIgnoreCase("null"))
                baseUrl = "https://khetigaadi.com/images/brandlogos/";
                binding.tvSearchByBrand.setVisibility(View.VISIBLE);
                Timber.e("onChanged: %s",brandParents.toString());
                try {
                    binding.llBrand.removeAllViews();
                    if (!brandParents.isEmpty()) {
                        for (int i = 0; i < brandParents.size(); i = i + 4) {
                            LinearLayout linearLayout = new LinearLayout(activity);
                            linearLayout.setWeightSum(4);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            for (int j = 0; j <= 3 && i + j < brandParents.size(); j++) {
                                BrandRow1Binding rowBinding = BrandRow1Binding.inflate(getLayoutInflater(), linearLayout, false);
                                final Brand brand = brandParents.get(i + j);
                                Glide.with(activity)
                                        // .load(brand.getImage() != null ? Constants.decodeURL(baseUrl + brand.getImage()) : null)
                                        .load(baseUrl + brand.getImage())
                                        .placeholder(R.mipmap.no_image)
                                        .error(R.mipmap.no_image)
                                        .into(rowBinding.image);
                                rowBinding.getRoot().setOnClickListener(v -> callListPage(brand));
                                linearLayout.addView(rowBinding.getRoot());
                                Log.d("cvbcvbcvbzcxxzdfsg===", "" + baseUrl + brand.getImage());
                            }
                            binding.llBrand.addView(linearLayout);
                        }
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                if(forBrand) {
                    binding.container.post(() -> binding.container.fullScroll(ScrollView.FOCUS_DOWN));
                    forBrand = false;
                }
            }
            else {
                int cntt = brandRepository.getCountt();
                if(cntt<=0)
                  getBrands();
            }
        });
    }
    private void callListPage(Brand currentTractor) {
        Filter newTractorListParam = new Filter();
         // currentTractor.setBrand_id("2");
        //newTractorListParam.setBrands(Collections.singletonList(currentTractor));
        List<Brand> bdg = new ArrayList<Brand>();
        bdg.add(currentTractor);
        newTractorListParam.setBrands(bdg);
        Log.d("vbvcxbcvbcvcvbcbcv======",""+newTractorListParam.getBrands().toString()+"=="+currentTractor);
        //newTractorListParam.setModels(Collections.singletonList());
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
        fragmentTransaction.replace(contentView.getId(), NewTractorsListFragment.getInstance(newTractorListParam)).addToBackStack(TAG).commit();
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
    private void callList() {
        Filter jsonObjectDrive = new Filter();
        Log.d("vcvzczcvcvccvcbcv===",""+selectedDrive);
        if (selectedDrive != 0) {
            jsonObjectDrive.setDrives(Collections.singletonList(new Drive(selectedDrive)));
        }
        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
        FrameLayout contentView = (FrameLayout)activity.findViewById(R.id.containerView);
        fragmentTransaction.replace(contentView.getId(),NewTractorsListFragment.getInstance(jsonObjectDrive)).addToBackStack(TAG).commit();
    }
    /** @noinspection deprecation*/
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch(requestCode) {
            case HpSelectionActivity.HP_SELECTION_REQ: {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Filter newTractorListParam = new Filter();
                        newTractorListParam.setHps(Collections.singletonList(data.getParcelableExtra(HpSelectionActivity.SELECTED_HP)));
                        FragmentManager mFragmentManager = activity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
                        FrameLayout contentView = (FrameLayout) activity.findViewById(R.id.containerView);
                        fragmentTransaction.replace(contentView.getId(), NewTractorsListFragment.getInstance(newTractorListParam)).addToBackStack(TAG).commit();
                    }
                }
                break;
            }
            case BrandSelectionActivity.BRAND_SELECTION_REQ: {
                  if(resultCode == Activity.RESULT_OK) {
                      Brand fg = data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND);
                      Log.d("cvbcvbcvbcvbc===",""+(resultCode == Activity.RESULT_OK)+"=="+fg+"=="+fg.getName()+"=="+"=="+fg.getName());
                      String brand_name = data.getStringExtra("brand_name").toString();
                      String brand_id   = data.getStringExtra("brand_id");
                      fg.setName(brand_name);
                      fg.setId(Integer.parseInt(brand_id));
                     // fg.setId(Integer.parseInt(brand_id));
                      Log.d("cvbcvbcvbcvbc1===",""+(resultCode == Activity.RESULT_OK)+"=="+fg+"=="+fg.getName()+"=="+"=="+fg.getName()+"=="+fg.getId()+"=="+brand_id);
                      brand_idV = brand_id;
                      brand_nameV = brand_name;
                      viewModel.setSelectedBrand(fg);
                    if(viewModel.getSelectedBrand().getValue() != null) {
                        binding.etModel.setText("");
                        selectedModel = null;
                    Brand brandVal =  fg; // data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND);
                    if(brandVal!=null && brandVal.getName()!=null) {
                        binding.etBrand.setText(brandVal.getName());
                    }
                    }
                }
                break;
            }
            case ModelSelectionActivity.MODEL_SELECTION_REQ: {
                if (resultCode == Activity.RESULT_OK) {
                   try {
                       if (data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL) != null && data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL) instanceof Model)
                           selectedModel = data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL);
                       if (selectedModel != null) {
                           binding.etModel.setText(selectedModel.getName());
                           callListPage(viewModel.getSelectedBrand().getValue(), selectedModel);
                       }
                   }
                   catch(Exception e) {
                       e.printStackTrace();
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
        Log.d("cxvcxvcxvxv===",""+brands.size());
        if(brands.size()>0)
            return;
        brands.clear();
        khetiAPIs.getBrands()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull BrandResp myResponse)
                    {
                        // binding.llProgress.llProgress.setVisibility(GONE);
                        brands.clear();
                        try {
                             if(!myResponse.isError()) {
                                 brandRepository.deleteAll();
                                 binding.tvSearchByBrand.setVisibility(View.VISIBLE);
                                 String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL,"");

                                 Timber.e("onChanged: %s",""+myResponse);
                                try {
                                    Log.d("cvbcvbcvbcfdgd===",""+myResponse.getBrand().size()+"=="+myResponse.isError());
                                    binding.llBrand.removeAllViews();
                                    if (!myResponse.isError()) {
                                        String lpath = myResponse.getOthers().getBrandLogoPath(); // "https://khetigaadi.com/images/brandlogos/";
                                        SharedPreferenceUtils sharedPreferenceUtils = SharedPreferenceUtils.getInstance(activity);
                                        sharedPreferenceUtils.setValue(Constants.BRAND_BASE_URL,lpath);


                                        //  String baseUrl = SharedPreferenceUtils.getInstance(activity).getStringValue(Constants.BRAND_BASE_URL, "");
                                       // SharedPreferenceUtils.getInstance(activity).s
                                        for (int i = 0; i < myResponse.getBrand().size(); i++) {
                                            final Brand brandd = myResponse.getBrand().get(i);
                                           // myResponse.
                                            int brandType = brandd.getBrand_type();
                                            int brandStatus = brandd.getBrand_status();

                                           // insertBrand(brandd);
                                            if ((brandType == 0 || brandType == 1) && brandStatus == 1) {
                                                brands.add(brandd);
                                                try {
                                                    int cCount = brandRepository.getBrandCounts(brandd.getId());
                                                    if (cCount>0)
                                                        brandRepository.deleteBrand(brandd);
                                                    brandRepository.insertBrand(brandd);
                                                }
                                                catch(Exception e) {
                                                    Log.d("cvbcbcbcvbcvbcvbcv===",""+e.getLocalizedMessage());
                                                }
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
                                                  Brand brr = brand; // new Brand();
                                                //  brr = brand;
                                                 // brr.setBrand_id(brand.getBrand_id());
//                                                  brr.setBrand_name(brand.getBrand_name());
//                                                  brr.setBrand_status(brand.getBrand_status());
//                                                  brr.setBrand_type(brand.getBrand_type());
//                                                  brr.setCommodity(brand.getCommodity());
//                                                  brr.setDistrict(brand.getDistrict());
//                                                  brr.setId(brand.getId());



                                                //brands.get(i+j).mo;
                                                String manufacturerType = brand.getBrand_type() + "==" + brand.getBrand_status();
                                                Log.d("vbvcbcvbcvbvxvcx===", "" + manufacturerType + "==" + baseUrl + brand.getImage()+"=="+brr.getImage()+"=="+brand);
                                                // if(manufacturerType.contains("0") || manufacturerType.contains("1")) {
                                                Model model = new Model();
                                               // model.set
                                                Glide.with(activity)
                                                        //.load(brand.getImage() != null ? Constants.decodeURL(baseUrl + brand.getImage()) : null)
                                                        .load(lpath + brand.getImage())
                                                        .placeholder(R.mipmap.no_image)
                                                        .error(R.mipmap.no_image)
                                                        .into(rowBinding.image);
                                                if(selectedModel!=null) {
                                                    rowBinding.getRoot().setOnClickListener(v ->callListPage(brr,selectedModel));
                                                }
                                                else {
                                                    Log.d("hjghjhgdbvnvnvnbv===",""+brand+"=="+brr);
                                                    rowBinding.getRoot().setOnClickListener(v -> callListPage(brr));
                                                }
                                                    linearLayout.addView(rowBinding.getRoot());
                                            }
                                            binding.llBrand.addView(linearLayout);
                                        }
                                    }
                                }
                                catch(Exception e) {
                                    Log.d("cvbcvbcvbcbvbvcv===",""+e.getMessage());
                                   // e.printStackTrace();
                                }
                                if(forBrand) {
                                    binding.container.post(() -> binding.container.fullScroll(ScrollView.FOCUS_DOWN));
                                    forBrand = false;
                                }
                            }
                            else {
                                for(int i = 0; i < myResponse.getMsg().size(); i++) {
                                    Toast.makeText(activity, myResponse.getMsg().get(i),Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e)
                    {
                        Log.d("vcxvcxvcxvxvc3424234===",""+e.getLocalizedMessage());
                        Timber.e("response Error: %s", e);
                        // binding.llProgress.llProgress.setVisibility(GONE);
                    }
                });
    }
}
