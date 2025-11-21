package com.khetigaadi.commons.activities;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.khetigaadi.base.model.StateResp;
import com.khetigaadi.commons.adapter.StateSelectionAdapter;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityStateSelectionBinding;
import com.khetigaadi.room.repository.StateRepository;
import com.khetigaadi.user.retrofit.UserAPIs;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

@AndroidEntryPoint
public class StateSelectionActivity extends BaseActivity {
    public static final String SELECTED_STATE = "selectedState";
    private StateSelectionAdapter mAdapter;
    @Inject
    StateRepository stateRepository;
    private ActivityStateSelectionBinding binding;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable disposable = new io.reactivex.rxjava3.disposables.CompositeDisposable();
    @Inject
    UserAPIs userAPIs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityStateSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//            ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//        EdgeToEdge.enable(this);
        if (Constants.isConnectionAvailable(this)) {
            getStatt();
        } else {
            noInternetConnection(binding.edSearch);
        }


        findViewById();
        initToolbar();
        initView();
    }
    @Override
    public void onStart() {
        super.onStart();
        stateRepository.getStates().observe(this,states -> {
            Log.d("vcbcvbcv===",""+states);
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
    private void findViewById() {
        Glide.with(StateSelectionActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.select_state));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.select_state));
    }
    private void initView() {
        mAdapter = new StateSelectionAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(StateSelectionActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(state -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_STATE, state);
            setResult(RESULT_OK,returnIntent);
            finish();
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyboard(StateSelectionActivity.this);
    }
    /** @noinspection ResultOfMethodCallIgnored*/
    @SuppressLint("CheckResult")
    private void getStatt(){
       userAPIs.getStates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new io.reactivex.rxjava3.observers.DisposableSingleObserver<StateResp>() {
                    @Override
                    public void onSuccess(@NotNull StateResp userResponse) {
                        try {
                             if (!userResponse.isError()) {
                                stateRepository.insertAll(userResponse.getStates());
                                 stateRepository.getStates().observe(StateSelectionActivity.this,states -> {
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
                        Toast.makeText(StateSelectionActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
