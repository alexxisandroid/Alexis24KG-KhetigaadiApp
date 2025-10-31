package com.khetigaadi.commons.listFilter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khetigaadi.commons.utils.Drive;
import com.khetigaadi.databinding.FilterListSubItemBinding;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;
public class ListFilterDriveAdapter extends RecyclerView.Adapter<ListFilterDriveAdapter.DataObjectHolder> {
    private List<Drive> stList;
   // FilterListSubItem hj;
    public ListFilterDriveAdapter(List<Drive> dataSet) {
        stList = dataSet;
    }
    @Override
    public @NotNull DataObjectHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new DataObjectHolder(FilterListSubItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder,int position) {
        holder.binding.filterName.setText(stList.get(holder.getAbsoluteAdapterPosition()).getName());
        holder.binding.cbSelect.setText(stList.get(holder.getAbsoluteAdapterPosition()).getName());
        holder.binding.cbSelect.setChecked(stList.get(holder.getAbsoluteAdapterPosition()).isSelected());
        holder.binding.cbSelect.setOnClickListener(v -> stList.get(holder.getAbsoluteAdapterPosition()).setSelected(((CheckBox)v).isChecked()));
    }
    @Override
    public int getItemCount() {
        return stList.size();
    }
    public void clearSelection() {
        stList = stList.stream().peek(hp -> {
                    if(hp.isSelected())
                        hp.setSelected(false);
                }
        ).collect(Collectors.toList());
        notifyDataSetChanged();
    }
    public List<Drive> getSelectedItems() {
        return stList.stream().filter(Drive::isSelected).collect(Collectors.toList());
    }
    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        private final FilterListSubItemBinding binding;
        public DataObjectHolder(@NonNull FilterListSubItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    // method to access in activity after updating selection
    public List<Drive> getDriveList() {
        return stList;
    }
}
