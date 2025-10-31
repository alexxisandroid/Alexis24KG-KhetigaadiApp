package com.khetigaadi.agri.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.agri.adapter.BrandSelectionAdapter;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.databinding.ActivityBrandSelectionBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.BrandRepository;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class BrandSelectionActivity extends BaseActivity {
    public static final String BRAND_TYPE = "type";
    public static final String SELECTED_BRAND = "selectedBrand";
    private BrandSelectionAdapter mAdapter;
    public static final int BRAND_SELECTION_REQ = 74;
    @Inject
    BrandRepository brandRepository;
    private ActivityBrandSelectionBinding binding;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    KhetiAPIs khetiAPIs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = ActivityBrandSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        findViewById();
        initToolbar();
        initView();
    }
    @Override
    public void onStart() {
        super.onStart();
        @BrandType int type = getIntent().getExtras().getInt(BRAND_TYPE, BrandType.OLD_TRACTOR);
        switch (type) {
            case BrandType.NEW_TRACTOR: {
                brandRepository.getNewTractorBrands().observe(this, this::setDataToAdapter);
                break;
            }
            case BrandType.OLD_TRACTOR: {
                brandRepository.getOldTractorBrands().observe(this, this::setDataToAdapter);
                break;
            }
            case BrandType.IMPLEMENT: {
                brandRepository.getImplementBrands().observe(this, this::setDataToAdapter);
                break;  //
            }
        }
    }
    private void setDataToAdapter(List<com.khetigaadi.implement.newImplement.util.Brand> brands) {
        if(!brands.isEmpty()) {
            binding.llProgresss.llProgress.setVisibility(View.GONE);
            mAdapter.setData(brands);
            binding.edSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s,int start,int before,int count) {
                    mAdapter.getFilter().filter(s.toString());
                }
                @Override
                public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        else {
           getBrands();
        }
      //  else
            //Toast.makeText(this,getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
    }
    private void setDataToAdapter2(List<Brand> listResource) {
        if(!listResource.isEmpty()) {
                binding.llProgresss.llProgress.setVisibility(View.GONE);
               // mAdapter.setData(listResource);
                binding.edSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s,int start,int before,int count) {
                        mAdapter.getFilter().filter(s.toString());
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
        }
      //  else
           // Toast.makeText(this,getResources().getString(R.string.please_try_after_some_time),Toast.LENGTH_SHORT).show();
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbarI.toolbar.setTitle(getString(R.string.brand));
    }
    private void findViewById() {
        Glide.with(BrandSelectionActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
    }
    private void initView() {
        mAdapter = new BrandSelectionAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(BrandSelectionActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(brand -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_BRAND,brand);
            returnIntent.putExtra("brand_name",brand.getName());
            returnIntent.putExtra("brand_id",""+brand.getId());
            Log.d("sfdfsfdsfdsf===",":"+brand.getId()+"=="+brand.getName()+"=="+brand.getName()+"=="+brand);
            setResult(RESULT_OK,returnIntent);
            finish();
        });
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) binding.mRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(0, 0, 0, 0);
        binding.mRecyclerView.setLayoutParams(marginLayoutParams);
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard(BrandSelectionActivity.this);
    }
    public void getBrands() {
        int cnfg = brandRepository.getCountt();
        if (cnfg>0)
            return;
        ProgressBar pbar = binding.llProgresss.llProgress;
        pbar.setVisibility(View.VISIBLE);
        disposable.add(khetiAPIs.getBrands()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<BrandResp>() {
                    @Override
                    public void onSuccess(@NotNull BrandResp brandResp) {
                        pbar.setVisibility(View.GONE);
                        try {
                          //  binding.tvTips.setText(Html.fromHtml(safetyTipsResp.getData().getContent()));
                            List<com.khetigaadi.implement.newImplement.util.Brand> brands = brandResp.getBrand();
                            Log.d("dfghdfhdhdghdghd=====",""+brands.size()+"==");
                            binding.llProgresss.llProgress.setVisibility(View.GONE);
                            final List<com.khetigaadi.implement.newImplement.util.Brand> filteredBrands = new ArrayList<>();
                            @BrandType int type = getIntent().getExtras().getInt(BRAND_TYPE,BrandType.OLD_TRACTOR);
                            switch(type) {
                                case BrandType.NEW_TRACTOR: {
                                  //  brandRepository.getNewTractorBrands().observe(this, this::setDataToAdapter);
                                    for(int i=0;i<brands.size();i++) {
                                        int brandStatus = brands.get(i).getBrand_status();
                                        int brandType = brands.get(i).getBrand_type();
                                        if((brandType == 0 || brandType == 1) && brandStatus == 1) {
                                            brands.get(i).setName(brands.get(i).getName());
                                            filteredBrands.add(brands.get(i));
                                           Log.d("vcxbvcbcv===",""+brands.get(i).getName()+"=="+filteredBrands.get(filteredBrands.size()-1).getName());
                                        }
                                    }
                                    break;
                                }
                                case BrandType.OLD_TRACTOR: {
                                  //  brandRepository.getOldTractorBrands().observe(this, this::setDataToAdapter);
                                    for(int i=0;i<brands.size();i++) {
                                        int brandStatus = brands.get(i).getBrand_status();
                                        int brandType = brands.get(i).getBrand_type();
                                        if(brandType == 0 || brandType == 1) {
                                            brands.get(i).setName(brands.get(i).getName());
                                            filteredBrands.add(brands.get(i));
                                        }
                                    }
                                    break;
                                }
                                case BrandType.IMPLEMENT: {
                                    //brandRepository.getImplementBrands().observe(this, this::setDataToAdapter);
                                    for(int i=0;i<brands.size();i++) {
                                        int brandStatus = brands.get(i).getBrand_status();
                                        int brandType = brands.get(i).getBrand_type();
                                        if((brandType == 0 || brandType == 2) && brandStatus==1) {
                                            brands.get(i).setName(brands.get(i).getName());
                                            filteredBrands.add(brands.get(i));
                                        }
                                    }
                                    break;  //
                                }
//                                default:
//                                    for(int i=0;i<brands.size();i++) {
//                                        int brandStatus = brands.get(i).getBrand_status();
//                                        int brandType = brands.get(i).getBrand_type();
//                                        filteredBrands.add(brands.get(i));
//                                        Log.d("vcxbvcbcv===",""+brands.get(i).getBrand_name()+"=="+filteredBrands.get(i).getBrand_name());
//                                    }
//                                    break;
                            }
                            mAdapter.setData(filteredBrands);
                            binding.edSearch.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void onTextChanged(CharSequence s,int start,int before,int count) {
                                    mAdapter.getFilter().filter(s.toString());
                                }
                                @Override
                                public void beforeTextChanged(CharSequence s,int start,int count,int after) {

                                }
                                @Override
                                public void afterTextChanged(Editable s) {
                                }
                            });
                            brandRepository.insertAll(brands);
                        }
                        catch(Exception e) {
                            pbar.setVisibility(View.GONE);
                            Log.d("xcvcxvcxvxcvx===",""+e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        pbar.setVisibility(View.GONE);
                    }
                }));
    }
}
