package com.khetigaadi.pick_image;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.pick_image.databinding.GalleryitemBinding;

import java.util.ArrayList;
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Uri> galleryImages;
    private OnClickListener onClickListener;
    public ImageAdapter(Context context, ArrayList<Uri> galleryImages) {
        this.context = context;
        this.galleryImages = galleryImages;
    }
    public int getCount() {
        return galleryImages.size();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @NonNull GalleryitemBinding binding = GalleryitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri item = galleryImages.get(position);
        holder.binding.image.setOnClickListener(v -> {
            if (onClickListener != null)
                onClickListener.onClick(item, position);
        });
        if (position == 0) {
            holder.binding.image.setImageResource(R.drawable.ic_camera_text);
        } else if (position == 1) {
            holder.binding.image.setImageResource(R.drawable.ic_gallery_text);
        } else {
            Glide.with(context).load(item).into(holder.binding.image);
        }
    }
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return galleryImages.size();
    }
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final GalleryitemBinding binding;
        public ViewHolder(GalleryitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface OnClickListener {
        void onClick(Uri path, int position);
    }
}

