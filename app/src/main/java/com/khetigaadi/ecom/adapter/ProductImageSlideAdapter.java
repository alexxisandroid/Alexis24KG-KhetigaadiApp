package com.khetigaadi.ecom.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.ProductImageListItemBinding;
import com.khetigaadi.shop.model.ProductImage;
import org.jetbrains.annotations.NotNull;
import java.util.List;
public class ProductImageSlideAdapter extends PagerAdapter {
    private final List<ProductImage> productImages;
    private final String imagePath;
    private final Listener listener;
    private final Context context;
    public ProductImageSlideAdapter(Context context, List<ProductImage> products, String imagePath, Listener listener) {
        this.context = context;
        this.productImages = products;
        this.imagePath = imagePath;
        this.listener = listener;
    }
    @Override
    public int getCount() {
        return productImages.size();
    }
    @Override
    public @NotNull View instantiateItem(@NotNull ViewGroup container,final int position) {
        ProductImageListItemBinding binding = ProductImageListItemBinding.inflate(LayoutInflater.from(container.getContext()),container,false);
        binding.rlLayout.setPadding(0,0,0,0);
        Glide.with(context)
                .load(Constants.decodeURL(imagePath + productImages.get(position).getImage()))
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(binding.imageDisplay);
        container.addView(binding.getRoot());
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(imagePath, productImages);
        });
        return binding.getRoot();
    }
    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }
    public interface Listener {
        void onItemClick(String baseUrl, List<ProductImage> productImages);
    }
}