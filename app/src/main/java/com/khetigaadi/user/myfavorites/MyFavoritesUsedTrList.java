package com.khetigaadi.user.myfavorites;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
//import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.tractor.usedTractor.GetUsedTrSellerDetails;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.model.User;
import com.khetigaadi.user.myfavorites.model.MyFavUsedTractor;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@SuppressWarnings("CallToPrintStackTrace")
@AndroidEntryPoint
public class MyFavoritesUsedTrList extends BaseFragment {
    private static final String TAG = MyFavoritesUsedTrList.class.getSimpleName();
    private MyFavUsedTractorListAdapter mAdapter;
    private FragmentActivity activity;
    @SuppressLint("VisibleForTests")
    @Inject
    Tracker mTracker;
    @Inject
    SessionManager sessionManager;
    private User user;
    private MyFavoritesViewModel viewModel;
    private RecycleListBinding binding;
    public static Fragment getInstance() {
        return new MyFavoritesUsedTrList();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        user = sessionManager.getUser();
    }
    @SuppressLint("VisibleForTests")
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater,container,false);
        assert this.getParentFragment() != null;
        viewModel = new ViewModelProvider(this.getParentFragment()).get(MyFavoritesViewModel.class);
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.filterIcon.setVisibility(View.GONE);
        init();
        getUsedTractorList();
        return binding.getRoot();
    }
    ActivityResultLauncher<Intent> tractorDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    try {
                        int position = result.getData().getIntExtra("position", -1);
                        MyFavUsedTractor tractor = mAdapter.getItem(position);
                        tractor.getUsedTractor().setFavoritesStatus(result.getData().getBooleanExtra(UsedTractorDetailActivity.isFavourite, false));
                        if (tractor.getUsedTractor().getFavoritesStatus())
                            mAdapter.updateItem(tractor, position);
                        else
                            mAdapter.itemRemove(position);
                    } catch (Exception e) {
                        //noinspection CallToPrintStackTrace
                        e.printStackTrace();
                    }
                }
            });
    private void init() {
        mAdapter = new MyFavUsedTractorListAdapter(activity, user, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(activity, UsedTractorDetailActivity.class);
            intent.putExtra("tractorId", mAdapter.getItem(position).getUsedTractor().getId());
            intent.putExtra("position", position);
            tractorDetailsActivityResultLauncher.launch(intent);
        });
        mAdapter.setOnFavoritesClickListener((position, view) -> {
            if (mAdapter.getList() != null) {
                view.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.anim_scale));
                setFavorites(mAdapter.getItem(position).getUsedTractor());
            }
        });
        mAdapter.setOnSellerDetailsClickListener((position, v) -> {
            try {
                Intent intent = new Intent(activity, GetUsedTrSellerDetails.class);
                intent.putExtra("tractorId", mAdapter.getItem(position).getUsedTractor().getId());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        viewModel.getUsedTractors().observe(getViewLifecycleOwner(), myFavUsedTractors -> mAdapter.updateItems(myFavUsedTractors));
    }
    @SuppressLint("LogNotTimber")
    private void getUsedTractorList() {
        viewModel.getTractors().observe(getViewLifecycleOwner(),myFavoritesRespStateData -> {
            switch (myFavoritesRespStateData.getStatus()) {
                case LOADING:
                    binding.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    if(myFavoritesRespStateData.getData() != null) {
                        mAdapter.setOthers(myFavoritesRespStateData.getData().getOthers());

                        Log.d("vcxxvxvxvcxv===",""+myFavoritesRespStateData.getData());

                        Log.d("vcxxvxvxvcxv22===",""+myFavoritesRespStateData.getData().getOthers());
                    }
                    binding.llProgress.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Toast.makeText(activity,myFavoritesRespStateData.getError(),Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }
    private void setFavorites(final UsedTractor usedTractor) {
        viewModel.setUsedTractorFavorites(usedTractor).observe(getViewLifecycleOwner(), response -> {
            switch (response.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (response.getData() != null) {
                        Toast.makeText(activity, response.getData().getMsg().get(0), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity, response.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
