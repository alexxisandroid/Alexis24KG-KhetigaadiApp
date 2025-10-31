package com.khetigaadi.tractor.compare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.CompareTractorBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.compare.model.CompTractResp;
import com.khetigaadi.tractor.newTractor.model.NewTractor;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * Created by Ratan on 7/27/2015.
 */
@AndroidEntryPoint
public class CompareTractorTab extends Fragment {
    public static int int_items = 3;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    SharedPreferenceUtils pref;
    @Inject
    KhetiAPIs khetiAPIs;
    private CompareTractorViewModel viewModel;
    private CompareTractorBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CompareTractorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider((CompareTractorActivity) activity).get(CompareTractorViewModel.class);
        if (Constants.isConnectionAvailable(activity)) {
            getCompareTractorDetails();
        } else {
            ((MainActivity) activity).noInternetConnection();
        }
    }

    private void getCompareTractorDetails() {
        // Tag used to cancel the request
        viewModel.getCompare().observe(activity, compTractRespStateData -> {
            switch (compTractRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.llProgress.setVisibility(View.GONE);
                    if (compTractRespStateData.getData() != null) {
                        pref.setTractorImagePath(compTractRespStateData.getData().getOthers().getTractorImagePath());
                        bindData(compTractRespStateData.getData());
                        binding.viewPager.setAdapter(new CompareTractorTab.MyAdapter(getChildFragmentManager()));
                        binding.tabs.post(() -> binding.tabs.setupWithViewPager(binding.viewPager));
                    }
                    break;
                case ERROR:
                    binding.llProgress.setVisibility(View.GONE);
                    Toast.makeText(activity, compTractRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    activity.finish();
                    break;
            }
        });
    }

    private void bindData(CompTractResp data) {
        NewTractor tractorsJsonArray1;
        NewTractor tractorsJsonArray2;
        try {

            tractorsJsonArray1 = data.getOthers().getTractorData().get(0);
            tractorsJsonArray2 = data.getOthers().getTractorData().get(1);
            Glide.with(activity)
                    .load(pref.getTractorImagePath() + tractorsJsonArray1.getImage().getTractor_img_name())
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(binding.ivNewTractor1Image);
            Glide.with(activity)
                    .load(pref.getTractorImagePath() + tractorsJsonArray2.getImage().getTractor_img_name())
                    .placeholder(R.mipmap.no_image)
                    .error(R.mipmap.no_image)
                    .into(binding.ivNewTractor2Image);

            binding.tvTractor1Name.setText(tractorsJsonArray1.getName());
            binding.tvTractor2Name.setText(tractorsJsonArray2.getName());
            binding.tvTractor1Hp.setText(String.format("%s %s", tractorsJsonArray1.getExactHp(), getString(R.string.hp)));
            binding.tvTractor2Hp.setText(String.format("%s %s", tractorsJsonArray2.getExactHp(), getString(R.string.hp)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class MyAdapter extends FragmentStatePagerAdapter {
        private MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public @NotNull Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new CompareTraSpec();
                case 2:
                    return new CompareTraImages();
                default:
                    return new CompareTraOverview();
            }
        }

        @Override
        public int getCount() {
            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return activity.getString(R.string.overview);
                case 1:
                    return activity.getString(R.string.specification);
                case 2:
                    return activity.getString(R.string.images);
                default:
                    return activity.getString(R.string.overview);

            }
        }

    }

}
