package com.khetigaadi.tractor.review;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khetigaadi.R;
//import com.khetigaadi.databinding.ActivityReviewBinding;
//import com.khetigaadi.databinding.ActivityReviewBinding;
import com.khetigaadi.databinding.ActivityReviewBinding;
import com.khetigaadi.tractor.newTractor.model.NewTractor;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class ReviewActivity extends AppCompatActivity {
    private ActivityReviewBinding binding;
    private NewTractor tractor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(),false);

        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBarsInsets.left,systemBarsInsets.top,systemBarsInsets.right,systemBarsInsets.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);
        initToolbar();
        tractor = getIntent().getParcelableExtra("tractor");
        binding.rating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (rating == 1) {
                binding.tvRateTitle.setText(getString(R.string.terrible));
                binding.tvRateSubTitle.setText(getString(R.string.i_regret_riding_this_tractor));
                setColorAndImage(ContextCompat.getColor(this, R.color.red), R.drawable.ic_thumb_down_black_24dp);
            } else if (rating == 2) {
                binding.tvRateTitle.setText(getString(R.string.its_bad));
                binding.tvRateSubTitle.setText(R.string.i_know_better_tractor_in_the_same);
                setColorAndImage(ContextCompat.getColor(this, R.color.red), R.drawable.ic_thumb_down_black_24dp);
            } else if (rating == 3) {
                binding.tvRateTitle.setText(getString(R.string.its_ok));
                binding.tvRateSubTitle.setText(R.string.could_have_been_much_better);
                setColorAndImage(ContextCompat.getColor(this, R.color.greenHaze), R.drawable.ic_thumb_up_black_24dp);
            } else if (rating == 4) {
                binding.tvRateTitle.setText(getString(R.string.superb));
                binding.tvRateSubTitle.setText(R.string.i_love_the_tractor);
                setColorAndImage(ContextCompat.getColor(this, R.color.greenHaze), R.drawable.ic_thumb_up_black_24dp);
            } else {
                binding.tvRateTitle.setText(getString(R.string.amazing));
                binding.tvRateSubTitle.setText(R.string.the_tractor_exceeds_all_the_expectations);
                setColorAndImage(ContextCompat.getColor(this, R.color.greenHaze), R.drawable.ic_thumb_up_black_24dp);
            }
        });
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.write_a_review));
    }
    private void setColorAndImage(int color,int image) {
        binding.fabImage.setImageResource(image);
        DrawableCompat.setTint(DrawableCompat.wrap(binding.fabImage.getDrawable()),color);
//        binding.fabImage.setImageResource(buttonDrawable);
    }
}