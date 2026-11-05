package com.khetigaadi.ecom.list;
import android.content.Context;
import android.graphics.Paint;
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
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.ProductListItemBinding;
import com.khetigaadi.shop.model.Others;
import com.khetigaadi.shop.model.Product;
import com.khetigaadi.util.TextUtil;
import com.khetigaadi.util.Utils;
public class ProductListAdapter extends BaseRecyclerViewAdapter<Product,Others> {
    public static final int VIEW_ITEM = 1;
    private final Context context;
    public static final int EMPTY_VIEW = 10;
    public static final int VIEW_PROG = 0;
    private ClickListener clickListener;
    public ProductListAdapter(Context context,RecyclerView recyclerView) {
        super(context,recyclerView);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        if(viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else if(viewType == VIEW_ITEM) {
            return new ViewHolder(ProductListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else {
            return new ProgressViewHolder(context,LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item,parent,false));
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if (holder instanceof ViewHolder) {
            Product item = items.get(position);
            ((ViewHolder) holder).binding.tvName.setText(item.getName());
            ((ViewHolder) holder).binding.tvDiscPrice.setText(String.format("%s%s", context.getString(R.string.rs),Utils.priceConvert(item.getFinalPrice() + "")));
            ((ViewHolder) holder).binding.tvPrice.setPaintFlags(((ViewHolder) holder).binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ((ViewHolder) holder).binding.tvPrice.setText(String.format("%s%s", context.getString(R.string.rs),Utils.priceConvert(item.getPrice()+"")));
            if(!TextUtil.isEmptyOrZero(item.getDiscountPer())) {
                ((ViewHolder) holder).binding.priceGroup.setVisibility(View.VISIBLE);
                ((ViewHolder) holder).binding.tvDiscPer.setText(String.format("%s%% %s", item.getDiscountPer(), context.getString(R.string.off)));
                ((ViewHolder) holder).binding.tvDiscount.setText(String.format("%s%%", item.getDiscountPer()));
            }
            else {
                ((ViewHolder) holder).binding.priceGroup.setVisibility(View.INVISIBLE);
            }
            Glide.with(context).load(others.getShopProductImagePath() != null && item.getImage() != null && item.getImage().getImage() != null ?
                    others.getShopProductImagePath() + item.getImage().getImage() : "")
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).into(((ViewHolder) holder).binding.ivImage);
            ((ViewHolder) holder).binding.getRoot().setOnClickListener(v -> {
                if(clickListener != null)
                    clickListener.onItemClick(position, ((ViewHolder) holder).binding.getRoot());
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(items.size() == 0) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM:VIEW_PROG;
    }
    @Override
    public int getItemCount() {
        return items.size() > 0 ? items.size() : 1;
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final ProductListItemBinding binding;
        public ViewHolder(@NonNull ProductListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ClickListener {
        void onItemClick(int position,View v);
    }
}
