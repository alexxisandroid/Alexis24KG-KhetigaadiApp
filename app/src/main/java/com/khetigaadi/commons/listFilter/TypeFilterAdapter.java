package com.khetigaadi.commons.listFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.khetigaadi.base.databinding.FilterListSubItemNewBinding;
import com.khetigaadi.databinding.FilterListSubItemBinding;
import com.khetigaadi.implement.newImplement.util.Type;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class TypeFilterAdapter extends RecyclerView.Adapter<TypeFilterAdapter.DataObjectHolder> implements Filterable {
    private List<Type> stList;
    private List<Type> filteredData;
    private final ItemFilter mFilter = new ItemFilter();
    public TypeFilterAdapter() {
        filteredData = new ArrayList<>();
        stList = filteredData;
    }
    @Override
    public @NotNull DataObjectHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new DataObjectHolder(FilterListSubItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.binding.cbSelect.setText(filteredData.get(holder.getAbsoluteAdapterPosition()).getName());
        holder.binding.cbSelect.setChecked(filteredData.get(holder.getAbsoluteAdapterPosition()).getSelected());
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
    public void updateItems(List<Type> hpList) {
        filteredData = hpList;
        stList = hpList;
        notifyDataSetChanged();
    }
    public List<Type> getSelectedItems() {
        return stList.stream().filter(Type::getSelected).collect(Collectors.toList());
    }
    public void clearSelection() {
        stList = stList.stream().peek(hp -> {
                    if (hp.getSelected())
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
            final List<Type> list = stList;
            int count = list.size();
            final ArrayList<Type> nlist = new ArrayList<>(count);
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
            filteredData = (ArrayList<Type>) results.values;
            notifyDataSetChanged();
        }
    }

    public List<Type> getHPList() {
        return filteredData;
    }
}
