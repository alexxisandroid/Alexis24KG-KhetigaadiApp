package com.khetigaadi.tractor.sellTractorNewUi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.khetigaadi.R;
import com.khetigaadi.commons.activities.YearSelectionActivity;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.commons.utils.Year;
import com.khetigaadi.databinding.FragmentSellTractorYearBinding;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class SellTractorYear extends BaseFragment {
    private static final String TAG = "SellTractorYear";
    private List<Year> alYear;
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private @NonNull
    FragmentSellTractorYearBinding binding;
    private FragmentActivity activity;
    ActivityResultLauncher<Intent> yearSelectResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Year selectedYear = data != null ? data.getParcelableExtra(YearSelectionActivity.SELECTED_YEAR) : null;
                    if (selectedYear != null) {
                        binding.etSellTractorYear.setText(selectedYear.getName());
                        viewModel.getUsedTractor().setModelYear(selectedYear.getName());
                        searchYearData(binding.etSellTractorYear.getText().toString());
                    }
                }
            });
    public static Fragment getInstance() {
        return new SellTractorYear();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSellTractorYearBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(activity).get(SellViewModel.class);
        alYear = new ArrayList<>();
        binding.etSellTractorYear.setOnClickListener(v ->yearSelectResultLauncher.launch(new Intent(activity,YearSelectionActivity.class)));
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        alYear= Constants.getYears();
        initList();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(4);
    }
    private void initList() {
        Timber.e("onChanged: " + alYear.toString());
        try {
            binding.llYear.removeAllViews();
            for (int i = 0; i < alYear.size(); i = i + 2) {
                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setWeightSum(2);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j <= 1 && i + j < alYear.size(); j++) {
//                    View rowLayout = (SellTractorYear.this).getLayoutInflater().inflate(R.layout.model_row, linearLayout, false);
                    ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(), linearLayout, false);
                    final Year year = alYear.get(i + j);
                    rowBinding.tvModelName.setText(year.getName());
                    if (!TextUtils.isEmpty(viewModel.getUsedTractor().getModelYear())) {
                        if (viewModel.getUsedTractor().getModelYear().equalsIgnoreCase(year.getName())) {
                            rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                        }
                    }
                    rowBinding.getRoot().setOnClickListener(v -> {
                        binding.etSellTractorYear.setText(year.getName());
                        rowBinding.ll.setBackground(ContextCompat.getDrawable(activity, R.drawable.button_selector_red));
                        viewModel.getUsedTractor().setModelYear(year.getName());
                        initList();
                        searchYearData(binding.etSellTractorYear.getText().toString());
                    });
                    linearLayout.addView(rowBinding.getRoot());
                }
                binding.llYear.addView(linearLayout);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void searchYearData(String year) {
        if (!year.isEmpty()) {
            ((SellTractorActivity) activity).callFragments(5);
        }
    }

}