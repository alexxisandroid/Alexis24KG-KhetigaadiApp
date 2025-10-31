package com.khetigaadi.implement.compare;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.databinding.ListItemBinding;
import com.khetigaadi.implement.newImplement.util.Type;

import java.util.ArrayList;
import java.util.List;
public class ImpTypeCompAdapter extends RecyclerView.Adapter<ImpTypeCompAdapter.DataObjectHolder> implements Filterable {
    private ClickListener myClickListener;
    private List<Type> mStringFilterList;
    private List<Type> stList;
    private ItemFilter mFilter;
    public ImpTypeCompAdapter(ArrayList<Type> dataSet) {
        stList = dataSet;
        mStringFilterList = dataSet;
    }
    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
    public List<Type> getList() {
        return stList;
    }
    @NonNull
    @Override
    public DataObjectHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        return new DataObjectHolder(ListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }
    @Override
    public void onBindViewHolder(DataObjectHolder holder,int position) {
        holder.binding.serchRes.setText(stList.get(position).getName());
    }
    @Override
    public int getItemCount() {
        return stList.size();
    }
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }
    public void setData(List<Type> types) {
        stList = types;
        mStringFilterList = types;
        notifyDataSetChanged();
    }
    public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final com.khetigaadi.databinding.ListItemBinding binding;
        public DataObjectHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(v,getAbsoluteAdapterPosition(),stList.get(getAbsoluteAdapterPosition()));
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Type> filterList = new ArrayList<>();
                for (int i = 0; i < mStringFilterList.size(); i++) {
                    if ((mStringFilterList.get(i).getName().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        filterList.add(mStringFilterList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mStringFilterList.size();
                results.values = mStringFilterList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {
            stList = (ArrayList<Type>) results.values;
            notifyDataSetChanged();
        }
    }
    public interface ClickListener {
        void onItemClick(View v,int position,Type type);
    }
}
