package com.khetigaadi.tractor.sellTractorNewUi;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.model.Month;
import com.khetigaadi.databinding.FragmentSellTractorMonthBinding;
import com.khetigaadi.databinding.ModelRowBinding;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class SellTractorMonth extends BaseFragment {
    private static final String TAG = SellTractorMonth.class.getSimpleName();
    private List<Month> alMonth;
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private FragmentSellTractorMonthBinding binding;
    private FragmentActivity activity;
    public static Fragment getInstance() {
        return new SellTractorMonth();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSellTractorMonthBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider((SellTractorActivity)activity).get(SellViewModel.class);
        init();
        getMonth();
        return view;
    }
    private void init() {
        alMonth = new ArrayList<>();
    }
    private void getMonth() {
        alMonth.clear();
        alMonth.add(new Month(1, "Jan"));
        alMonth.add(new Month(2, "Feb"));
        alMonth.add(new Month(3, "Mar"));
        alMonth.add(new Month(4, "April"));
        alMonth.add(new Month(5, "May"));
        alMonth.add(new Month(6, "June"));
        alMonth.add(new Month(7, "July"));
        alMonth.add(new Month(8, "Aug"));
        alMonth.add(new Month(9, "Sept"));
        alMonth.add(new Month(10, "Oct"));
        alMonth.add(new Month(11, "Nov"));
        alMonth.add(new Month(12, "Dec"));
        initList();
    }
    private void initList() {
        Timber.e("onChanged: " + alMonth.toString());
        try {
            binding.llMonth.removeAllViews();
            for (int i = 0; i < alMonth.size(); i = i + 2) {
                LinearLayout linearLayout = new LinearLayout(activity);
                linearLayout.setWeightSum(2);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (int j = 0; j <= 1 && i + j < alMonth.size(); j++) {
                    ModelRowBinding rowBinding = ModelRowBinding.inflate(getLayoutInflater(),linearLayout,false);
                    final Month month = alMonth.get(i + j);
                    rowBinding.tvModelName.setText(month.getName());
                    if (!TextUtils.isEmpty(viewModel.getUsedTractor().getModelMonth())) {
                        if (viewModel.getUsedTractor().getModelMonth().equalsIgnoreCase(month.getName())) {
                            rowBinding.ll.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_selector_red));
                        }
                    }
                    rowBinding.getRoot().setOnClickListener(v -> {
                      rowBinding.ll.setBackground(ContextCompat.getDrawable(activity,R.drawable.button_selector_red));
                        viewModel.getUsedTractor().setModelMonth(month.getName());
                        initList();
                        searchMonthData(month.getName());
                    });
                    linearLayout.addView(rowBinding.getRoot());
                }
                binding.llMonth.addView(linearLayout);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void searchMonthData(String Month) {
        if (!Month.isEmpty()) {
          ((SellTractorActivity)activity).callFragments(6);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(5);
    }
}