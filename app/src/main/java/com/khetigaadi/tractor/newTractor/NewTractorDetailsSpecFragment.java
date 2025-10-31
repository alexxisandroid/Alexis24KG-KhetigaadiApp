package com.khetigaadi.tractor.newTractor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.databinding.NewTractorSpecDetailsBinding;
import com.khetigaadi.tractor.newTractor.model.NewTrSpecList;
import com.khetigaadi.tractor.newTractor.model.NewTrSpecResp;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
/**
 * Created by Irfan on 2/4/2016.
 */
@AndroidEntryPoint
public class NewTractorDetailsSpecFragment extends Fragment {
    private FragmentActivity activity;
    @Inject
    SharedPreferenceUtils pref;
    public TractorDetailSpecAdapter tractorDetailSpecAdapter;
    private NewTractorSpecDetailsBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = NewTractorSpecDetailsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NewTractorViewModel viewModel = new ViewModelProvider((NewTractorDetails) activity).get(NewTractorViewModel.class);
        viewModel.getSpecificationDetails().observe(getViewLifecycleOwner(), newTrSpecRespStateData -> {
            switch (newTrSpecRespStateData.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (newTrSpecRespStateData.getData() != null) {
                        setData(newTrSpecRespStateData.getData());
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity, newTrSpecRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    private void setData(NewTrSpecResp newTrSpecResp) {
        try {
            List<NewTrSpecList> items = newTrSpecResp.getData()
                    .stream()
                    .filter(item -> {
                        if (item.getTractorSpec().size() <= 0) {
                            return false;
                        } else {
                            for (int i = 0; i < item.getTractorSpec().size(); i++) {
                                if (!TextUtils.isEmpty(item.getTractorSpec().get(i).getTractorSpecData().getSpecData()))
                                    return true;
                            }
                            return false;
                        }
                    })
                    .filter(i -> i.getTractorSpec()
                            .stream()
                            .anyMatch(s -> s.getTractorSpecData() != null && s.getTractorSpecData().getSpecData() != null))
                    .collect(Collectors.toList());
            binding.rvSpecification.setLayoutManager(new LinearLayoutManager(activity));
            tractorDetailSpecAdapter = new TractorDetailSpecAdapter(activity, items);
            binding.rvSpecification.setAdapter(tractorDetailSpecAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
