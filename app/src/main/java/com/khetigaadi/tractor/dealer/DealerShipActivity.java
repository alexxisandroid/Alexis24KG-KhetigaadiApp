package com.khetigaadi.tractor.dealer;
//import android.app.Activity;
//import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.view.Window;
//import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
//import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khetigaadi.R;
import com.khetigaadi.agri.activity.BrandSelectionActivity;
//import com.khetigaadi.databinding.DealerNameBinding;
//import com.khetigaadi.databinding.DeleteTrFeedbackFormDialogBinding;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.base.model.MyResponse;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityDealerShipBinding;
import com.khetigaadi.def.BrandType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.user.model.User;
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
//import timber.log.Timber;

@AndroidEntryPoint
public class DealerShipActivity extends BaseActivity {
    private Brand selectedBrand;
    @Inject
    User user;
    private ActivityDealerShipBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    CompositeDisposable composite = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDealerShipBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(),(v,insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
//        EdgeToEdge.enable(this);
        findViewById();
        initToolbar();
        initUI();
    }
    @Override
    protected void onResume() {
        super.onResume();
        hideSoftKeyboard(DealerShipActivity.this);
    }
    @SuppressWarnings("deprecation")
    private void initUI() {
        binding.etBrand.setOnClickListener(v -> {
            if (binding.spTypeTrOrImp.getSelectedItemPosition() == 0) {
                Toast.makeText(DealerShipActivity.this, getString(R.string.please_select_brand_type), Toast.LENGTH_SHORT).show();
            } else if (binding.spTypeTrOrImp.getSelectedItemPosition() == 1) {
                Intent intent = new Intent(DealerShipActivity.this, BrandSelectionActivity.class);
                intent.putExtra(BrandSelectionActivity.BRAND_TYPE, BrandType.NEW_TRACTOR);
                //noinspection deprecation
                startActivityForResult(intent, BrandSelectionActivity.BRAND_SELECTION_REQ);
            } else if (binding.spTypeTrOrImp.getSelectedItemPosition() == 2) {
                Intent intent = new Intent(DealerShipActivity.this, BrandSelectionActivity.class);
                intent.putExtra(BrandSelectionActivity.BRAND_TYPE, BrandType.IMPLEMENT);
                startActivityForResult(intent, BrandSelectionActivity.BRAND_SELECTION_REQ);
            }
        });
        binding.riSubmit.setOnRippleCompleteListener(rippleView -> {
            if (validate()) {
                if (Constants.isConnectionAvailable(DealerShipActivity.this)) {
                    addDealershipEnq();
                } else {
                    noInternetConnection(binding.parent);
                }
            }
        });
    }
    private boolean validate() {
        if (selectedBrand == null) {
            Toast.makeText(this,getString(R.string.please_select_brand),Toast.LENGTH_SHORT).show();
        } else if (!binding.cbAgree.isChecked()) {
            Toast.makeText(this,getString(R.string.please_check_agree_button),Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }
    private void addDealershipEnq() {
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("brand_id", selectedBrand.getId());
        params.put("brand_type", binding.spTypeTrOrImp.getSelectedItem());
        composite.add(khetiAPIs.dealershipRequest(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MyResponse>() {
                    @Override
                    public void onSuccess(@NonNull MyResponse response) {
                        try {
                            if (!response.isError()) {
                                Toast.makeText(DealerShipActivity.this, response.getMsg().get(0), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(DealerShipActivity.this, response.getMsg().get(0), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            //noinspection CallToPrintStackTrace
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(DealerShipActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));
    }
    @SuppressWarnings("deprecation")
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.dealership));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.dealership));
    }
    private void findViewById() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.brand_type,R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        binding.spTypeTrOrImp.setAdapter(adapter);


    }
    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode,int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == BrandSelectionActivity.BRAND_SELECTION_REQ) {
            if(resultCode == RESULT_OK) {
                if(data != null) {
                    selectedBrand = data.getParcelableExtra(BrandSelectionActivity.SELECTED_BRAND);
                    if(selectedBrand != null)
                        binding.etBrand.setText(selectedBrand.getName());
                }
            }
        }
    }

//    private void showDialog(final String name) {
//        final Dialog d = new Dialog(this);
//        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        d.setCancelable(false);
//        d.setCanceledOnTouchOutside(false);
//        DealerNameBinding dialogBinding = DealerNameBinding.inflate(getLayoutInflater());
//        d.setContentView(dialogBinding.getRoot());
//        TextView textView = d.findViewById(R.id.tv_dealerName);
//        textView.setText(name);
//        d.show();
//    }

}
