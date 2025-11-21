package com.khetigaadi.ecom.adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.AppConfig;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.CategoriesListItemBinding;
import com.khetigaadi.databinding.CategoriesNewHomeListItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.shop.model.Category;
import com.khetigaadi.shop.model.Others;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CategoriesAdapter extends BaseRecyclerViewAdapter<Category, Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private boolean isFromHome;
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_PROG = 0;
    private CategoriesAdapter.Listener listener;
    public CategoriesAdapter(Context context,RecyclerView recyclerView,boolean isFromHome) {
        super(context, recyclerView);
        this.context = context;
        this.isFromHome = isFromHome;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            if (isFromHome)
                return new HomeViewHolder(CategoriesNewHomeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            else
                return new ViewHolder(CategoriesListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ProgressViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if (holder instanceof ViewHolder) {
            Category item = items.get(position);

            String othShopImg = others.getShopCategoryImagePath();
            if(othShopImg==null || othShopImg.equals(null) || othShopImg.equals("null") || othShopImg.isEmpty() || othShopImg.equals(""))
                othShopImg = BuildConfig.BASE_URL+"images/shop-category/";
            Glide.with(context)
                    .load(othShopImg+item.getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(((ViewHolder)holder).binding.image);
            if (item.getColorCode() != null)
                ((ViewHolder) holder).binding.image.setBackgroundColor(Color.parseColor(item.getColorCode()));
            ((ViewHolder) holder).binding.title.setText(item.getName());
            ((ViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(v, position, item);
            });
        }
        else if (holder instanceof HomeViewHolder) {
            Category item = items.get(position);
            String othShopImg = others.getShopCategoryImagePath();
            if(othShopImg==null || othShopImg.equals(null) || othShopImg.equals("null") || othShopImg.isEmpty() || othShopImg.equals(""))
                othShopImg = BuildConfig.BASE_URL+"images/shop-category/";
            Glide.with(context)
                    .load(othShopImg + item.getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(((HomeViewHolder) holder).binding.image);
            if (item.getColorCode() != null)
                ((HomeViewHolder) holder).binding.image.setBackgroundColor(Color.parseColor(item.getColorCode()));
            ((HomeViewHolder) holder).binding.title.setText(item.getName());
            ((HomeViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemClick(v, position, item);
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    public void setListener(CategoriesAdapter.Listener listener) {
        this.listener = listener;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final CategoriesListItemBinding binding;
        public ViewHolder(@NonNull CategoriesListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private static class HomeViewHolder extends RecyclerView.ViewHolder {
        private final CategoriesNewHomeListItemBinding binding;
        public HomeViewHolder(@NonNull CategoriesNewHomeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface Listener {
        void onItemClick(View v,int position,Category item);
    }
}
