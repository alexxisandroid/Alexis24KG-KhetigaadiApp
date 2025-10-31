package com.khetigaadi.tractor.sellTractorNewUi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.databinding.FragmentSellTractorPriceBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.UsedTractorRepository;
import com.khetigaadi.tractor.sellTractorNewUi.fileuploadservice.FileUploadService;
import com.khetigaadi.tractor.sellTractorNewUi.model.TractorImages;
import com.khetigaadi.user.myUsedTractor.MyUploadedUsedTractorList;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SellTractorPrice extends BaseFragment {
    private SellViewModel viewModel;
    @Inject
    KhetiAPIs khetiAPIs;
    private FragmentSellTractorPriceBinding binding;
    private FragmentActivity activity;
    @Inject
    UsedTractorRepository usedTractorRepository;

    public static Fragment getInstance() {
        return new SellTractorPrice();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSellTractorPriceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(activity).get(SellViewModel.class);
        binding.cvUpdateSellTractor.setOnClickListener(View -> {
            if (!binding.etRegistrationNo.getText().toString().isEmpty() && !binding.etExpectedPrice.getText().toString().isEmpty()) {
                viewModel.getUsedTractor().setRegistrationNo(binding.etRegistrationNo.getText().toString());
                viewModel.getUsedTractor().setPrice(binding.etExpectedPrice.getText().toString());
                viewModel.getUsedTractor().setAbout(binding.etAboutUs.getText().toString());
                saveSellTractorData();
            } else {
                Toast.makeText(activity, "Please select fields!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


    private void saveSellTractorData() {
        viewModel.interTractor().observe(getViewLifecycleOwner(), myResponseStateData -> {
            switch (myResponseStateData.getStatus()) {
                case LOADING -> showProgressDialog(getString(R.string.please_wait));
                case SUCCESS -> {
                    dismissProgressDialog();
                    if (myResponseStateData.getData() != null) {
                        TractorImages images = viewModel.getImages();
                        images.setUsedTractorId(Integer.parseInt(myResponseStateData.getData().getData().getId()));
                        usedTractorRepository.insertTractor(images);
                        @SuppressLint("RestrictedApi") WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(FileUploadService.class)
                                .setInputData(new Data.Builder()
                                        .putInt(FileUploadService.ID, images.getUsedTractorId())
                                        .putString(FileUploadService.RIGHT, images.getRightImage().getPath())
                                        .putString(FileUploadService.FRONT_SIDE, images.getFrontSideImage().getPath())
                                        .putString(FileUploadService.LEFT, images.getLeftImage() != null ? images.getLeftImage().getPath() : null)
                                        .putString(FileUploadService.BACK, images.getBackSideImage() != null ? images.getBackSideImage().getPath() : null)
                                        .putString(FileUploadService.FRONT_TYRE, images.getFrontTyreImage() != null ? images.getFrontTyreImage().getPath() : null)
                                        .putString(FileUploadService.BACK_TYRE, images.getBackTyreImage() != null ? images.getBackTyreImage().getPath() : null)
                                        .build())
                                .build();
                        WorkManager
                                .getInstance(activity.getApplicationContext())
                                .enqueue(uploadWorkRequest);


                        activity.finish();
                        Intent intent = new Intent("anEvent");
                        intent.putExtra("key", MyUploadedUsedTractorList.TAG);
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    }
                }
                case ERROR -> {
                    dismissProgressDialog();
                    Toast.makeText(activity, myResponseStateData.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        ((SellTractorActivity) activity).iconColorChange(9);
    }
}
