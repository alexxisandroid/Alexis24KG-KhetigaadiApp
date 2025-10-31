package com.khetigaadi.commons.utils;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.databinding.EmptyViewBinding;
public class EmptyViewHolder extends RecyclerView.ViewHolder {
    public EmptyViewHolder(View itemView) {
        super(itemView);
    }
    public EmptyViewHolder(EmptyViewBinding binding) {
        super(binding.getRoot());
    }
}