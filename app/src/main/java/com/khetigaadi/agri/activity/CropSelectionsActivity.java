package com.khetigaadi.agri.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.agri.adapter.CropSelectionsAdapter;
import com.khetigaadi.base.model.agri.Crop;
import com.khetigaadi.base.model.agri.Crops;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.ActivityCropSelectionsBinding;
import com.khetigaadi.user.retrofit.UserAPIs;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
@AndroidEntryPoint
public class CropSelectionsActivity extends BaseActivity {
    public static final String SELECTED_CROPS = "selectedCrops";
    private CropSelectionsAdapter mAdapter;
    private List<Crop> selectedCrops;
    private ActivityCropSelectionsBinding binding;
    @Inject
    UserAPIs userAPIs;
    @Inject
    CompositeDisposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = ActivityCropSelectionsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        selectedCrops = getIntent().<Crop>getParcelableArrayListExtra("crops");
        findViewById();
        initToolbar();
        initView();
        getCrops();
    }
    private void initView() {
        mAdapter = new CropSelectionsAdapter(CropSelectionsActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(CropSelectionsActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        binding.tvCancel.setOnClickListener(v -> finish());
        binding.tvOk.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putParcelableArrayListExtra(SELECTED_CROPS, mAdapter.getSelectedItems());
            setResult(RESULT_OK, returnIntent);
            finish();
        });
    }
    private void findViewById() {
        Glide.with(CropSelectionsActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> finish());
        binding.toolbars.toolbar.setTitle(getString(R.string.choose_crop));
    }
    private void getCrops() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        disposable.add(userAPIs.getCrops()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Crops>() {
                    @Override
                    public void onSuccess(@NonNull Crops crops) {
                        try {
                            if (!crops.isError()) {
                                mAdapter.setData(crops.getData(),selectedCrops);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));


    }
    @Override
    protected void onPause()
    {
        super.onPause();
        hideSoftKeyboard(CropSelectionsActivity.this);
    }
}
