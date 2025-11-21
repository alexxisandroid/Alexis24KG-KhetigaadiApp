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
import com.khetigaadi.commons.adapter.MonthSelectionAdapter;
import com.khetigaadi.commons.model.Month;
import com.khetigaadi.databinding.ActivityStateSelectionBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class MonthSelectionActivity extends BaseActivity {
    public static final String SELECTED_MONTH = "selectedMonth";
    public static final int MONTH_SELECTION_REQ = 80;
    private MonthSelectionAdapter mAdapter;
    private ActivityStateSelectionBinding binding;
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
        initToolbar();
        initView();
        getStats();
    }
    private void getStats() {
        List<Month> stateList = new ArrayList<>();
        stateList.add(new Month(1, "Jan"));
        stateList.add(new Month(2, "Feb"));
        stateList.add(new Month(3, "Mar"));
        stateList.add(new Month(4, "April"));
        stateList.add(new Month(5, "May"));
        stateList.add(new Month(6, "June"));
        stateList.add(new Month(7, "July"));
        stateList.add(new Month(8, "Jug"));
        stateList.add(new Month(9, "Sept"));
        stateList.add(new Month(10, "Oct"));
        stateList.add(new Month(11, "Nov"));
        stateList.add(new Month(12, "Dec"));
        binding.llProgresss.llProgress.setVisibility(View.VISIBLE);
        try {
            mAdapter.setData(stateList);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.llProgresss.llProgress.setVisibility(View.GONE);
    }


    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.select_month));
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.select_month));
    }

    private void initView() {
        Glide.with(MonthSelectionActivity.this)
                .load(R.drawable.loader_white)
                .into(binding.llProgresss.ivLoader);
        mAdapter = new MonthSelectionAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(MonthSelectionActivity.this);
        binding.mRecyclerView.setHasFixedSize(true);
        binding.mRecyclerView.setLayoutManager(manager);
        binding.mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnClickListener(month -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SELECTED_MONTH, month);
            setResult(RESULT_OK, returnIntent);
            finish();
        });

    }


}
