package com.khetigaadi.commons.activities;
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
import com.khetigaadi.commons.adapter.YearSelectionAdapter;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ActivityStateSelectionBinding;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class YearSelectionActivity extends BaseActivity {
    public static final String SELECTED_YEAR = "selectedYear";
    public static final int YEAR_SELECTION_REQ = 85;
    private YearSelectionAdapter mAdapter;
    private ActivityStateSelectionBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        binding = ActivityStateSelectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        initToolbar();
        initView();
        getStats();
    }
    private void getStats() {
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        try {
            mAdapter.setData(Constants.getYears());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.llProgresss.llProgress.setVisibility(View.GONE);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.select_year));
    }
    private void initView() {
        Glide.with(YearSelectionActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
        mAdapter = new YearSelectionAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(YearSelectionActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(state -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_YEAR, state);
            setResult(RESULT_OK, returnIntent);
            finish();
        });

    }
}
