package com.khetigaadi.commons.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import com.khetigaadi.shop.model.search.Others;
import com.khetigaadi.commons.model.search.Others;
import com.khetigaadi.shop.model.Product;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
public class SearchProductAdapterr extends RecyclerView.Adapter<SearchProductAdapterr.MyViewHolder> implements Filterable {
    private List<Product> filteredData = null;
    private List<Product> mModelList;
    private ItemFilter mFilter = new ItemFilter();
    private OnClickListener onClickListener;
    public SearchProductAdapterr(Context context,List<Product> product,Others others) {

    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public void setOnItemClickListener(@NotNull Object any) {

    }
    @Override
    public Filter getFilter() {
        return null;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Product model = filteredData.get(position);
        holder.view.setText(model.getName());
        holder.view.setOnClickListener(view -> {
            if (onClickListener != null)
                onClickListener.onClick(model);
        });
    }
    @Override
    public int getItemCount() {
        return filteredData == null ? 0:filteredData.size();
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
            final List<Product> list = mModelList;
            int count = list.size();
            final ArrayList<Product> nlist = new ArrayList<>(count);
            Product filterableString;
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
            filteredData = (ArrayList<Product>) results.values;
            notifyDataSetChanged();
        }
    }
    public interface OnClickListener {
        void onClick(Product brand);
    }
    public static class Listener {
    }
}
