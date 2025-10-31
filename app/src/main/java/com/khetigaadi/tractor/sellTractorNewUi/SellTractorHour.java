package com.khetigaadi.tractor.sellTractorNewUi;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.model.Hours;
import com.khetigaadi.databinding.FragmentSellTractorHourBinding;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class SellTractorHour extends BaseFragment {
    private static final String TAG = SellTractorHour.class.getSimpleName();
    private ArrayList<Hours> alHours;
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private FragmentSellTractorHourBinding binding;
    private FragmentActivity activity;

    public static Fragment getInstance() {
        return new SellTractorHour();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellTractorHourBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider((SellTractorActivity)activity).get(SellViewModel.class);
        alHours = new ArrayList<>();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        alHours.clear();
        getHours();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(6);
    }
    private void getHours() {
        alHours.add(new Hours(1, "Less than 500 Hrs"));
        alHours.add(new Hours(2, "500 - 1000 Hrs"));
        alHours.add(new Hours(3, "1001 - 2000 Hrs"));
        alHours.add(new Hours(4, "2001 - 3000 Hrs"));
        alHours.add(new Hours(5, "3001 - 4000 Hrs"));
        alHours.add(new Hours(6, "4001 - 5000 Hrs"));
        alHours.add(new Hours(7, "5001 - 6000 Hrs"));
        alHours.add(new Hours(8, "6001 - 7000 Hrs"));
        alHours.add(new Hours(9, "7001 - 8000 Hrs"));
        alHours.add(new Hours(10, "8001 - 9000 Hrs"));
        alHours.add(new Hours(11, "9001 - 10000 Hrs"));
        alHours.add(new Hours(12, "above 10000 Hrs"));
        alHours.add(new Hours(13, "Not Available Hrs"));
        initHourList();
    }

    private void initHourList() {
        Timber.e("onChanged: " + alHours.toString());
        try {
            binding.llHour.removeAllViews();
            for (int i = 0; i < alHours.size(); i = i + 2) {
                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setWeightSum(2);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j <= 1 && i + j < alHours.size(); j++) {
                    @NonNull ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout, false);
                    final Hours hours = alHours.get(i + j);
                    rowBinding.tvModelName.setText(hours.getName());
                    if (!TextUtils.isEmpty(viewModel.getUsedTractor().getHoursDriven())) {
                        if (viewModel.getUsedTractor().getHoursDriven().equalsIgnoreCase(hours.getName())) {
                            rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                        }
                    }
                    rowBinding.getRoot().setOnClickListener(v -> {
                        rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                        viewModel.getUsedTractor().setHoursDriven(hours.getName());
                        initHourList();
                        viewModel.getUsedTractor().setHoursDriven(hours.getName());
                        searchHourData(hours.getName());
                    });
                    linearLayout.addView(rowBinding.getRoot());
                }
                binding.llHour.addView(linearLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void searchHourData(String Hours) {
        if (!Hours.isEmpty()) {
            ((SellTractorActivity)activity).callFragments(7);
        }
    }

}
