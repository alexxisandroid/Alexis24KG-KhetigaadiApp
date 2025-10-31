package com.khetigaadi.tractor.newTractor;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;
public class NewTractorDetailsAdapter extends FragmentStateAdapter {
    private final List<Fragment> items;
    public NewTractorDetailsAdapter(@NonNull FragmentActivity fragmentActivity, List<Fragment> items) {
        super(fragmentActivity);
        this.items = items;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return items.get(position);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}
