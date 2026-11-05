package com.khetigaadi.ecom.order;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.databinding.MyOrderListItemBinding;
import com.khetigaadi.databinding.MyOrderProductListItemBinding;
import com.khetigaadi.ecom.ProductDetailsActivity;
import com.khetigaadi.shop.model.Order;
import com.khetigaadi.shop.model.OrderProduct;
import com.khetigaadi.shop.model.Others;
import com.khetigaadi.util.TextUtil;
import com.khetigaadi.util.Utils;
public class MyOrderListAdapter extends BaseRecyclerViewAdapter<Order,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_PROG = 0;
    private ClickListener clickListener;
    public MyOrderListAdapter(Context context,RecyclerView recyclerView) {
        super(context, recyclerView);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if(viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else if(viewType == VIEW_ITEM) {
            vh = new ViewHolder(MyOrderListItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item,parent,false);
            vh = new ProgressViewHolder(context,v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if(holder instanceof ViewHolder) {
            Order order = items.get(position);
            Log.d("vbcxbvxcvxcv====",""+order.getProducts().size()+"==="+order.getTotalBill());
            for(OrderProduct item : order.getProducts()) {
                @NonNull MyOrderProductListItemBinding binding = MyOrderProductListItemBinding.inflate(LayoutInflater.from(context), ((ViewHolder) holder).binding.llProducts,false);
                binding.tvName.setText(item.getProduct().getName());
                Glide.with(context).load(others != null && others.getShopProductImagePath() != null && item.getProduct().getImage() != null ? others.getShopProductImagePath()+item.getProduct().getImage().getImage() : "")
                        .error(R.mipmap.no_image)
                        .placeholder(R.mipmap.no_image)
                        .into(binding.ivImage);
                binding.tvDiscPrice.setText(String.format("%s %s", context.getString(R.string.rs),Utils.priceConvert(item.getProduct().getFinalPrice() + "")));
                binding.tvPrice.setPaintFlags(binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                binding.tvPrice.setText(Utils.priceConvert(item.getProduct().getPrice() + ""));
                binding.tvQuantity.setText(String.format("%d", item.getQuantity()));
                if(!TextUtil.isEmptyOrZero(item.getProduct().getDiscountPer())) {
                    binding.tvDiscPer.setVisibility(View.VISIBLE);
                   // binding.tvDiscPer.setText(String.format("%s%% %s", item.getProduct().getDiscountPer(), context.getString(R.string.off)));

                    binding.tvDiscount.setText(String.format("%s%% %s",item.getProduct().getDiscountPer(),context.getString(R.string.off)));

                }
                else
                    binding.tvDiscPer.setVisibility(View.INVISIBLE);
                binding.ivImage.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("product_id", item.getProduct().getProduct_id());
                    // Pass existing product data to detail page for "quick load" feel
                    intent.putExtra("product_item", item);
                    context.startActivity(intent);
                });
                binding.getRoot().setOnClickListener(v -> {
                    if (clickListener != null)
                        clickListener.onItemClick(position,((ViewHolder) holder).binding.getRoot());
                });
                ((ViewHolder) holder).binding.llProducts.addView(binding.getRoot());
            }
            if(order.getProducts().size()>0) {
                ((ViewHolder) holder).binding.getRoot().setVisibility(View.VISIBLE);

                ((ViewHolder) holder).binding.tvTotalAmount.setText(String.format("%s  %s %s",context.getString(R.string.total_amount) + ":",context.getString(R.string.rs),Utils.priceConvert(order.getTotalBill())));
            }
            else  ((ViewHolder) holder).binding.getRoot().setVisibility(View.GONE);
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
        return items.size() > 0 ? items.size():1;
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final MyOrderListItemBinding binding;
        public ViewHolder(@NonNull MyOrderListItemBinding binding) {
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
