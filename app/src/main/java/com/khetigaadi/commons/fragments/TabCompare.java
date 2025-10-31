package com.khetigaadi.commons.fragments;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.adapter.CompareTabFragmentAdapter;
import com.khetigaadi.databinding.TabLayoutBinding;
import com.khetigaadi.def.CallFrom;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
public class TabCompare extends BaseFragment {
    private TabLayoutBinding binding;
    List<String> items;
    private FragmentActivity activity;
    @CallFrom
    int callFor = CallFrom.NEW_TRACTOR;
    public TabCompare() {
    }
    public static Fragment getInstance(@CallFrom int callFrom) {
        TabCompare fragment = new TabCompare();
        Bundle bundle = new Bundle();
        bundle.putInt("callFor", callFrom);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((MainActivity) activity).setToolbarTitle(getString(R.string.compare));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getArguments() != null)
            callFor = getArguments().getInt("callFor", CallFrom.NEW_TRACTOR);
        items = Arrays.asList(getString(R.string.tractors), getString(R.string.implement));
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
                        break;
                    case 1:
                        binding.viewPager2.setCurrentItem(1);
                        break;

                    default:
                        binding.viewPager2.setCurrentItem(tab.getPosition());
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
        CompareTabFragmentAdapter compareTabFragmentAdapter = new CompareTabFragmentAdapter(this, items);
        binding.viewPager2.setAdapter(compareTabFragmentAdapter);
        new TabLayoutMediator(binding.tabs, binding.viewPager2, (tab, position) -> tab.setText(items.get(position))).attach();
        if (callFor == CallFrom.IMPLEMENT) {
            binding.tabs.setScrollPosition(1, 0f, true);
            binding.viewPager2.setCurrentItem(1);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        addFabButtonItems();
    }
}
