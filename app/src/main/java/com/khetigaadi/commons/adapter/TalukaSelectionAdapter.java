package com.khetigaadi.commons.adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.base.model.Taluka;
import com.khetigaadi.databinding.StateListItemBinding;

import java.util.ArrayList;
import java.util.List;
public class TalukaSelectionAdapter extends RecyclerView.Adapter<TalukaSelectionAdapter.MyViewHolder> implements Filterable {
    private List<Taluka> mModelList;
    private List<Taluka> filteredData = null;
    private final ItemFilter mFilter = new ItemFilter();
    private OnClickListener onClickListener;
    public TalukaSelectionAdapter() {
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(StateListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Taluka model = filteredData.get(position);
        holder.binding.getRoot().setText(model.getName());
        holder.binding.getRoot().setOnClickListener(view -> {
            if (onClickListener != null)
                onClickListener.onClick(model);
        });
    }
    @Override
    public int getItemCount() {
        return filteredData == null ? 0 : filteredData.size();
    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }
    public void setData(List<Taluka> data) {
        filteredData = data;
        mModelList = data;
        notifyDataSetChanged();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final StateListItemBinding binding;
        private MyViewHolder(@NonNull StateListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Taluka> list = mModelList;
            int count = list.size();
            final ArrayList<Taluka> nlist = new ArrayList<>(count);
            Taluka filterableString;
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Taluka>) results.values;
            notifyDataSetChanged();
        }
    }
    public interface OnClickListener {
        void onClick(Taluka brand);
    }
}

