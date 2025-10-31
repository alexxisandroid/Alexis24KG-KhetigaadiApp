package com.khetigaadi.commons.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.khetigaadi.base.model.Model;
import com.khetigaadi.tractor.util.Model;
import com.khetigaadi.databinding.CropSelectionItemRowBinding;

import java.util.ArrayList;
import java.util.List;

public class ModelSelectionAdapter extends RecyclerView.Adapter<ModelSelectionAdapter.MyViewHolder> implements Filterable {

    private List<Model> mModelList;
    private List<Model> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    private OnClickListener onClickListener;

    public ModelSelectionAdapter() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CropSelectionItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Model model = filteredData.get(position);
        holder.itemView.textView.setText(model.getName());
        holder.itemView.textView.setOnClickListener(view -> {
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

    public void setData(List<Model> data) {
        filteredData = data;
        mModelList = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {

        this.onClickListener = onClickListener;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final CropSelectionItemRowBinding itemView;

        private MyViewHolder(@NonNull CropSelectionItemRowBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
            itemView.checkbox.setVisibility(View.GONE);
        }
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Model> list = mModelList;

            int count = list.size();
            final ArrayList<Model> nlist = new ArrayList<>(count);

            Model filterableString;

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
            filteredData = (ArrayList<Model>) results.values;
            notifyDataSetChanged();
        }
    }

    public interface OnClickListener {
        void onClick(Model brand);
    }
}