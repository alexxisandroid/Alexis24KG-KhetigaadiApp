package com.khetigaadi.tractor.compare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.CompareTraImageItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.ProgressbarItemBinding;
import com.khetigaadi.tractor.compare.util.CompareTraImage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CompareTraImagesAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private final ArrayList<CompareTraImage> mTractors;
    private static final int EMPTY_VIEW = 10;

    public CompareTraImagesAdapter(Context mContext, ArrayList<CompareTraImage> tractorList) {
        this.mContext = mContext;
        this.mTractors = tractorList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mTractors.size() == 0) {
            return EMPTY_VIEW;
        }
        return mTractors.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(CompareTraImageItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else {

            vh = new ProgressViewHolder(mContext, ProgressbarItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.@NotNull ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            CompareTraImage currentTractor = mTractors.get(position);
            Glide.with(mContext)
                    .load(Constants.decodeURL(currentTractor.getImage()))
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.image);
            Glide.with(mContext)
                    .load(Constants.decodeURL(currentTractor.getImage1()))
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.image1);
        }
    }


    @Override
    public int getItemCount() {
        return mTractors.size() > 0 ? mTractors.size() : 1;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final CompareTraImageItemBinding binding;


        public ViewHolder(@NonNull CompareTraImageItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }
}
