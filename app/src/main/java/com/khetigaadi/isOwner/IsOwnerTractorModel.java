package com.khetigaadi.isOwner;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.model.ModelResp;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.activities.ModelSelectionActivity;
import com.khetigaadi.databinding.IsOwnerModelScreenBinding;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.room.repository.ModelRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@AndroidEntryPoint
public class IsOwnerTractorModel extends BaseActivity {
    private static final String TAG = IsOwnerTractorModel.class.getSimpleName();
    @Inject
    ModelRepository modelRepository;
    @Inject
    SharedPreferenceUtils pref;
    int model_id = 0;
    private Model selectedModel;
    private Brand brand;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Inject
    KhetiAPIs khetiAPIs;
    private IsOwnerModelScreenBinding binding;
    List<Model> brandParentsV = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = IsOwnerModelScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        setSupportActionBar(binding.toolbarI.toolbar);
        binding.toolbarI.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbarI.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.toolbarI.toolbar.setTitle(getString(R.string.owner));
        iconColorChange();
        binding.etSellTractorModel.requestFocus();
        binding.etSellTractorModel.setOnClickListener(v -> {
            Intent intent = new Intent(IsOwnerTractorModel.this,ModelSelectionActivity.class);
            intent.putExtra(ModelSelectionActivity.SELECTED_BRAND, brand);
            startActivityForResult(intent,ModelSelectionActivity.MODEL_SELECTION_REQ);
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == ModelSelectionActivity.MODEL_SELECTION_REQ) {
            if (resultCode == RESULT_OK) {
                selectedModel = data.getParcelableExtra(ModelSelectionActivity.SELECTED_MODEL);
                if (selectedModel != null) {
                    binding.etSellTractorModel.setText(selectedModel.getName());
                    model_id = selectedModel.getId();
                    searchModelData(model_id);
                }
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getIntent().getExtras() != null) {
            brand = getIntent().getExtras().getParcelable("brand");
        }
        initList();
    }
    private void iconColorChange() {
        binding.tabOwner.tvBrand.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tabOwner.tvModel.setTextColor(ContextCompat.getColor(this, R.color.red));
    }
    private void initList() {
        modelRepository.getNewTractorModels(brand.getId()).observe(this, listResource -> {
            if (listResource.size()>0) {
                try {
                    binding.llModel.removeAllViews();
                    List<Model> brandParents = listResource;
                    Log.d("xcvxvcxvxvxvxc====",""+brandParents.size()+"=="+brand.getId());
                    if (brandParents != null) {
                        for (int i = 0; i < brandParents.size(); i = i + 2) {
                            LinearLayout linearLayout = new LinearLayout(IsOwnerTractorModel.this);
                            linearLayout.setWeightSum(2);
                            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                            for (int j = 0; j <= 1 && i + j < brandParents.size(); j++) {
                                ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout, false);
                                final Model model = brandParents.get(i + j);
                                rowBinding. tvModelName.setText(model.getName());
                                if (!pref.getIsOwnerModelId().isEmpty()) {
                                    if (pref.getIsOwnerModelId().equalsIgnoreCase(String.valueOf(model.getId()))) {
                                      rowBinding.  ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
                                    }
                                }
                                rowBinding.getRoot().setOnClickListener(v -> {
                                    binding.etSellTractorModel.setText(model.getName());
                                    model_id = model.getId();
                                 rowBinding.ll.setBackground(ContextCompat.getDrawable(this, R.drawable.button_selector_red));
                                    pref.setIsOwnerModelId(String.valueOf(model.getId()));
                                    searchModelData(model_id);
                                });
                                linearLayout.addView(rowBinding.getRoot());
                            }
                            binding.llModel.addView(linearLayout);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                getModels();
            }
        });
    }
    public void getModels() {
        if (brandParentsV.size()>0)
            return;
        else
            brandParentsV.clear();
        //binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        Log.d("dgfgfgsad===",""+brand.getId()+"==");
     //   if(brand_idV==null || brand_idV.equalsIgnoreCase(null) || brand_idV.equalsIgnoreCase("null") || brand_idV.isEmpty())
           // brand_idV = String.valueOf(brand.getId());
        disposable.add(khetiAPIs.getNewTractorModels(Integer.parseInt(String.valueOf(brand.getId())))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ModelResp>() {
                    @Override
                    public void onSuccess(@NotNull ModelResp safetyTipsResp) {
                       // binding.llProgresss.llProgress.setVisibility(View.GONE);
                        try {
                            // binding.tvTips.setText(Html.fromHtml(safetyTipsResp.getData().getContent()));
                            Log.d("dgfgfgsad22===",""+brand.getId());
                            List<Model> listResource = safetyTipsResp.getData().getData();
                            Log.d("dgfgfgsad221234===",""+listResource.size());
                           // binding.llProgresss.llProgress.setVisibility(View.GONE);
                            binding.llModel.removeAllViews();
                            List<Model> brandParents = listResource;

                            Log.d("xcvxvcxvxvxvxc====",""+brandParents.size()+"=="+brand.getId());
                            if (brandParents != null) {
                                for (int i = 0; i < brandParents.size(); i = i + 2) {
                                    brandParentsV.add(brandParents.get(i));
                                    LinearLayout linearLayout = new LinearLayout(IsOwnerTractorModel.this);
                                    linearLayout.setWeightSum(2);
                                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                    for (int j = 0; j <= 1 && i + j < brandParents.size(); j++) {
                                        ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout, false);
                                        final Model model = brandParents.get(i + j);
                                        rowBinding. tvModelName.setText(model.getName());
                                        Log.d("sdfdsfsfdsf===",""+model.getBrandId()+"=="+model.getName());
                                        modelRepository.deleteModel(model);
                                        modelRepository.insertModel(model);
                                        if (!pref.getIsOwnerModelId().isEmpty()) {
                                            if (pref.getIsOwnerModelId().equalsIgnoreCase(String.valueOf(model.getBrandId()))) {
                                                rowBinding.  ll.setBackground(ContextCompat.getDrawable(IsOwnerTractorModel.this, R.drawable.button_selector_red));
                                            }
                                        }
                                        rowBinding.tvModelName.setOnClickListener(v -> {
                                            binding.etSellTractorModel.setText(model.getName());
                                            model_id = Integer.parseInt(model.getBrandId());
                                            Log.d("sdfdsfsfdsf22===",""+model.getBrandId()+"=="+model.getName());
                                            rowBinding.ll.setBackground(ContextCompat.getDrawable(IsOwnerTractorModel.this,R.drawable.button_selector_red));
                                            pref.setIsOwnerModelId(String.valueOf(model.getBrandId()));
                                            searchModelData(model_id);
                                            return;
                                        });
                                        linearLayout.addView(rowBinding.getRoot());
                                    }
                                    binding.llModel.addView(linearLayout);
                                }
                            }
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                      //  binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    private void searchModelData(int modelId) {
        Log.d("dfsdsfsfdsfdsf====",""+modelId);
        if (modelId != 0) {
            pref.setIsOwnerModelId(String.valueOf(modelId));
            Intent intent = new Intent(this, IsOwnerTractorYear.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
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
        if (item.getItemId() == R.id.menu_close) {
            showAlerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlerDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.are_you_sure_you_want_quit))
                .setNegativeButton(getString(R.string.no),(dialog, which) -> dialog.cancel())
                .setPositiveButton(getString(R.string.yes),(dialog, which) -> {
                    dialog.cancel();
                    addSomething();
                }).show();
    }
    private void addSomething() {
        if (!pref.getIsOwnerModelId().isEmpty()) {
            pref.removeKey("ISOWNERMODELID");
        }
        if (!pref.getIsOwnerBrandId().isEmpty()) {
            pref.removeKey("ISOWNERBRANDID");
        }
        Intent intent = new Intent(IsOwnerTractorModel.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!pref.getIsOwnerModelId().isEmpty()) {
            pref.removeKey("ISOWNERMODELID");
        }
    }
}
