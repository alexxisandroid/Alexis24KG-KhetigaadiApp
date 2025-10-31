package com.khetigaadi.mandi;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.R;
import com.khetigaadi.base.model.Brand;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.MandiListItemBinding;
import com.khetigaadi.mandi.model.Record;
import com.khetigaadi.model.Others;
import com.khetigaadi.util.Utils;

import org.jetbrains.annotations.NotNull;
public class MandiPriceListAdapter extends BaseRecyclerViewAdapter<Record, Others> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private static final int EMPTY_VIEW = 10;
    Context context;
    public MandiPriceListAdapter(Context context, RecyclerView rvList) {
        super(context, rvList);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.@NotNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else if (viewType == VIEW_ITEM) {
            return new RecyclerViewHolder(MandiListItemBinding.inflate(LayoutInflater.from(context), parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);
            return new ProgressViewHolder(context, v);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder,int position) {
        if (holder instanceof RecyclerViewHolder) {
           // Record record = getItem(position);
            Record record = getItem(position);
            ((RecyclerViewHolder) holder).binding.tvCommodity.setText(record.getCommodity());
            ((RecyclerViewHolder) holder).binding.tvLocation.setText(String.format("%s, %s", record.getState(), record.getDistrict()));
            ((RecyclerViewHolder) holder).binding.tvMarket.setText(String.format("%s - %s", context.getString(R.string.market), record.getMarket()));
            ((RecyclerViewHolder) holder).binding.tvMinPriceValue.setText(String.format("%s%s", context.getString(R.string.rs), Utils.priceConvert(record.getMinPrice())));
            ((RecyclerViewHolder) holder).binding.tvMaxPriceValue.setText(String.format("%s%s", context.getString(R.string.rs), Utils.priceConvert(record.getMaxPrice())));
            ((RecyclerViewHolder) holder).binding.tvModelPriceValue.setText(String.format("%s%s", context.getString(R.string.rs), Utils.priceConvert(record.getModalPrice())));
            ((RecyclerViewHolder) holder).binding.tvVariety.setText(String.format("%s - %s", context.getString(R.string.variety), record.getVariety()));
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final MandiListItemBinding binding;


        public RecyclerViewHolder(@NonNull MandiListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
