package com.khetigaadi.tractor.newTractor;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.khetigaadi.R;
import com.khetigaadi.base.databinding.VpImageBinding;
import com.khetigaadi.commons.utils.Product;
import org.jetbrains.annotations.NotNull;
import java.util.List;
public class NewTractorDetailsPictureAdapter extends PagerAdapter {
    FragmentActivity activity;
    List<Product> products;
    public NewTractorDetailsPictureAdapter(FragmentActivity activity,List<Product> products) {
        this.activity = activity;
        this.products = products;
    }
    @Override
    public int getCount() {
        if(products != null && !products.isEmpty()) {
            return products.size();
        }
        else {
            return 0;
        }
    }
    @Override
    public @NotNull View instantiateItem(@NotNull ViewGroup container,final int position) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        VpImageBinding view = VpImageBinding.inflate(inflater, container, false);
        view.imageDisplay.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Log.d("vcbvcbcbcvcvds====",""+products.get(position).getImageUrl());
        Glide.with(activity)
                .load(products.get(position).getImageUrl())
                .placeholder(R.mipmap.no_image)
                .error(R.mipmap.no_image)
                .into(view.imageDisplay);
        container.addView(view.getRoot());
        return view.getRoot();
    }
    @Override
    public void destroyItem(ViewGroup container,int position,@NotNull Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(@NotNull View view,@NotNull Object object) {
        return view == object;
    }
}