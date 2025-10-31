package com.khetigaadi.tractor.serviceCenter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.DealerRecycleListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.tractor.TractorFilterActivity;
import com.khetigaadi.tractor.dealer.DealerListViewModel;
import com.khetigaadi.tractor.dealer.DealersListAdapter;
import com.khetigaadi.tractor.dealer.GetDealersDetails;
import com.khetigaadi.user.SessionManager;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class SCenterList extends Fragment {
    private static final String TAG = "SCenterList";
    private DealersListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    SessionManager session;
    private DealerListViewModel viewModel;
    private DealerRecycleListBinding binding;
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    viewModel.setFilter(result.getData().getExtras().getParcelable("filter"));
                    mAdapter.itemRemoveAll();
                    getDealersList(1);
                }
            });
    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DealerRecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view,@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DealerListViewModel.class);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.service_centers));
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.progressBar.ivLoader);
        binding.filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter", viewModel.getFilter());
            intent.putExtra("callFrom", CallFrom.DEALER);
            filterActivityResultLauncher.launch(intent);
        });
        initAdapter();
        getDealersList(1);
    }
    private void initAdapter() {
        mAdapter = new DealersListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnGetDetailsClickListener((position, v) -> {
            Intent intent = new Intent(activity, GetDealersDetails.class);
            intent.putExtra("dealerId", mAdapter.getItem(position).getId());
            intent.putExtra("position", position);
            startActivityForResult(intent, Constants.DEALER_DETAILS_REQUESRT);
        });
        mAdapter.setOnCallClickListener((position, v) -> {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        123);

            } else {
                String[] paths = mAdapter.getItem(position).getContact().split("/");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + paths[0]));
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(this::getDealersList);
    }
    private void getDealersList(int page) {
        viewModel.getDealers(page).observe(getViewLifecycleOwner(), dealerListRespStateData -> {
            switch (dealerListRespStateData.getStatus()) {
                case LOADING:
                    if (page == 1)
                        binding.progressBar.llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.llProgress.setVisibility(View.GONE);
                    if (dealerListRespStateData.getData() != null) {
                        mAdapter.setTotPages(dealerListRespStateData.getData().getData().getLastPage());
                        mAdapter.addItems(dealerListRespStateData.getData().getData().getDealers(), page, dealerListRespStateData.getData().getOthers());
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity, dealerListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == Constants.DEALER_DETAILS_REQUESRT) {
            if (data != null && data.getIntExtra("position",-1) != -1) {
                int position = data.getIntExtra("position",-1);
                // dealerResp.getDealers().get(position).setRequested(true);
                mAdapter.notifyItemChanged(position);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
