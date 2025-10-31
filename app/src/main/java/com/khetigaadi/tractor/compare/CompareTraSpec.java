package com.khetigaadi.tractor.compare;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.khetigaadi.databinding.CompareTrSpecificationBinding;
import com.khetigaadi.tractor.compare.model.CompTracSpecific;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Created by Irfan on 2/17/2016.
 */
public class CompareTraSpec extends Fragment {
    private FragmentActivity activity;
    private CompareTractorViewModel viewModel;
    private CompareTrSpecificationBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CompareTrSpecificationBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider((CompareTractorActivity) activity).get(CompareTractorViewModel.class);
        bindData();
    }
    private void bindData() {
        try {
            viewModel.getCompareTractors().observe(getViewLifecycleOwner(), compTractRespStateData -> {
                if (compTractRespStateData.getData() != null) {
                    List<CompTracSpecific> items = compTractRespStateData.getData().getData()
                            .stream()
                            .filter(item -> item.getTractorSpec().size() > 0)
                            .filter(i -> i.getTractorSpec()
                                    .stream()
                                    .anyMatch(s -> s.getCompareTractorSpecData() != null && !TextUtils.isEmpty(s.getCompareTractorSpecData().get(0).getSpecData()) && !TextUtils.isEmpty(s.getCompareTractorSpecData().get(1).getSpecData())))
                            .collect(Collectors.toList());
                    binding.rvSpecification.setLayoutManager(new LinearLayoutManager(activity));
                    CompareTractorDetailSpecAdapter tractorDetailSpecAdapter = new CompareTractorDetailSpecAdapter(activity, items);
                    binding.rvSpecification.setAdapter(tractorDetailSpecAdapter);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
