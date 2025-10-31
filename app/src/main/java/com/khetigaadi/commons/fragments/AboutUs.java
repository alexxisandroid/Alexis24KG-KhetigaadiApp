package com.khetigaadi.commons.fragments;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.khetigaadi.MainActivity;
import com.khetigaadi.R;

import org.jetbrains.annotations.NotNull;
/**
 * Created by Irfan on 12-Jul-16.
 */
public class AboutUs extends BaseFragment {
    private FragmentActivity activity;
    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.about_khetigaadi));
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_us, container, false);
    }
}
