package com.khetigaadi.commons.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.model.City;
import com.khetigaadi.base.model.TalukaResp;
import com.khetigaadi.commons.adapter.TalukaSelectionAdapter;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityTalukaSelectionBinding;
import com.khetigaadi.room.repository.TalukaRepository;
import com.khetigaadi.user.retrofit.UserAPIs;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class TalukaSelectionActivity extends BaseActivity {
    public static final int TALUKA_SELECTION_REQ = 59;
    public static final String SELECTED_TALUKA = "selectedTaluka";
    private TalukaSelectionAdapter mAdapter;
    private City selectedCity;
    @Inject
    TalukaRepository cityRepository;
    private ActivityTalukaSelectionBinding binding;
    @Inject
    UserAPIs userAPIs;
    @Inject
    TalukaRepository talukaRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityTalukaSelectionBinding.inflate(getLayoutInflater());
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
        selectedCity = getIntent().getParcelableExtra("city");

        if(Constants.isConnectionAvailable(this)) {
            getTalukas();
        } else {
            noInternetConnection(binding.edSearch);
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        cityRepository.getTalukas(selectedCity.getId()).observe(this,cities -> {
            if (cities.size()>0) {
                    if(cities != null && cities.size() != 0) {
                        mAdapter.setData(cities);
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
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
            }
        });
    }
    private void findViewById() {
        Glide.with(TalukaSelectionActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.select_taluka));
    }
    private void initView() {
        mAdapter = new TalukaSelectionAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(TalukaSelectionActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(city -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_TALUKA,city);
            setResult(RESULT_OK,returnIntent);
            finish();
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard(TalukaSelectionActivity.this);
    }
    @SuppressLint("CheckResult")
    private void getTalukas(){
        userAPIs.getTalukas(selectedCity.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new io.reactivex.rxjava3.observers.DisposableSingleObserver<TalukaResp>() {
                    @Override
                    public void onSuccess(@NotNull TalukaResp userResponse) {
                        try {
                            if (!userResponse.isError()) {
                                talukaRepository.insertAll(userResponse.getData());
                                talukaRepository.getTalukas(selectedCity.getId()).observe(TalukaSelectionActivity.this,states -> {
                                    Timber.d("dssadasdasd===" + states.size() + "==" + states.toString());
                                    if (states.size()>0) {
                                        mAdapter.setData(states);
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
                                    else
                                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                                });

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Toast.makeText(TalukaSelectionActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
