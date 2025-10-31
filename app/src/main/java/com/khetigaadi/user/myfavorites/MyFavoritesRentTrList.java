package com.khetigaadi.user.myfavorites;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.tractor.rent.RentTractorDetails;
import com.khetigaadi.tractor.rent.RentTractorSendEnquiry;
import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.myfavorites.model.MyFavNewTractor;
import com.khetigaadi.user.myfavorites.model.MyFavRentTractors;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class MyFavoritesRentTrList extends Fragment {
    private static final String TAG = MyFavoritesRentTrList.class.getSimpleName();
    private MyFavRentTrListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    SessionManager sessionManager;
    private MyFavoritesViewModel viewModel;
    private RecycleListBinding binding;
    public static Fragment getInstance() {
        return new MyFavoritesRentTrList();
    }
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this.getParentFragment()).get(MyFavoritesViewModel.class);
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.myRecyclerView.setHasFixedSize(true);
        init();
        getRentTractorList();
        binding.addIcon.setVisibility(View.GONE);
        binding.filterIcon.setVisibility(View.GONE);
        return binding.getRoot();
    }
    ActivityResultLauncher<Intent> tractorDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    try {
                        int position = result.getData().getIntExtra("position", -1);
                        MyFavRentTractors tractor = mAdapter.getItem(position);
                        tractor.getRentTractor().setBookmark(result.getData().getBooleanExtra(UsedTractorDetailActivity.isFavourite,false));
                        if (tractor.getRentTractor().isBookmark())
                            mAdapter.updateItem(tractor,position);
                        else
                            mAdapter.itemRemove(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    private void init() {
        mAdapter = new MyFavRentTrListAdapter(activity,sessionManager.getUser(),binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoaded();
        mAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(activity, RentTractorDetails.class);
            intent.putExtra("tractorId", mAdapter.getItem(position).getRentTractor().getRentTractorId());
            intent.putExtra("position", position);
            tractorDetailsActivityResultLauncher.launch(intent);
        });
        mAdapter.setOnFavoritesClick((position, view) -> {
            view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_scale));
            setFavorites(mAdapter.getItem(position).getRentTractor());
        });
        mAdapter.setOnSendEnqClickListener((position, v) -> {
            Intent intent = new Intent(activity, RentTractorSendEnquiry.class);
            intent.putExtra("tractorId", mAdapter.getItem(position).getRentTractor().getRentTractorId());
            startActivity(intent);
        });
        viewModel.getRentTractors().observe(getViewLifecycleOwner(), rentTractors -> mAdapter.updateItems(rentTractors));
    }
    private void getRentTractorList() {
        viewModel.getTractors().observe(getViewLifecycleOwner(), newTractorRespStateData -> {
            switch (newTractorRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    if (newTractorRespStateData.getData() != null) {
                        mAdapter.setOthers(newTractorRespStateData.getData().getOthers());
                    }
                    binding.llProgress.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Toast.makeText(activity, newTractorRespStateData.getError(), Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
    private void setFavorites(final RentTractor tractor) {
        viewModel.setRentTractorFavorites(tractor).observe(getViewLifecycleOwner(),response -> {
            switch (response.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (response.getData() != null) {
                        Toast.makeText(activity, response.getData().getMsg().get(0),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity, response.getError(),Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
