package com.khetigaadi.commons.activities;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.adapter.HpSelectionAdapter;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.commons.utils.HpResponse;
import com.khetigaadi.databinding.ActivityStateSelectionBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.HpRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class HpSelectionActivity extends BaseActivity {
    private static final String TAG = "HpSelectionActivity";
    public static final String SELECTED_HP = "selectedHp";
    public static final int HP_SELECTION_REQ = 94;
    private HpSelectionAdapter mAdapter;
    @Inject
    HpRepository hpRepository;
    private ActivityStateSelectionBinding binding;
    @Inject
    KhetiAPIs khetiAPIs;
    private final CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityStateSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        findViewById();
        initToolbar();
        initView();
        getHps();
    }
    private void getHps() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        try {
            hpRepository.getHPs().observe(HpSelectionActivity.this, listResource -> {
                Log.d("cbnccncnncjjhkghjg====",""+listResource.isEmpty()+"=="+listResource.size());
                if (!listResource.isEmpty()) {
                    List<Hp> hps = listResource;
                    try {
                        mAdapter.setData(hps);
                        binding.edSearch.addTextChangedListener(new TextWatcher() {

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                mAdapter.getFilter().filter(s.toString());
                            }

                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    getHpsList();
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void findViewById() {
        Glide.with(HpSelectionActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.select_hp));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.select_hp));
    }
    private void initView() {
        mAdapter = new HpSelectionAdapter(HpSelectionActivity.this);
        LinearLayoutManager manager = new LinearLayoutManager(HpSelectionActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(hp -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_HP,hp);
            setResult(RESULT_OK,returnIntent);
            finish();
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard(HpSelectionActivity.this);
    }
    public void getHpsList()
    {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        disposable.add(khetiAPIs.getHPs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<HpResponse>() {
                    @Override
                    public void onSuccess(@NotNull HpResponse safetyTipsResp) {
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                        try {
                           // binding.tvTips.setText(Html.fromHtml(safetyTipsResp.getData().getContent()));
                          List<Hp> hps = safetyTipsResp.getHp();
                         // Log.d("cvbcvbcvbcvbc===",""+hps.size());
                            mAdapter.setData(hps);
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
                            for(int i=0;i<hps.size();i++) {
                                Hp cvbh = hps.get(i);
                                hpRepository.deleteHP(cvbh);
                            }
                            hpRepository.insertAll(hps);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e(e, "onError: ");
                        binding.llProgresss.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
}
