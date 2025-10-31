package com.khetigaadi.agri.activity;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khetigaadi.R;
import com.khetigaadi.agri.adapter.ZoomImagePagerAdapter;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.databinding.ActivityImageViewBinding;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class ImageViewActivity extends BaseActivity {
    private ActivityImageViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = ActivityImageViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        initToolbar();
        List<String> urls = new ArrayList<>();
        if (getIntent().getExtras().containsKey("url")) {
            urls.add(getIntent().getStringExtra("url"));
        } else if (getIntent().getExtras().containsKey("urls")) {
            urls = getIntent().getStringArrayListExtra("urls");
        }
        binding.viewPager.setAdapter(new ZoomImagePagerAdapter(this, urls));
        if (urls.size() !=1)
            binding.indicator.setViewPager(binding.viewPager);
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.app_name));
    }
}
