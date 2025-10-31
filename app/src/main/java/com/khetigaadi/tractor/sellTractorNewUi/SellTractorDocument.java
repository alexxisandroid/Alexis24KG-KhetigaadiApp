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
import com.khetigaadi.databinding.FragmentSellTractorDocumentBinding;
import com.khetigaadi.retrofit.KhetiAPIs;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SellTractorDocument extends BaseFragment {
    private String noc = "",rc = "",owner = "";
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private FragmentSellTractorDocumentBinding binding;
    private FragmentActivity activity;
    public static Fragment getInstance() {
        return new SellTractorDocument();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentSellTractorDocumentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(activity).get(SellViewModel.class);
        binding.btnNocYes.setOnClickListener(View -> {
            noc = "1";
            binding.btnNocYes.setChecked(true);
            binding.btnNocNo.setChecked(false);
        });
        binding.btnNocNo.setOnClickListener(View -> {
            noc = "0";
            binding.btnNocNo.setChecked(true);
            binding.btnNocYes.setChecked(false);
        });
        binding.btnRcAvail.setOnClickListener(View -> {
            rc = "1";
            binding.btnRcAvail.setChecked(true);
            binding.btnRcNotAvail.setChecked(false);
        });
        binding.btnRcNotAvail.setOnClickListener(View -> {
            rc = "0";
            binding.btnRcNotAvail.setChecked(true);
            binding.btnRcAvail.setChecked(false);
        });
        binding.btnFirstOwner.setOnClickListener(View -> {
            owner = "one";
            binding.btnFirstOwner.setChecked(true);
            binding.btnSecondOwner.setChecked(false);
            binding.btnThirdOwner.setChecked(false);
            binding.btnMoreOwner.setChecked(false);
        });
        binding.btnSecondOwner.setOnClickListener(View -> {
            owner = "two";
            binding.btnFirstOwner.setChecked(false);
            binding.btnSecondOwner.setChecked(true);
            binding.btnThirdOwner.setChecked(false);
            binding.btnMoreOwner.setChecked(false);
        });
        binding.btnThirdOwner.setOnClickListener(View -> {
            owner = "three";
            binding.btnFirstOwner.setChecked(false);
            binding.btnSecondOwner.setChecked(false);
            binding.btnThirdOwner.setChecked(true);
            binding.btnMoreOwner.setChecked(false);
        });
        binding.btnMoreOwner.setOnClickListener(View -> {
            owner = "more_than_three";
            binding.btnFirstOwner.setChecked(false);
            binding.btnSecondOwner.setChecked(false);
            binding.btnThirdOwner.setChecked(false);
            binding.btnMoreOwner.setChecked(true);
        });
        binding.cvDocumentNext.setOnClickListener(View -> {
            if (!noc.isEmpty() && !rc.isEmpty() && !owner.isEmpty()) {
                viewModel.getUsedTractor().setTractorNoc(noc);
                viewModel.getUsedTractor().setRC(rc);
                viewModel.getUsedTractor().setOwner(owner);
                ((SellTractorActivity)activity).callFragments(8);

            } else {
                Toast.makeText(activity,"Please select fields!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(7);
    }
}
