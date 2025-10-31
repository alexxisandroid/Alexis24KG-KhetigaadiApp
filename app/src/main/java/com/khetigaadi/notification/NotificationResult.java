package com.khetigaadi.notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.activities.BaseActivity;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.NotificationResultBinding;
import com.khetigaadi.notification.model.Notification;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NotificationResult extends BaseActivity {
    private NotificationResultBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NotificationResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initToolbar();
        Intent intent = getIntent();
        Notification notification = intent.getParcelableExtra("notification");
        binding.tvTitle.setText(notification.getTitle());
        if (notification.getImageUrl() != null && !notification.getImageUrl().equals("")) {
            Glide.with(this)
                    .load(Constants.decodeURL(notification.getImageUrl()))
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(binding.ivImage);
            binding.ivImage.setVisibility(View.VISIBLE);
        } else {
            binding.ivImage.setVisibility(View.GONE);
        }
        binding.description.setText(notification.getDescription());
    }
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.search);
        shareItem.setVisible(false);
        return true;
    }
    private void initToolbar() {
        setSupportActionBar(binding.toolbars.toolbar);
        binding.toolbars.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        binding.toolbars.toolbar.setNavigationOnClickListener(v -> onBackPressed());
        binding.toolbars.toolbar.setTitle(getString(R.string.notification));
    }
}
