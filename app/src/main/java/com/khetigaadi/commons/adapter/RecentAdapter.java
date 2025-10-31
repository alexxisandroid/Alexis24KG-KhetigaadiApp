package com.khetigaadi.commons.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.model.Recent;
import com.khetigaadi.commons.utils.OnLoadMoreListener;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.RecentListItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Irfan on 1/14/2016.
 */
public class RecentAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private List<Recent> mNews;
    private MyClickListener myClickListener;

    public RecentAdapter(Context mContext, List<Recent> recordList, RecyclerView recyclerView) {
        this.mContext = mContext;
        mNews = recordList;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mNews.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(RecentListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            vh = new ProgressViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent, false));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.@NotNull ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Recent recent = mNews.get(position);
            ((ViewHolder) holder).binding.newsHeading.setText(recent.getName());
            Glide.with(mContext)
                    .load(recent.getImage())
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image).into(((ViewHolder) holder).binding.newsImage);
        }
    }

    public void setLoaded() {
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
    }

    public void addItems(List<Recent> recents) {
        mNews = recents;
        notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private final RecentListItemBinding binding;

        public ViewHolder(@NonNull RecentListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(v -> myClickListener.onItemClick(getAbsoluteAdapterPosition(), mNews.get(getAbsoluteAdapterPosition()), v));
        }

    }

    public interface MyClickListener {
        void onItemClick(int position, Recent recent, View v);
    }

}
