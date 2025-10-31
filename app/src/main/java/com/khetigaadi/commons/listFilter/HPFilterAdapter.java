package com.khetigaadi.commons.listFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.khetigaadi.base.databinding.FilterListSubItemNewBinding;
import com.khetigaadi.commons.model.Hp;
import com.khetigaadi.databinding.FilterListSubItemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class HPFilterAdapter extends RecyclerView.Adapter<HPFilterAdapter.DataObjectHolder> implements Filterable {
    private List<Hp> stList;
    private List<Hp> filteredData;
    private final ItemFilter mFilter = new ItemFilter();
    public HPFilterAdapter() {
        filteredData = new ArrayList<>();
        stList = filteredData;
    }
    @Override
    public @NotNull DataObjectHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new DataObjectHolder(FilterListSubItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Log.d("fgvsgsgsdgfsdgs===",""+filteredData.get(holder.getAbsoluteAdapterPosition()).getHpRange());
        holder.binding.filterName.setText(filteredData.get(holder.getAbsoluteAdapterPosition()).getHpRange());
        holder.binding.cbSelect.setText(filteredData.get(holder.getAbsoluteAdapterPosition()).getHpRange());
        holder.binding.cbSelect.setChecked(filteredData.get(holder.getAbsoluteAdapterPosition()).isSelected());
        holder.binding.cbSelect.setOnClickListener(v -> filteredData.get(holder.getAbsoluteAdapterPosition()).setSelected(((CheckBox) v).isChecked()));
    }
    @Override
    public int getItemCount() {
        return filteredData == null ? 0 : filteredData.size();
    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }
    public void updateItems(List<Hp> hpList) {
        filteredData = hpList;
        stList = hpList;
        notifyDataSetChanged();
    }
    public List<Hp> getSelectedItems() {
        return stList.stream().filter(Hp::isSelected).collect(Collectors.toList());
    }
    public void clearSelection() {
        stList = stList.stream().peek(hp -> {
                    if (hp.isSelected())
                        hp.setSelected(false);
                }
        ).collect(Collectors.toList());
        filteredData = stList;
        notifyDataSetChanged();
    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        private final FilterListSubItemBinding binding;

        public DataObjectHolder(@NonNull FilterListSubItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Hp> list = stList;
            int count = list.size();
            final ArrayList<Hp> nlist = new ArrayList<>(count);
            Hp filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getHpRange().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
            filteredData = (ArrayList<Hp>) results.values;
            notifyDataSetChanged();
        }
    }
    public List<Hp> getHPList() {
        return filteredData;
    }
}
