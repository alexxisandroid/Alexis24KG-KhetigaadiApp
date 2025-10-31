package com.khetigaadi.commons.listFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.khetigaadi.base.databinding.FilterListSubItemNewBinding;
import com.khetigaadi.base.model.City;
import com.khetigaadi.databinding.FilterListSubItemBinding;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class CityFilterAdapter extends RecyclerView.Adapter<CityFilterAdapter.DataObjectHolder> implements Filterable {
    private List<City> stList;
    private List<City> filteredData;
    private final ItemFilter mFilter = new ItemFilter();
    public CityFilterAdapter() {
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
    public void updateItems(List<City> hpList) {
        filteredData = hpList;
        stList = hpList;
        notifyDataSetChanged();
    }
    public List<City> getSelectedItems() {
        return stList.stream().filter(City::isSelected).collect(Collectors.toList());
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
            final List<City> list = stList;
            int count = list.size();
            final ArrayList<City> nlist = new ArrayList<>(count);
            City filterableString;
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
            filteredData = (ArrayList<City>) results.values;
            notifyDataSetChanged();
        }
    }
    public List<City> getHPList() {
        return filteredData;
    }
}
