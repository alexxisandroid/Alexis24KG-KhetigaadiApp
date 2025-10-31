package com.khetigaadi.user.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.khetigaadi.databinding.CropSelectionItemRowBinding;
import com.khetigaadi.user.model.UserType;
import java.util.ArrayList;
import java.util.List;
public class UserTypeSelectionAdapter extends RecyclerView.Adapter<UserTypeSelectionAdapter.MyViewHolder> implements Filterable {
    private List<UserType> mModelList;
    private List<UserType> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    private OnClickListener onClickListener;
    public UserTypeSelectionAdapter() {
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(CropSelectionItemRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final UserType model = filteredData.get(position);
        holder.binding.textView.setText(model.getUserLevelName());
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
    public void setData(List<UserType> data) {
        filteredData = data;
        mModelList = data;
        notifyDataSetChanged();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private com.khetigaadi.databinding.CropSelectionItemRowBinding binding;
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
            final List<UserType> list = mModelList;
            int count = list.size();
            final ArrayList<UserType> nlist = new ArrayList<>(count);
            UserType filterableString;
            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getUserLevelName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<UserType>) results.values;
            notifyDataSetChanged();
        }
    }
    public interface OnClickListener {
        void onClick(UserType userType);
    }
}