package com.khetigaadi.base.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.khetigaadi.base.fragment.ImageFragment;
import com.khetigaadi.base.model.Banner;

import java.util.List;

public class BannerImagesAdapter extends FragmentStateAdapter {


    private final List<Banner> items;
    private final View.OnClickListener onClickListener;

    public BannerImagesAdapter(@NonNull FragmentActivity fragmentActivity, List<Banner> items, View.OnClickListener onClickListener) {
        super(fragmentActivity);
        this.items = items;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ImageFragment.newInstance(items.get(position), onClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
