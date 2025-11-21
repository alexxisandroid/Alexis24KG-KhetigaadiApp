package com.khetigaadi.commons.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
import com.khetigaadi.base.model.AllBannerResp;
//import com.khetigaadi.base.model.Brand;
import com.khetigaadi.implement.newImplement.util.Brand;
//import com.khetigaadi.base.model.Model;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityOnRoadPriceBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.retrofit.KhetiAPIs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
@AndroidEntryPoint
public class OnRoadPriceActivity extends BaseActivity {
    private Brand selectedBrand;
    private Model selectedModel;
    private ActivityOnRoadPriceBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    CompositeDisposable composite = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityOnRoadPriceBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        initToolbar();
        binding.etBrand.setOnClickListener(v -> {
            Intent intent = new Intent(OnRoadPriceActivity.this, BrandSelectionActivity.class);
            intent.putExtra(BrandSelectionActivity.BRAND_TYPE, BrandType.OLD_TRACTOR);
            startActivityForResult(intent, BrandSelectionActivity.BRAND_SELECTION_REQ);
        });
        binding.etModel.setOnClickListener(v -> {
            if (selectedBrand != null) {
                Intent intent = new Intent(OnRoadPriceActivity.this, ModelSelectionActivity.class);
                intent.putExtra(ModelSelectionActivity.SELECTED_BRAND, selectedBrand);
                startActivityForResult(intent, ModelSelectionActivity.MODEL_SELECTION_REQ);
            } else {
                Toast.makeText(OnRoadPriceActivity.this, getString(R.string.please_select_brand), Toast.LENGTH_SHORT).show();
            }
        });
        binding.riRequest.setOnRippleCompleteListener(rippleView -> {
            if (validation()) {
                if (Constants.isConnectionAvailable(OnRoadPriceActivity.this)) {
                    sendRequest();
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AllBannerResp allBanner = new Gson().fromJson(pref.getStringValue(Constants.APP_BANNER_KEY,"{}"),AllBannerResp.class);
        if (allBanner.getBanners().getOnRoadPrice() != null) {
            Glide.with(this).load(allBanner.getBanners().getOnRoadPrice().get(0).getImageUrl()).into(binding.banner);
        }
    }
    private boolean validation() {
        if (selectedBrand == null) {
            Snackbar.make(binding.llContainer, getString(R.string.please_select_brand), Snackbar.LENGTH_LONG).show();
        } else if (selectedModel == null) {
            Snackbar.make(binding.llContainer, getString(R.string.please_select_model), Snackbar.LENGTH_LONG).show();
        } else if (!binding.cbAgree.isChecked()) {
            Snackbar.make(binding.llContainer, getResources().getString(R.string.please_check_agree_button), Snackbar.LENGTH_LONG).show();
        } else {
            return true;
        }
        return false;
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.getOnRoadPrice));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.getOnRoadPrice));
    }
    private void sendRequest() {
        hideSoftKeyboard(OnRoadPriceActivity.this);
        binding.llProgress.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<>();
        params.put("brand_id", selectedBrand.getId() + "");
        params.put("tractor_model_id", selectedModel.getId() + "");
        params.put("user_id", session.getUser().getId());
        composite.add(khetiAPIs.getNewTrPriceByBrandModel(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse myResponse) {
                        Toast.makeText(OnRoadPriceActivity.this,myResponse.getMsg().get(0),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(OnRoadPriceActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        binding.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case BrandSelectionActivity.BRAND_SELECTION_REQ: {
                if (resultCode == RESULT_OK) {
                    selectedBrand = data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND);
                    if (selectedBrand != null) {
                        binding.etBrand.setText(selectedBrand.getName());
                        binding.etModel.setText("");
                        selectedModel = null;
                    }
                }
                break;
            }
            case ModelSelectionActivity.MODEL_SELECTION_REQ: {
                if (resultCode == RESULT_OK) {
                    selectedModel = data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL);
                    if (selectedModel != null) {
                        binding.etModel.setText(selectedModel.getName());
                    }
                }
                break;
            }
        }
    }
}
