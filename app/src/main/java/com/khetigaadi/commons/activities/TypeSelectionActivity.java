package com.khetigaadi.commons.activities;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.adapter.TypeSelectionAdapter;
import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.commons.model.TypeResp;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityTypeSelectionBinding;
import com.khetigaadi.databinding.BrandRow1Binding;
import com.khetigaadi.implement.newImplement.util.Type;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.TypeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class TypeSelectionActivity extends BaseActivity {
    public static final String SELECTED_TYPE = "selectedType";
    public static final String SELECTED_BRAND = "selectedBrand";
    private TypeSelectionAdapter mAdapter;
    public static final int TYPE_SELECTION_REQ = 49;
    @Inject
    TypeRepository typeRepository;
    private Brand brand;
    private ActivityTypeSelectionBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityTypeSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
        try {
          ////  EdgeToEdge.enable(this);
            brand = getIntent().getExtras().getParcelable(SELECTED_BRAND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById();
        initToolbar();
        initView();
    }
    @Override
    public void onStart() {
        super.onStart();
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        if (brand != null) {
            typeRepository.getTypeByBrandId(brand.getId()).observe(this,types -> {
                if (types.size()>0 && types != null) {
                    Timber.e("onChanged: %s",types.toString());
                    setDataToAdapter(types);
                }
                else
                    getType();
            });
        }
        else {
            typeRepository.getTypes().observe(this,listResource -> {
                if (listResource.size()>0 && listResource != null) {
                    setDataToAdapter(listResource);
                }
                else
                    getType();
            });
        }
    }
    private void setDataToAdapter(@NonNull List<Type> types) {
        binding.llProgresss.llProgress.setVisibility(View.GONE);
        mAdapter.setData(types);
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.type));
        getSupportActionBar().setTitle(getString(R.string.type));
    }
    private void findViewById() {
        Glide.with(TypeSelectionActivity.this).load(R.drawable.loader_white).into(binding.llProgresss.ivLoader);
    }
    private void initView() {
        mAdapter = new TypeSelectionAdapter();
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(brand -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_TYPE, brand);
            setResult(RESULT_OK, returnIntent);
            finish();
        });
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) binding.mRecyclerView.getLayoutParams();
        marginLayoutParams.setMargins(0, 0, 0, 0);
        binding.mRecyclerView.setLayoutParams(marginLayoutParams);
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard(TypeSelectionActivity.this);
    }
    void getType()
    {
        int cnfg = typeRepository.getCountt();
        if (cnfg>0)
            return;
        khetiAPIs.getTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull TypeResp myResponse) {
                        // binding.llProgress.llProgress.setVisibility(GONE);
                        try {
                            if (!myResponse.isError()) {
                                String baseUrl = SharedPreferenceUtils.getInstance(TypeSelectionActivity.this).getStringValue(Constants.BRAND_BASE_URL,"");
                                Timber.e("onChanged: %s",""+myResponse);
                                try {
                                    Log.d("cvbcvbcvbcfdgd===","=="+myResponse.isError());
                                    binding.llProgresss.llProgress.setVisibility(View.GONE);
                                    List<Type> typess = myResponse.getTypes();
                                    mAdapter.setData(typess);
                                    typeRepository.deleteAll();
                                    binding.edSearch.addTextChangedListener(new TextWatcher() {
                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            mAdapter.getFilter().filter(s.toString());
                                        }
                                        @Override
                                        public void beforeTextChanged(CharSequence s,int start,int count,int after) {
                                        }
                                        @Override
                                        public void afterTextChanged(Editable s) {
                                        }
                                    });
                                    typeRepository.insertAll(typess);
                                }
                                catch(Exception e) {
                                    Log.d("cvbcvbcvbcbvbvcv===",""+e.getMessage());
                                    // e.printStackTrace();
                                }
                            }
                            else {
                                for(int i = 0; i < myResponse.getMsg().size(); i++) {
                                    Toast.makeText(TypeSelectionActivity.this,myResponse.getMsg().get(i),Toast.LENGTH_LONG).show();
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
}
