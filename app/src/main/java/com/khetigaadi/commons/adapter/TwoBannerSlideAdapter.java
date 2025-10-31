package com.khetigaadi.commons.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.khetigaadi.base.R;
import com.khetigaadi.base.model.Banner;
import com.khetigaadi.databinding.VpImageBinding;
import java.util.List;
public class TwoBannerSlideAdapter extends PagerAdapter {
    private final FragmentActivity activity;
    private final List<Banner> products;
    public TwoBannerSlideAdapter(FragmentActivity activity, List<Banner> products) {
        this.activity = activity;
        this.products = products;
    }
    @Override
    public int getCount() {
        return products.size();
    }
    @NonNull
    @Override
    public View instantiateItem(@NonNull ViewGroup container, final int position) {
        VpImageBinding binding = VpImageBinding.inflate(LayoutInflater.from(container.getContext()), container, false);
        binding.imageDisplay.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(activity)
                .load(products.get(position).getImageUrl())
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(binding.imageDisplay);
//        if (position == getCount() - 1) {
//
//            ViewGroup.LayoutParams params = binding.imageDisplay.getLayoutParams();
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            binding.imageDisplay.setLayoutParams(params);
//
//        }
//        else {
//
//        }
        container.addView(binding.getRoot());
        return binding.getRoot();
    }
    @Override
    public void destroyItem(ViewGroup container,int position,@NonNull Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view,@NonNull Object object) {
        return view == object;
    }
}