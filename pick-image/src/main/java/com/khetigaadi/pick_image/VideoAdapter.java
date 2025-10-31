package com.khetigaadi.pick_image;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.khetigaadi.pick_image.databinding.GalleryitemBinding;
//import com.khetigaadi.R;
//import com.khetigaadi.databinding.GalleryitemBinding;
//import com.khetigaadi.pick_image.databinding.GalleryitemBinding;

import java.util.ArrayList;
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<Video> galleryImages;
    private OnClickListener onClickListener;
    public VideoAdapter(Context context,ArrayList<Video> galleryImages) {
        this.context = context;
        this.galleryImages = galleryImages;
    }
    public int getCount() {
        return galleryImages.size();
    }
    public Object getItem(int position) {
        return position;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        @NonNull GalleryitemBinding binding = GalleryitemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        Video item = galleryImages.get(position);
        holder.binding.image.setOnClickListener(v -> {
            if (onClickListener != null)
                onClickListener.onClick(item, position);
        });
        if (position == 0) {
            holder.binding.image.setImageResource(R.drawable.ic_camera_text);
        } else if (position == 1) {
            holder.binding.image.setImageResource(R.drawable.ic_gallery_text);
        } else {
            Glide.with(context)
                    .load(item.getUri())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.binding.image);
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
        private GalleryitemBinding binding;
        public ViewHolder(GalleryitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    public interface OnClickListener {
        void onClick(Video video, int position);
    }
}

