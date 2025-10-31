package com.khetigaadi.base.adapter;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.khetigaadi.base.R;
import com.khetigaadi.base.model.Banner;
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
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.vp_image, container, false);
        RelativeLayout rlLayout = view.findViewById(R.id.rlLayout);
//        rlLayout.setPadding(0, 0, 0, 0);
        ImageView mImageView = view
                .findViewById(R.id.image_display);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(activity)
                .load(products.get(position).getImageUrl())
                .error(R.mipmap.no_image)
                .placeholder(R.mipmap.no_image)
                .into(mImageView);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}