package com.khetigaadi.tractor.usedTractor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.TractorFilterActivity;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.usedTractor.model.UsedTractorListResp;
import com.khetigaadi.tractor.usedTractor.util.UsedTractor;
import com.khetigaadi.user.LoginActivity;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.util.Utils;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class UsedTractorsListFragment extends Fragment {
    private static final String TAG = UsedTractorsListFragment.class.getSimpleName();
    private UsedTractorListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private String userId = "0";
    @Inject
    SessionManager session;
    @Inject
    KhetiAPIs khetiAPIs;
    private UsedTractorsListViewModel viewModel;
    ActivityResultLauncher<Intent> tractorDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    try {
                        int position = result.getData().getIntExtra("position", -1);
                        UsedTractor tractor = mAdapter.getItem(position);
                        tractor.setFavoritesStatus(result.getData().getBooleanExtra(UsedTractorDetailActivity.isFavourite, false));
                        mAdapter.updateItem(tractor, position);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if(data != null) {
                    viewModel.setFilter(result.getData().getExtras().getParcelable("filter"));
                    mAdapter.itemRemoveAll();
                    getUsedTractorListL(1);
                }
            });
    private RecycleListBinding binding;
    public UsedTractorsListFragment() {
    }
    public static UsedTractorsListFragment getInstance(Filter filter) {
        UsedTractorsListFragment fragment = new UsedTractorsListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("filter",filter);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        try {
            userId = session.getUser().getId();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UsedTractorsListViewModel.class);
        ((MainActivity) activity).setToolbarTitle(getString(R.string.old_tractor));
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding. filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter", viewModel.getFilter());
            intent.putExtra("callFrom", CallFrom.OLD_TRACTOR);
            filterActivityResultLauncher.launch(intent);
        });
        init();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        Log.d("cvxvcxcxvcx====",""+Utils.isConnectionAvailable(activity));
        if(!Utils.isConnectionAvailable(activity)){
            Snackbar.make(binding.myRecyclerView,activity.getResources().getString(R.string.no_internet_connection),
                    Snackbar.LENGTH_LONG).show();
        }
        getUsedTractorList(1);
    }
    private void init() {
        mAdapter = new UsedTractorListAdapter(activity,userId,binding.myRecyclerView,false);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, v) -> {
            try {
                Timber.e("onItemClick");
                Intent intent = new Intent(activity, UsedTractorDetailActivity.class);
                intent.putExtra("tractorId", mAdapter.getItem(position).getId());
                intent.putExtra("position", position);
                tractorDetailsActivityResultLauncher.launch(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mAdapter.setOnFavoritesClickListener((position, view) -> {
            view.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.anim_scale));
            if (session.isLoggedIn()) {
                setMyFavorites(mAdapter.getItem(position),mAdapter.getItem(position).getFavoritesStatus(), position);
            } else {
                startActivity(new Intent(activity,LoginActivity.class));
            }
        });
        mAdapter.setOnSellerDetailsClickListener((position,v) -> {
            try {
                Intent intent = new Intent(activity,GetUsedTrSellerDetails.class);
                intent.putExtra("tractorId",mAdapter.getItem(position).getId());
                startActivity(intent);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        mAdapter.setOnChatClickListener((position, v) -> {
        });
        mAdapter.setOnLoadMoreListener(this::getUsedTractorList);
    }
    private void getUsedTractorList(int currPageNo) {
        viewModel.getUsedTractors(currPageNo).removeObservers(this);
      if(getView()!=null)
        viewModel.getUsedTractors(currPageNo).observe(getViewLifecycleOwner(), usedTractorListRespStateData -> {
            switch (usedTractorListRespStateData.getStatus()) {
                case LOADING:
                    if (currPageNo == 1) {
                       binding. llProgress.setVisibility(View.VISIBLE);
                        binding.filterIcon.setVisibility(View.GONE);
                    }
                    break;
                case SUCCESS:
                    UsedTractorListResp usedTractorListResp = usedTractorListRespStateData.getData();
                    binding. llProgress.setVisibility(View.GONE);
                    binding.filterIcon.setVisibility(View.VISIBLE);
                    if (usedTractorListResp != null) {
                        if (!usedTractorListResp.isError()) {
                            mAdapter.setTotPages(usedTractorListResp.getData().getLastPage());
                            mAdapter.addItems(usedTractorListResp.getData().getData(), currPageNo, usedTractorListResp.getOthers());
                        } else {
                            Toast.makeText(activity, usedTractorListResp.getMsg().get(0), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), usedTractorListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    binding. llProgress.setVisibility(View.GONE);
                    break;
            }
        });

    }
    private void getUsedTractorListL(int currPageNo) {
        viewModel.getUsedTractors(currPageNo).removeObservers(this);
        viewModel.getUsedTractors(currPageNo).observe(this,usedTractorListRespStateData -> {
            switch (usedTractorListRespStateData.getStatus()) {
                case LOADING:
                    if (currPageNo == 1) {
                        binding. llProgress.setVisibility(View.VISIBLE);
                        binding.filterIcon.setVisibility(View.GONE);
                    }
                    break;
                case SUCCESS:
                    UsedTractorListResp usedTractorListResp = usedTractorListRespStateData.getData();
                    binding. llProgress.setVisibility(View.GONE);
                    binding.filterIcon.setVisibility(View.VISIBLE);
                    if (usedTractorListResp != null) {
                        if (!usedTractorListResp.isError()) {
                            mAdapter.setTotPages(usedTractorListResp.getData().getLastPage());
                            mAdapter.addItems(usedTractorListResp.getData().getData(), currPageNo, usedTractorListResp.getOthers());
                        } else {
                            Toast.makeText(activity, usedTractorListResp.getMsg().get(0), Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case ERROR:
                    Toast.makeText(getActivity(), usedTractorListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                    binding. llProgress.setVisibility(View.GONE);
                    break;
            }
        });
    }
    private void setMyFavorites(final UsedTractor usedTractor, final boolean myFavoritesStatus, final int position) {
        viewModel.setUsedTractorFavorites(usedTractor).observe(getViewLifecycleOwner(), response -> {
            switch (response.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    mAdapter.getItem(position).setFavoritesStatus(!mAdapter.getItem(position).getFavoritesStatus());
                    if (response.getData() != null) {
                        Toast.makeText(activity, response.getData().getMsg().get(0), Toast.LENGTH_SHORT).show();
                    }
                    mAdapter.notifyItemChanged(position);
                    break;
                case ERROR:
                    Toast.makeText(activity, response.getError(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
