package com.khetigaadi.commons.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.R;
import com.khetigaadi.databinding.CropSelectionItemRowBinding;
import com.khetigaadi.implement.newImplement.util.Type;

import java.util.ArrayList;
import java.util.List;

public class TypeSelectionAdapter extends RecyclerView.Adapter<TypeSelectionAdapter.MyViewHolder> implements Filterable {

    private List<Type> mModelList;
    private List<Type> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    private OnClickListener onClickListener;

    public TypeSelectionAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crop_selection_item_row, parent, false);
        return new MyViewHolder(CropSelectionItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Type model = filteredData.get(position);
        holder.binding.textView.setText(model.getName());
        holder.binding.textView.setOnClickListener(view -> {
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
    public void setData(List<Type> data) {
        filteredData = data;
        mModelList = data;
        notifyDataSetChanged();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final com.khetigaadi.databinding.CropSelectionItemRowBinding binding;

        private MyViewHolder(@NonNull CropSelectionItemRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.checkbox.setVisibility(View.GONE);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Type> list = mModelList;

            int count = list.size();
            final List<Type> nlist = new ArrayList<>(count);

            Type filterableString;

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
            filteredData = (List<Type>) results.values;
            notifyDataSetChanged();
        }
    }

    public interface OnClickListener {
        void onClick(Type type);
    }
}