package com.khetigaadi.implement.newImplement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.ListItemBinding;
import com.khetigaadi.implement.newImplement.util.Implements;

import java.util.ArrayList;
import java.util.List;
public class SearchResAdapter extends RecyclerView.Adapter {
    private static final int EMPTY_VIEW = 10;
    private static final int VIEW_ITEM = 1;
    private MyClickListener myClickListener;
    private final List<Implements> mDataset;
    SearchResAdapter(ArrayList<Implements> dataSet) {
        mDataset = dataSet;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        return mDataset.size() == 0 ? EMPTY_VIEW : VIEW_ITEM;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else {
            return new DataObjectHolder(ListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataObjectHolder) {
            ((DataObjectHolder) holder).binding.serchRes.setText(mDataset.get(position).getName());
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size() > 0 ? mDataset.size() : 1;
    }
    class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final com.khetigaadi.databinding.ListItemBinding binding;
        public DataObjectHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAbsoluteAdapterPosition(),v);
        }
    }
}
