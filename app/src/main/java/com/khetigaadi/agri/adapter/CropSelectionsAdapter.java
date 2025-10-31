package com.khetigaadi.agri.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.R;
import com.khetigaadi.base.model.agri.Crop;
import com.khetigaadi.databinding.CropSelectionItemRowBinding;

import java.util.ArrayList;
import java.util.List;
public class CropSelectionsAdapter extends RecyclerView.Adapter<CropSelectionsAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private List<Crop> mModelList;
    private List<Crop> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    public CropSelectionsAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(CropSelectionItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Crop model = filteredData.get(position);
        holder.binding.textView.setText(model.getName());
        holder.binding.textView.setTextColor(model.isSelected() ? ContextCompat.getColor(context, R.color.colorPrimaryDark) : ContextCompat.getColor(context, R.color.black));
        holder.binding.checkbox.setChecked(model.isSelected());
        holder.binding.checkbox.setOnClickListener(v -> addRemoveSelection(model, holder));
        holder.binding.textView.setOnClickListener(view -> addRemoveSelection(model, holder));
    }
    private void addRemoveSelection(Crop model, MyViewHolder holder) {
        if (model.getName().equalsIgnoreCase("all")) {
            for (int i = 1; i < filteredData.size(); i++) {
                filteredData.get(i).setSelected(!filteredData.get(0).isSelected());
            }
            filteredData.get(0).setSelected(!filteredData.get(0).isSelected());
            notifyDataSetChanged();
        } else {
            model.setSelected(!model.isSelected());
            holder.binding.textView.setTextColor(model.isSelected() ? ContextCompat.getColor(context, R.color.colorPrimaryDark) : ContextCompat.getColor(context, R.color.black));
            holder.binding.checkbox.setChecked(model.isSelected());
        }
    }
    @Override
    public int getItemCount() {
        return filteredData == null ? 0 : filteredData.size();
    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }
    public void setData(List<Crop> data, List<Crop> selectedCrops) {
        if (selectedCrops != null) {
            for (int i = 0; i < data.size(); i++) {
                if (selectedCrops.contains(data.get(i))) {//here i am changed added .getCrop()
                    data.get(i).setSelected(true);
                }
            }
        }
        filteredData = data;
        mModelList = data;
        notifyDataSetChanged();
    }
    public ArrayList<Crop> getSelectedItems() {
        ArrayList<Crop> selectedCrops = new ArrayList<>();
        if (filteredData != null)
            for (Crop crop : filteredData) {
                if (crop.isSelected())
                    selectedCrops.add(crop);
            }
        return selectedCrops;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private View view;
        CropSelectionItemRowBinding binding;
        private MyViewHolder(@NonNull CropSelectionItemRowBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Crop> list = mModelList;
            int count = list.size();
            final ArrayList<Crop> nlist = new ArrayList<>(count);
            Crop filterableString;
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
            filteredData = (ArrayList<Crop>) results.values;
            notifyDataSetChanged();
        }
    }
}