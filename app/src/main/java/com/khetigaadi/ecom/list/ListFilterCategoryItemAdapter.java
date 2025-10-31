package com.khetigaadi.ecom.list;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khetigaadi.databinding.FilterListSubItemBinding;
import com.khetigaadi.shop.model.Category;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
public class ListFilterCategoryItemAdapter extends RecyclerView.Adapter<ListFilterCategoryItemAdapter.DataObjectHolder> implements Filterable {
    private List<Category> stList;
    private ItemFilter mFilter = new ItemFilter();
    private List<Category> filteredData;
    public ListFilterCategoryItemAdapter() {
        filteredData = new ArrayList<>();
        stList = filteredData;
    }
    @Override
    public @NotNull DataObjectHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new DataObjectHolder(FilterListSubItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder,int position) {
        holder.binding.filterName.setText(filteredData.get(position).getName());
        holder.binding.cbSelect.setText(filteredData.get(position).getName());
        holder.binding.cbSelect.setChecked(filteredData.get(position).isSelected());
        holder.binding.cbSelect.setOnClickListener(v -> filteredData.get(position).setSelected(((CheckBox) v).isChecked()));
    }
    @Override
    public int getItemCount() {
        return filteredData == null ? 0 : filteredData.size();
    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }
    public void updateItems(List<Category> brands) {
        filteredData = brands;
        stList = brands;
        notifyDataSetChanged();
    }
    public List<Category> getSelectedItems() {
        return stList.stream().filter(Category::isSelected).collect(Collectors.toList());
    }
    public void clearSelection() {
        stList = stList.stream().peek(brand -> {
                    if (brand.isSelected())
                        brand.setSelected(false);
                }
        ).collect(Collectors.toList());
        filteredData = stList;
        notifyDataSetChanged();
    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder {
       // private final FilterListSubItemNewBinding binding;
        private final FilterListSubItemBinding binding;
        public DataObjectHolder( @NonNull FilterListSubItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Category> list = stList;
            int count = list.size();
            final ArrayList<Category> nlist = new ArrayList<>(count);
            Category filterableString;
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
            filteredData = (ArrayList<Category>) results.values;
            notifyDataSetChanged();
        }
    }
    public List<Category> getBrandNameList() {
        return filteredData;
    }
}
