package com.khetigaadi.commons.listFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khetigaadi.implement.newImplement.util.Brand;
import com.khetigaadi.databinding.FilterListSubItemBinding;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class ListFilterBrandItemAdapter1 extends RecyclerView.Adapter<ListFilterBrandItemAdapter1.DataObjectHolder> implements Filterable {
    private List<Brand> stList;
    private ItemFilter mFilter = new ItemFilter();
    private List<Brand> filteredData;
    public ListFilterBrandItemAdapter1() {
        filteredData = new ArrayList<>();
        stList = filteredData;
    }
    @Override
    public @NotNull DataObjectHolder onCreateViewHolder(@NotNull ViewGroup parent,int viewType) {
        return new DataObjectHolder(FilterListSubItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder,int position) {
        Log.d("fgdgdfgfdfg1111===",""+filteredData.get(position).getName());

        if(filteredData.get(position).getName()!=null && !filteredData.get(position).getName().equalsIgnoreCase(null) && !filteredData.get(position).getName().isEmpty()) {
           Log.d("fgdgdfgfdfg===",""+filteredData.get(position).getName());
           holder.binding.filterName.setText(filteredData.get(position).getName());
           holder.binding.cbSelect.setText(filteredData.get(position).getName());
           holder.binding.cbSelect.setChecked(filteredData.get(position).getSelected());
           holder.binding.cbSelect.setOnClickListener(v -> filteredData.get(position).setSelected(((CheckBox) v).isChecked()));
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
    public void updateItems(List<Brand> brands) {
        filteredData = brands;
        stList = brands;
        notifyDataSetChanged();
    }
    public List<Brand> getSelectedItems() {
        return stList.stream().filter(Brand::getSelected).collect(Collectors.toList());
    }
    public void clearSelection() {
        stList = stList.stream().peek(brand -> {
                    if (brand.getSelected())
                        brand.setSelected(false);
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
            final List<Brand> list = stList;
            int count = list.size();
            final ArrayList<Brand> nlist = new ArrayList<>(count);
            Brand filterableString;
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
        protected void publishResults(CharSequence constraint,FilterResults results) {
            filteredData = (ArrayList<Brand>) results.values;
            notifyDataSetChanged();
        }
    }
    public List<Brand> getBrandNameList() {
        return filteredData;
    }
}
