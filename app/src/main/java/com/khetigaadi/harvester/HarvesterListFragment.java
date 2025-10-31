package com.khetigaadi.harvester;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.harvester.model.HarvesterListResp;
import com.khetigaadi.model.LinearLayoutManagerWrapper;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.TractorFilterActivity;
import com.khetigaadi.tractor.newTractor.NewTrGetOnRoadPriceActivity;
import com.khetigaadi.tractor.newTractor.model.Filter;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class HarvesterListFragment extends Fragment {
    private static final String TAG = "HarvesterListFragment";
    private HarvesterListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    KhetiAPIs khetiAPIs;
    private int position1 = 0;
    private HarvesterListViewModel viewModel;
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    viewModel.setFilter(result.getData().getExtras().getParcelable("filter"));
                    mAdapter.itemRemoveAll();
                    getImpList(1);
                }
            });
    private RecycleListBinding binding;
    public HarvesterListFragment() {
    }
    public static HarvesterListFragment getInstance(Filter filter) {
        HarvesterListFragment fragment = new HarvesterListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("filter", filter);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if (mAdapter != null) {
            mAdapter.notifyItemChanged(position1);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(HarvesterListViewModel.class);
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.myRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(activity, LinearLayoutManager.VERTICAL, false);
        binding.myRecyclerView.setLayoutManager(mLayoutManager);
        binding.filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter", viewModel.getFilter());
            intent.putExtra("callFrom", CallFrom.HARVESTER);
            filterActivityResultLauncher.launch(intent);
        });
        initView();
        getImpList(1);
        return view;
    }
    private void initView() {
        mAdapter = new HarvesterListAdapter(activity, binding.myRecyclerView, false);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, v, harvester) -> {
            position1 = position;
            Intent intent = new Intent(activity, HarvesterDetailsActivity.class);
            intent.putExtra("harvesterId", mAdapter.getItem(position).getHarvesterId());
            intent.putExtra("position", position);
            startActivity(intent);
        });
        mAdapter.setGetPriceClick((position, v) -> {
            Intent intent = new Intent(activity, NewTrGetOnRoadPriceActivity.class);
            intent.putExtra("id", mAdapter.getItem(position).getHarvesterId());
            intent.putExtra("name", mAdapter.getItem(position).getName());
            intent.putExtra("image", mAdapter.getOthers().getHarvesterImagePath() + mAdapter.getItem(position).getHarvesterImage().getName());
            intent.putExtra("priceType", PriceType.HARVESTER);
            startActivity(intent);
        });
        mAdapter.setOnLoadMoreListener(this::getImpList);
    }
    private void getImpList(int page) {
        viewModel.getImpList(page).observe(this.activity, implementListRespStateData -> {
            switch (implementListRespStateData.getStatus()) {
                case LOADING:
                    if (page == 1) {
                        binding.llProgress.setVisibility(View.VISIBLE);
                        binding.filterIcon.setVisibility(View.GONE);
                    }
                    break;
                case SUCCESS:
                    binding.llProgress.setVisibility(View.GONE);
                    binding.filterIcon.setVisibility(View.VISIBLE);
                    HarvesterListResp implementListResp = implementListRespStateData.getData();
                    if (implementListResp != null) {
                        mAdapter.setTotPages(implementListResp.getData().getLastPage());
                        mAdapter.addItems(implementListResp.getData().getData(), page, implementListResp.getOthers());
                    }
                    break;
                case ERROR:
                    binding.llProgress.setVisibility(View.GONE);
                    binding.filterIcon.setVisibility(View.VISIBLE);
                    Toast.makeText(activity, implementListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
