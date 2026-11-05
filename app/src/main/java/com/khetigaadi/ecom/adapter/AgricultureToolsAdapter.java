package com.khetigaadi.ecom.adapter;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.khetigaadi.BuildConfig;
import com.khetigaadi.R;
import com.khetigaadi.base.util.BaseRecyclerViewAdapter;
import com.khetigaadi.commons.utils.EmptyViewHolder;
import com.khetigaadi.commons.utils.ProgressViewHolder;
import com.khetigaadi.databinding.AgricultureToolsListItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.shop.model.Others;
import com.khetigaadi.shop.model.Product;
import com.khetigaadi.util.TextUtil;
import com.khetigaadi.util.Utils;
public class AgricultureToolsAdapter extends BaseRecyclerViewAdapter<Product,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static final int EMPTY_VIEW = 10;
    private final int VIEW_PROG = 0;
    private ClickListener clickListener;
    public AgricultureToolsAdapter(Context context,RecyclerView recyclerView) {
        super(context, recyclerView);
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == EMPTY_VIEW) {
            return new EmptyViewHolder(EmptyViewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        } else if(viewType == VIEW_ITEM) {
            vh = new ViewHolder(AgricultureToolsListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item, parent, false);
            vh = new ProgressViewHolder(context, v);
        }
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if (holder instanceof ViewHolder) {
            Product item = items.get(position);
            ((ViewHolder) holder).binding.title.setText(item.getName());
           // Log.d("cvxxcvxvxvxcvcv====",""+others.getShopProductImagePath() + item.getImage().getImage());
            String spath =   others.getShopProductImagePath();
            if(spath==null || spath.equalsIgnoreCase(null) || spath.equalsIgnoreCase("null") || spath.equalsIgnoreCase("null") || spath.isEmpty())
                spath = BuildConfig.BASE_URL+"images/shop_product/";
           // Log.d("zxcxczcxzcxzcz12===",""+spath+item.getImage().getImage());
            Glide.with(context)
                            //.load(others.getShopProductImagePath() != null && item.getImage() != null && item.getImage().getImage() != null ?
                 //   others.getShopProductImagePath() + item.getImage().getImage() : "")
                    .load(spath+item.getImage().getImage())
                    .error(R.mipmap.no_image)
                    .placeholder(R.mipmap.no_image).into(((ViewHolder)holder).binding.image);
            ((ViewHolder) holder).binding.tvPrice.setPaintFlags(((ViewHolder) holder).binding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            ((ViewHolder) holder).binding.tvPrice.setText( context.getResources().getString(R.string.rs)+Utils.priceConvert(item.getPrice() + ""));
            ((ViewHolder) holder).binding.tvDiscPrice.setText(String.format("%s %s", context.getString(R.string.rs), Utils.priceConvert(item.getFinalPrice() + "")));
            ((ViewHolder)holder).binding.tvDiscount.setVisibility(View.VISIBLE);
            if(!TextUtil.isEmptyOrZero(item.getDiscountPer())) {
                ((ViewHolder)holder).binding.priceGroup.setVisibility(View.VISIBLE);
               // ((ViewHolder)holder).binding.tvDiscPer.setText(String.format("%s%% %s",item.getDiscountPer(),context.getString(R.string.off)));
             //tvDiscount
                ((ViewHolder)holder).binding.tvDiscount.setText(String.format("%s%% %s",item.getDiscountPer(),context.getString(R.string.off)));

            }
            else {
                ((ViewHolder)holder).binding.tvDiscount.setText(String.format("%s%% %s",item.getDiscountPer(),context.getString(R.string.off)));

                ((ViewHolder) holder).binding.priceGroup.setVisibility(View.VISIBLE);
            }
            ((ViewHolder)holder).binding.getRoot().setOnClickListener(v -> {
                if(clickListener != null)
                    clickListener.onItemClick(position,((ViewHolder)holder).binding.getRoot());
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
        private final AgricultureToolsListItemBinding binding;
        public ViewHolder(@NonNull AgricultureToolsListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface ClickListener {
        void onItemClick(int position,View v);
    }
}
