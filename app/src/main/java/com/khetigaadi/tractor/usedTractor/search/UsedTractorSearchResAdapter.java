package com.khetigaadi.tractor.usedTractor.search;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.commons.utils.MyClickListener;
import com.khetigaadi.databinding.ListItemBinding;
import com.khetigaadi.tractor.usedTractor.util.SearchTrByName;

import java.util.List;
public class UsedTractorSearchResAdapter extends RecyclerView.Adapter<UsedTractorSearchResAdapter.DataObjectHolder> {
    private static MyClickListener myClickListener;
    private final List<SearchTrByName> mDataset;
    public UsedTractorSearchResAdapter(List<SearchTrByName> dataSet) {
        mDataset = dataSet;
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        UsedTractorSearchResAdapter.myClickListener = myClickListener;
    }
    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DataObjectHolder(ListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.binding.serchRes.setText(mDataset.get(position).getName());
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ListItemBinding binding;
        public DataObjectHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }
}
