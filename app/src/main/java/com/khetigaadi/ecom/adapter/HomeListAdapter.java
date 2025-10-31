package com.khetigaadi.ecom.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.HomeListItemBinding;
import com.khetigaadi.model.Others;
import com.khetigaadi.shop.model.CategoryProducts;
import com.khetigaadi.shop.model.Product;
public class HomeListAdapter extends BaseRecyclerViewAdapter<CategoryProducts,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_PROG = 0;
    private ClickListener clickListener;
    public HomeListAdapter(Context context,RecyclerView recyclerView) {
        super(context, recyclerView);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else if (viewType == VIEW_ITEM) {
            vh = new ViewHolder(HomeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent,false);
            vh = new ProgressViewHolder(context, v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            CategoryProducts item = items.get(position);
            ((ViewHolder) holder).binding.tvCategoryLabel.setText(item.getName());
            ((ViewHolder) holder).binding.rvProductsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            AgricultureToolsAdapter agricultureToolsAdapter = new AgricultureToolsAdapter(context, ((ViewHolder) holder).binding.rvProductsList);
            ((ViewHolder) holder).binding.rvProductsList.setAdapter(agricultureToolsAdapter);
            agricultureToolsAdapter.setOnItemClickListener((position1, v) -> {
                if (clickListener != null)
                    clickListener.onItemClick(position1, position, v, item.getProducts().get(position1));
            });
            agricultureToolsAdapter.addItems(item.getProducts(), 1, getOthers());
            ((ViewHolder) holder).binding.tvSeeMore.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onSeeMoreClick(position, v);
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final HomeListItemBinding binding;
        public ViewHolder(@NonNull HomeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ClickListener {
        void onItemClick(int position1,int position,View v,Product product);
        void onSeeMoreClick(int position,View v);
    }
}
