package com.khetigaadi.tractor.rent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
//import com.khetigaadi.base.model.RentTractor;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.tractor.TractorFilterActivity;
//import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.tractor.rent.model.RentTractor;
import com.khetigaadi.tractor.rent.model.RentTractorResp;
import com.khetigaadi.tractor.rent.rentTractorNewUi.RentTractorBrand;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.user.LoginActivity;
import com.khetigaadi.user.SessionManager;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class RentTractorListFragment extends Fragment {
    public static final String TAG = RentTractorListFragment.class.getSimpleName();
    private RentTrListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    SessionManager session;
    private RentTractorListViewModel viewModel;
    private RecycleListBinding binding;
    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = getActivity();
        try {
            ((MainActivity) activity).setToolbarTitle(getString(R.string.rentTractor));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.hideKeyboard(activity);
        Timber.i("Setting screen name: " + TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    try {
                        viewModel.setFilter(result.getData().getExtras().getParcelable("filter"));
                        mAdapter.itemRemoveAll();
                        getRentTractorList(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
         binding = RecycleListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RentTractorListViewModel.class);
        ((MainActivity) activity).hideSoftKeyboard();
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into( binding.ivLoader);
        binding. myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.  myRecyclerView.setLayoutManager(mLayoutManager);
        binding.myRecyclerView.setHasFixedSize(true);
        binding. addIcon.setVisibility(View.VISIBLE);
        binding.addIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, RentTractorBrand.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        binding. filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter",viewModel.getFilter());
            intent.putExtra("callFrom",CallFrom.RENT_TRACTOR);
            filterActivityResultLauncher.launch(intent);
        });
        init();
        if(Constants.isConnectionAvailable(activity)) {
            getRentTractorList(1);
        }
        else {
            ((MainActivity) activity).noInternetConnection();
        }
        return  binding.getRoot();
    }
    ActivityResultLauncher<Intent> tractorDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if(data != null) {
                    try {
                        int position = result.getData().getIntExtra("position", -1);
                        com.khetigaadi.tractor.rent.model.RentTractor tractor = mAdapter.getItem(position);
                        tractor.setBookmark(result.getData().getBooleanExtra(UsedTractorDetailActivity.isFavourite, false));
                        mAdapter.updateItem(tractor, position);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    private void init() {
        mAdapter = new RentTrListAdapter(activity, session.getUser(),binding.myRecyclerView);
        binding. myRecyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL, false));
        binding. myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position,v) -> {
            Intent intent = new Intent(activity,RentTractorDetails.class);
            intent.putExtra("tractorId",mAdapter.getItem(position).getRentTractorId());
            intent.putExtra("position",position);
            tractorDetailsActivityResultLauncher.launch(intent);
        });
        mAdapter.setOnFavoritesClick((position,view) -> {
            view.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.anim_scale));
            if(session.isLoggedIn()) {
                setMyFavorites(mAdapter.getItem(position),mAdapter.getItem(position).isBookmark(), position);
            }
            else {
                startActivity(new Intent(activity,LoginActivity.class));
            }
        });
        mAdapter.setOnSendEnqClickListener((position, v) -> {
            Intent intent = new Intent(activity,RentTractorSendEnquiry.class);
            intent.putExtra("tractorId",mAdapter.getItem(position).getRentTractorId());
            startActivity(intent);
        });
        mAdapter.setOnLoadMoreListener(this::getRentTractorList);
    }
    private void getRentTractorList(int page) {
     try {
         viewModel.getRentTractors(page).observe(getViewLifecycleOwner(), tractorsResp -> {
             switch (tractorsResp.getStatus()) {
                 case LOADING:
                     if (page == 1) {
                         binding.llProgress.setVisibility(View.VISIBLE);
                         binding.addIcon.setVisibility(View.GONE);
                     }
                     break;
                 case SUCCESS:
                     RentTractorResp rentTractorResp = tractorsResp.getData();
                     binding.llProgress.setVisibility(View.GONE);
                     binding.addIcon.setVisibility(View.VISIBLE);
                     binding.filterIcon.setVisibility(View.VISIBLE);
                     if (rentTractorResp != null) {
                         if (!rentTractorResp.isError()) {
                             mAdapter.setTotPages(rentTractorResp.getData().getLastPage());
                             mAdapter.addItems(rentTractorResp.getData().getData(), page, rentTractorResp.getOthers());
                         } else {
                             Toast.makeText(activity, rentTractorResp.getMsg().get(0), Toast.LENGTH_SHORT).show();
                         }
                     }
                     break;
                 case ERROR:
                     Toast.makeText(activity, tractorsResp.getError(), Toast.LENGTH_SHORT).show();
                     mAdapter.addItems(new ArrayList<>(), page);
                     binding.llProgress.setVisibility(View.GONE);
                     binding.addIcon.setVisibility(View.VISIBLE);
                     break;
             }
         });
     }
     catch (Exception e) {
         e.printStackTrace();
     }
    }
    private void setMyFavorites(final RentTractor rentTractor,final boolean favoritesStatus,final int position) {
        viewModel.setRentTractorFavorites(rentTractor).observe(getViewLifecycleOwner(),myResponseStateData -> {
            switch (myResponseStateData.getStatus()) {
                case ERROR:
                    Toast.makeText(activity,myResponseStateData.getError(),Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    mAdapter.getItem(position).setBookmark(!mAdapter.getItem(position).isBookmark());
                    mAdapter.notifyItemChanged(position);
                    break;
            }
        });
    }
}
