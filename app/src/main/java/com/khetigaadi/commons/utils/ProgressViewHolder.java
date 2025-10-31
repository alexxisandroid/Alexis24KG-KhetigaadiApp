package com.khetigaadi.commons.utils;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.databinding.ProgressbarItemBinding;
public class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;
    public ProgressViewHolder(Context mContext, View v) {
        super(v);
        Glide.with(mContext)
                .load(R.drawable.loader_white)
                .into((ImageView) v.findViewById(R.id.ivLoader));
    }
    public ProgressViewHolder(Context mContext, ProgressbarItemBinding binding) {
        super(binding.getRoot());
        Glide.with(mContext)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
    }
}