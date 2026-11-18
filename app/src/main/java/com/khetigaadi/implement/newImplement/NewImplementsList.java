package com.khetigaadi.implement.newImplement;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
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
import com.khetigaadi.implement.newImplement.util.ImplementListResp;
import com.khetigaadi.model.LinearLayoutManagerWrapper;
import com.khetigaadi.tractor.TractorFilterActivity;
import com.khetigaadi.tractor.newTractor.NewTrGetOnRoadPriceActivity;
import com.khetigaadi.tractor.newTractor.model.Filter;

import java.util.Objects;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NewImplementsList extends Fragment {
    private final String TAG = NewImplementsList.class.getSimpleName();
    private NewImplementListAdapter mAdapter;
   // private FragmentManager mFragmentManager;
    private FragmentActivity activity;
    @SuppressLint("VisibleForTests")
    @Inject
    Tracker mTracker;
    private int position1 = 0;
    private NewImplementListViewModel viewModel;
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    //noinspection deprecation
                    viewModel.setFilter(Objects.requireNonNull(result.getData().getExtras()).getParcelable("filter"));
                    mAdapter.itemRemoveAll();
                    getImpList(1);
                }
            });
    private RecycleListBinding binding;
    public NewImplementsList() {
    }
    public static NewImplementsList getInstance(Filter filter) {
        NewImplementsList fragment = new NewImplementsList();
        Bundle bundle = new Bundle();
        bundle.putParcelable("filter",filter);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @SuppressLint("VisibleForTests")
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(mAdapter != null) {
            mAdapter.notifyItemChanged(position1);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        binding = RecycleListBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(NewImplementListViewModel.class);
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.myRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(activity,LinearLayoutManager.VERTICAL,false);
        binding.myRecyclerView.setLayoutManager(mLayoutManager);
        binding.filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter", viewModel.getFilter());
            intent.putExtra("callFrom", CallFrom.IMPLEMENT);
            filterActivityResultLauncher.launch(intent);
        });
        initView();
        getImpList(1);
        return view;
    }
    private void initView() {
        mAdapter = new NewImplementListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, v) -> {
            position1 = position;
            Intent intent = new Intent(activity, NewImpOverviewDetails.class);
            intent.putExtra("brandId", Objects.requireNonNull(mAdapter.getItem(position)).getBrandId()/*implementListResp.getImplementArrayList().get(position).getBrandId()*/);
            intent.putExtra("typeId", Objects.requireNonNull(mAdapter.getItem(position)).getImplementTypeId()/*implementListResp.getImplementArrayList().get(position).getImplementTypeId()*/);
            intent.putExtra("modelId", Objects.requireNonNull(mAdapter.getItem(position)).getImplementModelId()/*implementListResp.getImplementArrayList().get(position).getImplementModelId()*/);
            intent.putExtra("position", position);
            startActivity(intent);
        });
        mAdapter.setGetPriceClick((position, v) -> {
            String imgVall = "";
            if(Objects.requireNonNull(mAdapter.getItem(position)).getImplementImage()!=null)
               imgVall = Objects.requireNonNull(mAdapter.getItem(position)).getImplementImage().getImage();
            Intent intent = new Intent(activity,NewTrGetOnRoadPriceActivity.class);
            intent.putExtra("id", Objects.requireNonNull(mAdapter.getItem(position)).getId());
            intent.putExtra("name", Objects.requireNonNull(mAdapter.getItem(position)).getImplementName());
            intent.putExtra("image", Objects.requireNonNull(mAdapter.getOthers()).getImplementImagePath() + imgVall);
            intent.putExtra("priceType",PriceType.IMPLEMENT);
            intent.putExtra("toolbarlabel","Get Price");
            startActivity(intent);
        });
        mAdapter.setOnLoadMoreListener(this::getImpList);
    }
    private void getImpList(int page) {
       try {
           viewModel.getImpList(page).observe(getViewLifecycleOwner(), implementListRespStateData -> {
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
                       ImplementListResp implementListResp = implementListRespStateData.getData();
                       if(implementListResp != null) {
                           mAdapter.setTotPages(implementListResp.getData().getLastPage());
                           mAdapter.addItems(implementListResp.getData().getImps(), page, implementListResp.getOthers());
                           // Log.d("bcvbvbvbbvcvbcv1234===",""+implementListResp.getData().getImps());
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
       catch(Exception e) {
           //noinspection CallToPrintStackTrace
           e.printStackTrace();
       }
    }
}
