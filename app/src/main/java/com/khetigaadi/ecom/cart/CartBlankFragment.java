package com.khetigaadi.ecom.cart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.khetigaadi.databinding.FragmentCartBlankBinding;

import org.jetbrains.annotations.NotNull;


public class CartBlankFragment extends Fragment {
    public static final String TAG = "CartBlankFragment";
    private FragmentCartBlankBinding binding;
    private FragmentActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBlankBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.btnShopping.setOnClickListener(v -> activity.onBackPressed());
        return view;
    }


}
