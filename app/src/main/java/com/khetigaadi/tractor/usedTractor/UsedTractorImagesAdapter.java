package com.khetigaadi.tractor.usedTractor;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.khetigaadi.commons.fragments.ImageFragment;
import java.util.List;
public class UsedTractorImagesAdapter extends FragmentStateAdapter {
    private final List<String> items;
    private final View.OnClickListener onClickListener;
    public UsedTractorImagesAdapter(@NonNull FragmentActivity fragmentActivity,List<String> items,View.OnClickListener onClickListener) {
        super(fragmentActivity);
        this.items = items;
        this.onClickListener = onClickListener;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ImageFragment.newInstance(items.get(position), onClickListener);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
