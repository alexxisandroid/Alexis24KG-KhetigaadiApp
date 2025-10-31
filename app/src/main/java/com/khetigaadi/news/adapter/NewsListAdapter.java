package com.khetigaadi.news.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.commons.utils.News;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.NewsListItemBinding;
import com.khetigaadi.model.Others;
public class NewsListAdapter extends BaseRecyclerViewAdapter<News, Others> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final Context mContext;
    private static final int EMPTY_VIEW = 10;
    private static MyClickListener myClickListener;
    public NewsListAdapter(Context mContext, RecyclerView recyclerView) {
        super(mContext, recyclerView);
        this.mContext = mContext;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        NewsListAdapter.myClickListener = myClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            return new ViewHolder(NewsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ProgressViewHolder(mContext, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            News item = items.get(position);
            ((ViewHolder) holder).binding.newsHeading.setText(item.getPostTitle());
//            ((ViewHolder) holder).binding.tvViews.setText(NumberFormatting.format(item.getvi()));
            Glide.with(mContext)
                    .load(item.getImage() != null ? item.getImage().getImage() : null)
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.newsImage);
        }
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final NewsListItemBinding binding;
        public ViewHolder(@NonNull NewsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> myClickListener.onItemClick(getAbsoluteAdapterPosition(), view));
        }
    }
}
