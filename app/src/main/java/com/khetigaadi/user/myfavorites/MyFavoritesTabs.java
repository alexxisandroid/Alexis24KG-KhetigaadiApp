package com.khetigaadi.user.myfavorites;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.databinding.TabLayoutBinding;
import com.khetigaadi.user.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class MyFavoritesTabs extends BaseFragment {
    public static int int_items = 3;
    private FragmentActivity activity;
    @Inject
    SessionManager sessionManager;
    private TabLayoutBinding binding;
    List<String> items;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        items = Arrays.asList(getString(R.string.btn_new), getString(R.string.old), getString(R.string.rent));
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = TabLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        binding.viewPager2.setCurrentItem(0);
                        ((MainActivity) activity).setToolbarTitle(getString(R.string.new_tractor));
                        break;
                    case 1:
                        binding.viewPager2.setCurrentItem(1);
                        ((MainActivity) activity).setToolbarTitle(getString(R.string.old_tractor));
                        break;
                    case 2:
                        binding.viewPager2.setCurrentItem(2);
                        ((MainActivity) activity).setToolbarTitle(getString(R.string.rentTractor));
                        break;

                    default:
                        binding.viewPager2.setCurrentItem(tab.getPosition());
                        ((MainActivity) activity).setToolbarTitle(getString(R.string.new_tractor));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPager2.setAdapter(new FavoriteFragmentAdapter(this, items));
        new TabLayoutMediator(binding.tabs, binding.viewPager2, (tab, position) -> tab.setText(items.get(position))).attach();

    }
}