package com.khetigaadi.tractor.sellTractorNewUi;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.databinding.FragmentSellTractorConditionBinding;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SellTractorCondition extends BaseFragment {
    private String engine_condition = "";
    private String tyre_condition = "";
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private FragmentSellTractorConditionBinding binding;
    private FragmentActivity activity;
    public static Fragment getInstance() {
        return new SellTractorCondition();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellTractorConditionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider((SellTractorActivity) activity).get(SellViewModel.class);
        binding.btnPoorEngine.setOnClickListener(View -> {
            engine_condition = "0-25% (poor)";
            binding.btnPoorEngine.setChecked(true);
            binding.btnAverageEngine.setChecked(false);
            binding.btnGoodEngine.setChecked(false);
            binding.btnVeryGdEngine.setChecked(false);
        });
        binding.btnAverageEngine.setOnClickListener(View -> {
            engine_condition = "26-50% (average)";
            binding.btnPoorEngine.setChecked(false);
            binding.btnAverageEngine.setChecked(true);
            binding.btnGoodEngine.setChecked(false);
            binding.btnVeryGdEngine.setChecked(false);
        });
        binding.btnGoodEngine.setOnClickListener(View -> {
            engine_condition = "51-75% (good)";
            binding.btnPoorEngine.setChecked(false);
            binding.btnAverageEngine.setChecked(false);
            binding.btnGoodEngine.setChecked(true);
            binding.btnVeryGdEngine.setChecked(false);
        });
        binding.btnVeryGdEngine.setOnClickListener(View -> {
            engine_condition = "76-100% (very good)";
            binding.btnPoorEngine.setChecked(false);
            binding.btnAverageEngine.setChecked(false);
            binding.btnGoodEngine.setChecked(false);
            binding.btnVeryGdEngine.setChecked(true);
        });

        binding.btnPoorTyre.setOnClickListener(View -> {
            tyre_condition = "0-25% (poor)";
            binding.btnPoorTyre.setChecked(true);
            binding.btnAvgTyre.setChecked(false);
            binding.btnGoodTyre.setChecked(false);
            binding.btnVeryGdTyre.setChecked(false);
        });
        binding.btnAvgTyre.setOnClickListener(View -> {
            tyre_condition = "26-50% (average)";
            binding.btnPoorTyre.setChecked(false);
            binding.btnAvgTyre.setChecked(true);
            binding.btnGoodTyre.setChecked(false);
            binding.btnVeryGdTyre.setChecked(false);
        });
        binding.btnGoodTyre.setOnClickListener(View -> {
            tyre_condition = "51-75% (good)";
            binding.btnPoorTyre.setChecked(false);
            binding.btnAvgTyre.setChecked(false);
            binding.btnGoodTyre.setChecked(true);
            binding.btnVeryGdTyre.setChecked(false);
        });
        binding.btnVeryGdTyre.setOnClickListener(View -> {
            tyre_condition = "76-100% (very good)";
            binding.btnPoorTyre.setChecked(false);
            binding.btnAvgTyre.setChecked(false);
            binding.btnGoodTyre.setChecked(false);
            binding.btnVeryGdTyre.setChecked(true);
        });

        binding.cvConditionNext.setOnClickListener(View -> {
            if (!engine_condition.isEmpty() && !tyre_condition.isEmpty()) {
                viewModel.getUsedTractor().setEngineCondition(engine_condition);
                viewModel.getUsedTractor().setTyreCondition(tyre_condition);
                ((SellTractorActivity) activity).callFragments(9);

            } else {
                Toast.makeText(activity, "Please select fields!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(8);
    }
}
