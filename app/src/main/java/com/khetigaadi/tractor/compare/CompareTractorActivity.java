package com.khetigaadi.tractor.compare;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khetigaadi.R;
import com.khetigaadi.databinding.ActivityCompareTractorBinding;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class CompareTractorActivity extends AppCompatActivity {
    private CompareTractorViewModel viewModel;
    private ActivityCompareTractorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = ActivityCompareTractorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
//            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
//            return insets;
//        });
//
//        EdgeToEdge.enable(this);

        initToolbar();
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbar.setTitle(getString(R.string.compareTractor));
        //Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.compareTractor));

        getSupportActionBar().setTitle(getResources().getString(R.string.compareTractor));
    }
}