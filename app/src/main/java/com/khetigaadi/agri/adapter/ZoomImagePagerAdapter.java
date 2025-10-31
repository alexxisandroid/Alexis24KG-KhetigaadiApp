package com.khetigaadi.agri.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.databinding.ZoomImageListItemBinding;

import java.util.List;
public class ZoomImagePagerAdapter extends RecyclerView.Adapter<ZoomImagePagerAdapter.MyViewHolder> {
    private final Context context;
    private final List<String> arrayList;
    public ZoomImagePagerAdapter(Context context, List<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @NonNull ZoomImageListItemBinding binding = ZoomImageListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(arrayList.get(position))
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(holder.binding.zoomageView);
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ZoomImageListItemBinding binding;
        public MyViewHolder(@NonNull ZoomImageListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}