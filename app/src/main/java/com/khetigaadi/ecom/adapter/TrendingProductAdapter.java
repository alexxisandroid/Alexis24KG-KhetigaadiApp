package com.khetigaadi.ecom.adapter;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.TrendingListItemBinding;
import com.khetigaadi.databinding.TrendingListItemLargeBinding;
import com.khetigaadi.model.Others;
//import com.khetigaadi.shop.databinding.TrendingListItemBinding;
//import com.khetigaadi.shop.databinding.TrendingListItemLargeBinding;
import com.khetigaadi.shop.model.Product;
import com.khetigaadi.util.TextUtil;
import com.khetigaadi.util.Utils;
public class TrendingProductAdapter extends BaseRecyclerViewAdapter<Product,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_PROG = 0;
    private final int VIEW_LARGE = 2;
    private ClickListener clickListener;
    public TrendingProductAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        this.context = context;
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
            return new ViewHolder(TrendingListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == VIEW_LARGE) {
            return new LargeViewHolder(TrendingListItemLargeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ProgressViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_for_horizontal_item, parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            Product item = items.get(position);
            ((ViewHolder) holder).binding.tvTitle.setText(item.getName());
            Glide.with(context).load(others.getShopProductImagePath() + item.getImage().getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).into(((ViewHolder) holder).binding.image);
            ((ViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onItemClick(position);
            });
            ((ViewHolder) holder).binding.tvPrice.setPaintFlags(((ViewHolder) holder).binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ((ViewHolder) holder).binding.tvPrice.setText(Utils.priceConvert(item.getPrice() + ""));
            ((ViewHolder) holder).binding.tvDiscPrice.setText(String.format("%s %s", context.getString(R.string.rs), Utils.priceConvert(item.getFinalPrice() + "")));
            if (!TextUtil.isEmptyOrZero(item.getDiscountPer())) {
                ((ViewHolder) holder).binding.tvDiscPer.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).binding.tvDiscPer.setText(String.format("%s%% %s", item.getDiscountPer(), context.getString(R.string.off)));
            } else
                ((ViewHolder) holder).binding.tvDiscPer.setVisibility(View.INVISIBLE);
        } else if (holder instanceof LargeViewHolder) {
            Product item = items.get(position);
            ((LargeViewHolder) holder).binding.tvTitle.setText(item.getName());
            Glide.with(context).load(others.getShopProductImagePath() + item.getImage().getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).into(((LargeViewHolder) holder).binding.image);
            ((LargeViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onItemClick(position);
            });
            ((LargeViewHolder) holder).binding.tvPrice.setPaintFlags(((LargeViewHolder) holder).binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ((LargeViewHolder) holder).binding.tvPrice.setText(Utils.priceConvert(item.getPrice() + ""));
            ((LargeViewHolder) holder).binding.tvDiscPrice.setText(String.format("%s %s", context.getString(R.string.rs), Utils.priceConvert(item.getFinalPrice() + "")));
            if (!TextUtil.isEmptyOrZero(item.getDiscountPer())) {
                ((LargeViewHolder) holder).binding.tvDiscPer.setVisibility(View.VISIBLE);
                ((LargeViewHolder) holder).binding.tvDiscPer.setText(String.format("%s%% %s", item.getDiscountPer(), context.getString(R.string.off)));
            } else
                ((LargeViewHolder) holder).binding.tvDiscPer.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return position == 0 ? VIEW_LARGE : items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final TrendingListItemBinding binding;
        public ViewHolder(@NonNull TrendingListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private static class LargeViewHolder extends RecyclerView.ViewHolder {
        private final TrendingListItemLargeBinding binding;
        public LargeViewHolder(TrendingListItemLargeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ClickListener {
        void onItemClick(int position);
    }
}
