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

import com.khetigaadi.base.util.StateData;
import com.khetigaadi.databinding.CompareTrOverviewBinding;
import com.khetigaadi.tractor.compare.model.CompTractResp;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorShortSpec;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;


/**
 * Created by Irfan on 2/17/2016.
 */
public class CompareTraOverview extends Fragment {
    private JSONArray tr1Color;
    private FragmentActivity activity;
    private JSONArray tr2Color;
    private boolean isLabelTractor1Unavailable25 = true;
    private boolean isLabelTractor1Unavailable26 = true;
    private boolean isLabelTractor2Unavailable25 = true;
    private boolean isLabelTractor2Unavailable26 = true;
    private CompareTractorViewModel viewModel;
    private CompareTrOverviewBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = CompareTrOverviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        activity = getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(activity).get(CompareTractorViewModel.class);
        bindData();
    }

    private void bindData() {
        try {
            viewModel.getCompareTractors().observe(getViewLifecycleOwner(), compTractRespStateData -> {
                try {
                    if (compTractRespStateData.getStatus() == StateData.Status.SUCCESS) {
                        CompTractResp data = compTractRespStateData.getData();
                        NewTractor firstTr = null;
                        if (data != null) {
                            firstTr = data.getOthers().getTractorData().get(0);
                            NewTractor secondTr = data.getOthers().getTractorData().get(1);
                            if (firstTr.getTractorModel() == null && secondTr.getTractorModel() == null) {
                                binding.llModel.setVisibility(View.GONE);
                            } else {
                                binding.tvModel.setText(firstTr.getTractorModel().getName());
                                binding.tvModel1.setText(secondTr.getTractorModel().getName());
                            }
                            if (firstTr.getDrive() == null && secondTr.getDrive() == null) {
                                binding.llDrive.setVisibility(View.GONE);
                            } else {
                                binding.tvDrive.setText(firstTr.getDrive().getName());
                                binding.tvDrive1.setText(secondTr.getDrive().getName());
                            }
                            binding.llColor.setVisibility(View.GONE);
            /* Temporarly commented by Pranav
            tr1Color = tractor1JsonObject.getJSONArray("color");
            tr2Color = tractor2JsonObject.getJSONArray("color");
            if (tractor1JsonObject.getJSONArray("color").get(0).toString().isEmpty() && tractor2JsonObject.getJSONArray("color").get(0).toString().isEmpty()) {
                llColor.setVisibility(View.GONE);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                params.setMargins(2, 2, 2, 2);
                if (!tractor1JsonObject.getJSONArray("color").get(0).toString().isEmpty()) {
                    int tr1colorLen = tr1Color.length();

                    for (int i = 0; i < tr1colorLen; i++) {
                        TextView tv = new TextView(activity);
                        tv.setText("     ");
                        tv.setLayoutParams(params);
                        tv.setBackgroundColor(Color.parseColor(tr1Color.get(i).toString()));
                        llTr1Color.addView(tv);
                    }
                }
                if (!tractor2JsonObject.getJSONArray("color").get(0).toString().isEmpty()) {
                    int tr2colorLen = tr2Color.length();
                    for (int i = 0; i < tr2colorLen; i++) {
                        TextView tv = new TextView(activity);
                        tv.setText("     ");
                        tv.setLayoutParams(params);
                        tv.setBackgroundColor(Color.parseColor(tr2Color.get(i).toString()));
                        llTr2Color.addView(tv);
                    }
                }
            }*/
                            if (firstTr.getExactHp() == null && secondTr.getExactHp() == null) {
                                binding.llHP.setVisibility(View.GONE);
                            } else {
                                binding.tvHp.setText(firstTr.getExactHp());
                                binding.tvHp1.setText(secondTr.getExactHp());
                            }
                            if (firstTr.getGetShortSpecData() != null
                                    && secondTr.getGetShortSpecData() != null) {
                                for (int i = 0; i < firstTr.getGetShortSpecData().size(); i++) {
                                    NewTractorShortSpec jsonObjectTractor1SpecId = firstTr.getGetShortSpecData().get(i);
                                    int tractor1SpecId = jsonObjectTractor1SpecId.getTractorSpecId();
                                    switch (tractor1SpecId) {
                                        case 25:
                                            if (!TextUtils.isEmpty(jsonObjectTractor1SpecId.getSpecData())) {
                                                binding.tvPTOHp.setText(jsonObjectTractor1SpecId.getSpecData());
                                                isLabelTractor1Unavailable25 = false;
                                            }
                                            break;
                                        case 26:
                                            if (!TextUtils.isEmpty(jsonObjectTractor1SpecId.getSpecData())) {
                                                binding.tvPTORpm.setText(jsonObjectTractor1SpecId.getSpecData());
                                                isLabelTractor1Unavailable26 = false;
                                            }
                                            break;
                                    }
                                }
                                if (isLabelTractor1Unavailable25) {
                                    binding.tvPTOHp.setText("-");
                                }
                                if (isLabelTractor1Unavailable26) {
                                    binding.tvPTORpm.setText("-");
                                }

                                for (int i = 0; i < secondTr.getGetShortSpecData().size(); i++) {
                                    NewTractorShortSpec jsonObjectTractor2SpecId = secondTr.getGetShortSpecData().get(i);
                                    int tractor2SpecId = jsonObjectTractor2SpecId.getTractorSpecId();
                                    switch (tractor2SpecId) {
                                        case 25:
                                            if (!jsonObjectTractor2SpecId.getSpecData().isEmpty()) {
                                                binding.tvPTOHp1.setText(jsonObjectTractor2SpecId.getSpecData());
                                                isLabelTractor2Unavailable25 = false;
                                            }
                                            break;
                                        case 26:
                                            if (!jsonObjectTractor2SpecId.getSpecData().isEmpty()) {
                                                binding.tvPTORpm1.setText(jsonObjectTractor2SpecId.getSpecData());
                                                isLabelTractor2Unavailable26 = false;
                                            }
                                            break;
                                    }
                                }
                                if (isLabelTractor2Unavailable25) {
                                    binding.tvPTOHp1.setText("-");
                                }
                                if (isLabelTractor2Unavailable26) {
                                    binding.tvPTORpm1.setText("-");
                                }
                            } else {
                                binding.llPTOHp.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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
