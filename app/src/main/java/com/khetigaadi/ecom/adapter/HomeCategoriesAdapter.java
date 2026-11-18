package com.khetigaadi.ecom.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
//import android.graphics.Color;
import android.util.Log;
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
import com.khetigaadi.databinding.CategoriesHomeListItemBinding;
import com.khetigaadi.databinding.EmptyViewBinding;
import com.khetigaadi.model.Others;
import com.khetigaadi.shop.model.Category;
public class HomeCategoriesAdapter extends BaseRecyclerViewAdapter<Category,Others> {
    private final int VIEW_ITEM = 1;
    private final Context context;
    private static final int EMPTY_VIEW = 10;
    public final int VIEW_PROG = 0;
    private CategoriesAdapter.Listener listener;
    public HomeCategoriesAdapter(Context context,RecyclerView recyclerView) {
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
            vh = new ViewHolder(CategoriesHomeListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
        else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_for_horizontal_item,parent,false);
            vh = new ProgressViewHolder(context,v);
        }
        return vh;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if(holder instanceof ViewHolder) {
//            Category item = items.get(position);
//            if(item != null) {
//                String othShopImg = others.getShopCategoryImagePath();
//                if(othShopImg==null || othShopImg.equals(null) || othShopImg.equals("null") || othShopImg.isEmpty() || othShopImg.equals(""))
//                    othShopImg = BuildConfig.BASE_URL+"images/shop-category/";
//              if(item.getName().equals("All")) {
//                  Glide.with(context)
//                          // .load(others != null && others.getShopCategoryImagePath() != null && item.getImage() != null ? others.getShopCategoryImagePath() + item.getImage() : "")
//                          .load(context.getResources().getDrawable(R.drawable.lady_bug1))
//                          .error(R.mipmap.no_image)
//                          .placeholder(R.mipmap.no_image)
//                          .into(((ViewHolder)holder).binding.image);
//              }
//              else
//                Glide.with(context)
//                           // .load(others != null && others.getShopCategoryImagePath() != null && item.getImage() != null ? others.getShopCategoryImagePath() + item.getImage() : "")
//                        .load(othShopImg+item.getImage())
//                        .error(R.mipmap.no_image)
//                        .placeholder(R.mipmap.no_image)
//                        .into(((ViewHolder) holder).binding.image);
//                //              else if(item.getName().equals("Agriculture Drone")) {
////                  Glide.with(context)
////                          .load(context.getResources().getDrawable(R.drawable.drone))
////                          .error(R.mipmap.no_image)
////                          .placeholder(R.mipmap.no_image)
////                          .into(((ViewHolder)holder).binding.image);
////              }
//                Log.d("vxcvvxvxcvcxv====",""+item.getColorCode());
//               // if(item.getColorCode() != null)
//                   // ((ViewHolder) holder).binding.image.setBackgroundColor(Color.parseColor(item.getColorCode()));
//                ((ViewHolder) holder).binding.title.setText(item.getName());
//            }
//            ((ViewHolder)holder).binding.getRoot().setOnClickListener(v -> {
//                if(listener != null)
//                    listener.onItemClick(v,position,item);
//            });
//        }
//    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,int position) {
        if(holder instanceof ViewHolder) {
            Category item = items.get(position);
            if(item != null) {
                String othShopImg = others.getShopCategoryImagePath();
                if(othShopImg == null || othShopImg.equals("null") || othShopImg.isEmpty())
                    othShopImg = BuildConfig.BASE_URL+"images/shop-category/";
                if(item.getName().equals("All") || item.getName().contains("All Category") || item.getName().contains("All \nCategory")) {
                    Glide.with(context)
                            // .load(others != null && others.getShopCategoryImagePath() != null && item.getImage() != null ? others.getShopCategoryImagePath() + item.getImage() : "")
                            .load(context.getResources().getDrawable(R.drawable.lady_bug1))
                            .error(R.mipmap.no_image)
                            .placeholder(R.mipmap.no_image)
                            .into(((ViewHolder)holder).binding.image);
                }
                else
                    Glide.with(context)
                            //.load(others != null && others.getShopCategoryImagePath() != null && item.getImage() != null ? others.getShopCategoryImagePath() + item.getImage() : "")
                            .load(othShopImg+item.getImage())
                            .error(R.mipmap.no_image)
                            .placeholder(R.mipmap.no_image)
                            .into(((ViewHolder) holder).binding.image);
                //  Log.d("vxcvvxvxcvcxv====",""+item.getColorCode()+"=="+othShopImg+item.getImage());
                //  if(item.getColorCode() != null)
                // ((ViewHolder) holder).binding.image.setBackgroundColor(Color.parseColor(item.getColorCode()));

                if(!item.getName().contains("Special Kit"))
                    ((ViewHolder) holder).binding.title.setText(item.getName().replace(" ","\n"));
                else
                    ((ViewHolder) holder).binding.title.setText(item.getName());
                if(item.getName().equals("All"))
                    ((ViewHolder) holder).binding.title.setText(item.getName().replace("All","All \nCategory"));

            }
            ((ViewHolder)holder).binding.getRoot().setOnClickListener(v -> {
                if(listener != null)
                    listener.onItemClick(v,position,item);
            });
        }
    }

    //    @Override
    public int getItemViewType(int position) {
        if(items.isEmpty()) {
            return EMPTY_VIEW;
        }
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }
    @Override
    public int getItemCount() {
        return !items.isEmpty() ? items.size() : 1;
    }
    public void setListener(CategoriesAdapter.Listener listener) {
        this.listener = listener;
    }
    private static class ViewHolder extends RecyclerView.ViewHolder {
        private final CategoriesHomeListItemBinding binding;
        public ViewHolder(@NonNull CategoriesHomeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface Listener {
        void onItemClick(View v,int position);
    }
}
