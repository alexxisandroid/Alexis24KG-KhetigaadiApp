package com.khetigaadi.commons.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khetigaadi.R;
import com.khetigaadi.commons.model.Hp;

import java.util.ArrayList;
import java.util.List;
public class HpSelectionAdapter extends RecyclerView.Adapter<HpSelectionAdapter.MyViewHolder> implements Filterable {
    private List<Hp> mModelList;
    private List<Hp> filteredData = null;
    private ItemFilter mFilter = new ItemFilter();
    private OnClickListener onClickListener;
    public HpSelectionAdapter(Context context) {
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_list_item, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Hp model = filteredData.get(position);
        holder.view.setText(model.getHpRange());
        holder.view.setOnClickListener(view -> {
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
    public void setData(List<Hp> data) {
        filteredData = data;
        mModelList = data;
        notifyDataSetChanged();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView view;
        private MyViewHolder(View itemView) {
            super(itemView);
            view = (TextView) itemView;
        }
    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<Hp> list = mModelList;
            int count = list.size();
            final List<Hp> nlist = new ArrayList<>(count);
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
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<Hp>) results.values;
            notifyDataSetChanged();
        }
    }
    public interface OnClickListener {
        void onClick(Hp brand);
    }
}
