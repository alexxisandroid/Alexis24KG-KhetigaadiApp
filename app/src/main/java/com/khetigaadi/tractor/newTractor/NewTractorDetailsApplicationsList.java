package com.khetigaadi.tractor.newTractor;

import android.content.Intent;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
import com.khetigaadi.commons.SharedPreferenceUtils;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.implement.newImplement.NewImpOverviewDetails;
import com.khetigaadi.implement.newImplement.NewImplementListAdapter;
import com.khetigaadi.implement.newImplement.util.ImplementListResp;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * Created by Irfan on 2/5/2016.
 */
@AndroidEntryPoint
public class NewTractorDetailsApplicationsList extends Fragment {
    private static final String TAG = "NewTractorDetailsApplic";
    private LinearLayoutManager mLayoutManager;
    private NewImplementListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    SharedPreferenceUtils pref;
    private NewTractorViewModel viewModel;
    private RecycleListBinding binding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: " + TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.filterIcon.setVisibility(View.GONE);
        binding.myRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        binding.myRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider((NewTractorDetails) activity).get(NewTractorViewModel.class);
        initView();
        getImpList(1);
    }

    private void initView() {
        mAdapter = new NewImplementListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(activity, NewImpOverviewDetails.class);
            intent.putExtra("brandId", mAdapter.getItem(position).getBrandId());
            intent.putExtra("typeId", mAdapter.getItem(position).getImplementTypeId());
            intent.putExtra("modelId", mAdapter.getItem(position).getImplementModelId());
            intent.putExtra("position", position);
            startActivity(intent);
        });
        mAdapter.setGetPriceClick((position, v) -> {
            Intent intent = new Intent(activity, NewTrGetOnRoadPriceActivity.class);
            intent.putExtra("id", mAdapter.getItem(position).getId());
            intent.putExtra("name", mAdapter.getItem(position).getImplementName());
            intent.putExtra("image", mAdapter.getOthers().getImplementImagePath() + mAdapter.getItem(position).getImplementImage().getImage());
            intent.putExtra("priceType", PriceType.IMPLEMENT);
            intent.putExtra("toolbarlabel", "Get Price");
            startActivity(intent);
        });
        mAdapter.setOnLoadMoreListener(this::getImpList);
    }

    private void getImpList(int page) {
        viewModel.getImpList(page).observe(getViewLifecycleOwner(),implementListRespStateData -> {
            switch (implementListRespStateData.getStatus()) {
                case LOADING:
                    if (page == 1) {
                        binding.llProgress.setVisibility(View.VISIBLE);
                    }
                    break;
                case SUCCESS:
                    binding.llProgress.setVisibility(View.GONE);
                    ImplementListResp implementListResp = implementListRespStateData.getData();
                    if (implementListResp != null) {
                        mAdapter.setTotPages(implementListResp.getData().getLastPage());
                        mAdapter.addItems(implementListResp.getData().getImps(), page, implementListResp.getOthers());
                    }
                    break;
                case ERROR:
                    binding.llProgress.setVisibility(View.GONE);
                    Toast.makeText(activity, implementListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

}
