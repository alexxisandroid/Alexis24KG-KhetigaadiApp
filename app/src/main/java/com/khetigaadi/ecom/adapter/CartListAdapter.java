package com.khetigaadi.ecom.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.CartListItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.model.Others;
import com.khetigaadi.shop.model.OrderProduct;
public class CartListAdapter extends BaseRecyclerViewAdapter<OrderProduct,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_PROG = 0;
    private ClickListener clickListener;
    public CartListAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            ));
        } else if (viewType == VIEW_ITEM) {
            return new ViewHolder(CartListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new ProgressViewHolder(context, LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent, false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if (holder instanceof ViewHolder) {
            OrderProduct item = items.get(position);
            Log.d("cvbcvbcvbvfxgvxc====",""+"=="+item.getProduct().toString()+"=="+item.getProduct().getImage());
            if(item.getProduct()!=null && !item.getProduct().equals(null) && !item.getProduct().equals("null"))
                ((ViewHolder) holder).binding.tvName.setText(item.getProduct().getName());
           String othImgg = others.getShopProductImagePath();
            if(others.getShopProductImagePath()==null || others.getShopProductImagePath().equals(null) || others.getShopProductImagePath().equals("null") || others.equals(""))
                othImgg = "https://khetigaadi.com/images/shop_product/";
            Glide.with(context)
                   // .load(others.getShopProductImagePath() != null && item.getProduct().getImages() != null && item.getProduct().getImages().size() > 0 ?
                   // others.getShopProductImagePath()+item.getProduct().getImages().get(0).getImage():"")
                    .load(othImgg+item.getProduct().getImages().get(0).getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image)
                    .into(((ViewHolder) holder).binding.ivImage);
            if(item.getProduct()!=null && !item.getProduct().equals(null) && !item.getProduct().equals("null"))
                ((ViewHolder)holder).binding.tvAmount.setText(String.format("%d",item.getProduct().getFinalPrice()));
            ((ViewHolder) holder).binding.tvQuantity.setText(String.format("%d",item.getQuantity()));
            ((ViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onItemClick(position, ((ViewHolder) holder).binding.getRoot());
            });
            ((ViewHolder) holder).binding.tvMinus.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onMinusClick(v, position);
               // notifyItemChanged(position);
            });
            ((ViewHolder) holder).binding.tvPlus.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onPlusClick(v, position);
               // notifyItemChanged(position);
            });
            ((ViewHolder) holder).binding.ivTrash.setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onDeleteClick(v, position);
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
        private final CartListItemBinding binding;
        public ViewHolder(@NonNull CartListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ClickListener {
        void onItemClick(int position,View v);
        void itemUpdate(OrderProduct item,int position);
        void onMinusClick(View view,int position);
        void onPlusClick(View view,int position);
        void onDeleteClick(View view,int position);
    }
}
