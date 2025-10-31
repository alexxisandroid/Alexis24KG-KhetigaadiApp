package com.khetigaadi.tractor.compare;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.khetigaadi.base.util.StateData;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.tractor.compare.model.CompTractResp;
import com.khetigaadi.tractor.compare.util.CompareTraImage;
import com.khetigaadi.tractor.newTractor.model.NewTractor;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by Irfan on 2/18/2016.
 */
@AndroidEntryPoint
public class CompareTraImages extends Fragment {
    private LinearLayoutManager mLayoutManager;
    private CompareTraImagesAdapter mAdapter;
    private ArrayList<CompareTraImage> imagesList;
    private FragmentActivity activity;
    @Inject
    SharedPreferenceUtils pref;
    private CompareTractorViewModel viewModel;
    private RecycleListBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider((CompareTractorActivity) activity).get(CompareTractorViewModel.class);
        binding.filterIcon.setVisibility(View.GONE);
        binding.myRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(activity);
        binding.myRecyclerView.setLayoutManager(mLayoutManager);
        imagesList = new ArrayList<>();
        try {

            viewModel.getCompareTractors().observe(getViewLifecycleOwner(), compTractRespStateData -> {
                if (compTractRespStateData.getStatus() == StateData.Status.SUCCESS) {
                    CompTractResp data = compTractRespStateData.getData();
                    NewTractor tractor1JsonObject = null;
                    if (data != null) {
                        tractor1JsonObject = data.getOthers().getTractorData().get(0);
                        NewTractor tractor2JsonObject = data.getOthers().getTractorData().get(1);
                        if (tractor1JsonObject.getImage() != null && tractor2JsonObject.getImage() != null) {
                            imagesList.add(new CompareTraImage(pref.getTractorImagePath() + tractor1JsonObject.getImage().getTractor_img_name(), pref.getTractorImagePath() + tractor2JsonObject.getImage().getTractor_img_name()));
                        }
                        mAdapter = new CompareTraImagesAdapter(activity, imagesList);
                        binding.myRecyclerView.setAdapter(mAdapter);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

}
