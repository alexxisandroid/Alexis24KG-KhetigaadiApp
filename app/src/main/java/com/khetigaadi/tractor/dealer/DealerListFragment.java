package com.khetigaadi.tractor.dealer;

import android.app.Activity;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.databinding.DealerRecycleListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.tractor.TractorFilterActivity;
import com.khetigaadi.user.SessionManager;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * Created by Irfan on 2/6/2016.
 */
@AndroidEntryPoint
public class DealerListFragment extends BaseFragment {
    private static final String TAG = DealerListFragment.class.getSimpleName();
    private DealersListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private int position;
    private DealerRecycleListBinding binding;
    @Inject
    SessionManager session;
    private DealerListViewModel viewModel;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: " + TAG);
        mTracker.setScreenName("Image~" + TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        addFabButtonItems(null);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DealerRecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DealerListViewModel.class);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.dealers));
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.progressBar.ivLoader);
        binding.filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter", viewModel.getFilter());
            intent.putExtra("callFrom", CallFrom.DEALER);
            filterActivityResultLauncher.launch(intent);
        });
        init();
        getDealersList(1);
    }

    private void init() {
        mAdapter = new DealersListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        ActivityResultLauncher<Intent> dealerDetailsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getIntExtra("position", -1) != -1) {
                            position = data.getIntExtra("position", -1);
                            //This code commented by PRANAV
                            mAdapter.notifyItemChanged(position);
                        }
                    }
                });
        mAdapter.setOnGetDetailsClickListener((position, v) -> {
            Intent intent = new Intent(activity, GetDealersDetails.class);
            intent.putExtra("dealerId", mAdapter.getList().get(position).getId());
            intent.putExtra("position", position);
            dealerDetailsResultLauncher.launch(intent);
        });
        mAdapter.setOnCallClickListener((position, v) -> {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, android.Manifest.permission.CALL_PHONE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{android.Manifest.permission.CALL_PHONE},
                        123);

            } else {
                try {
                    String[] paths = mAdapter.getItem(position).getContact().split("/");
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + paths[0]));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mAdapter.setOnLoadMoreListener(this::getDealersList);
    }


    @Override
    protected void addFabButtonItems(ArrayList<RFACLabelItem<Integer>> newItems) {
        ArrayList<RFACLabelItem<Integer>> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.dealership))
                .setResId(R.drawable.ic_dealers_)
                .setLabelColor(ContextCompat.getColor(activity, R.color.black))
                .setIconNormalColor(ContextCompat.getColor(activity, R.color.black))
                .setWrapper(0)
        );
        super.addFabButtonItems(items);
    }

    @Override
    protected void setOnRapidFloatingActionContentLabelListListener(RapidFloatingActionContentLabelList rfaContent) {
        rfaContent.setOnRapidFloatingActionContentLabelListListener(new RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener() {
            @Override
            public void onRFACItemLabelClick(int position, RFACLabelItem item) {
                fabAction(position);
            }

            @Override
            public void onRFACItemIconClick(int position, RFACLabelItem item) {
                fabAction(position);
            }

        });
    }

    @Override
    protected void fabAction(int position) {
        if (position == 2) {
            startActivity(new Intent(getActivity(), DealerShipActivity.class));
            super.rfabHelper.toggleContent();
        } else {
            super.fabAction(position);
        }
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

}
