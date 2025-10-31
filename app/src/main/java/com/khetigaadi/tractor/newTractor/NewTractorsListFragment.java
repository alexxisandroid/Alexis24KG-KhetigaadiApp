package com.khetigaadi.tractor.newTractor;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.fragments.BaseFragment;
import com.khetigaadi.commons.model.BrandResp;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.CallFrom;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.TractorFilterActivity;
import com.khetigaadi.tractor.newTractor.model.Filter;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.newTractor.model.NewTractorResp;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.user.SessionManager;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class NewTractorsListFragment extends BaseFragment {
    private static final String TAG = NewTractorsListFragment.class.getSimpleName();
    private NewTractorListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    SessionManager session;
    public int position1 = 0;
    private NewTractorsListViewModel viewModel;
    private RecycleListBinding binding;
    public static Filter filters;
    public NewTractorsListFragment() {
    }
    public static NewTractorsListFragment getInstance(Filter filter) {
        NewTractorsListFragment newTractorsListFragment = new NewTractorsListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("filter",filter);
        newTractorsListFragment.setArguments(bundle);
         filters = filter;
        Log.d("vbvcbvcbcb====",""+filters);
        return newTractorsListFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        if(mAdapter != null) {
            mAdapter.notifyItemChanged(position1);
        }
    }
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,final ViewGroup container,Bundle savedInstanceState)
    {
        binding = RecycleListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NewTractorsListViewModel.class);
        if(filters!=null)
            viewModel.setFilter(filters);
        Log.d("vcbvbvcvbc====",""+"filters.getBrands().toString()"+"=="+viewModel.getFilter());
        ((MainActivity) activity).setToolbarTitle(getString(R.string.new_tractor));
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.filterIcon.setOnClickListener(v -> {
            Intent intent = new Intent(activity, TractorFilterActivity.class);
            intent.putExtra("filter", viewModel.getFilter());
            intent.putExtra("callFrom", CallFrom.NEW_TRACTOR);
            filterActivityResultLauncher.launch(intent);

        });
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        init();
    }
    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),result -> {
                Intent data = result.getData();
              //  Log.d("gfhdfgdfgdf===",data+"=="+result.getData().getExtras().getParcelable("filter"));
                if (data != null) {
                    viewModel.setFilter(result.getData().getExtras().getParcelable("filter"));
                    mAdapter.itemRemoveAll();
                    getNewTractorList(1);
                }
            });
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(Constants.isConnectionAvailable(activity)) {
            Log.d("vcxcxvxvc====",""+viewModel.getFilter());
            getNewTractorList(1);
        }
        else {
            ((MainActivity)activity).noInternetConnection();
        }
    }
    ActivityResultLauncher<Intent> tractorDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                Log.d("vcbvcbvcbcvb=====","");
                if (data != null) {
                    try {
                        Bundle extras = result.getData().getExtras();
                        int position = extras.getInt("position",-1);
                        NewTractor tractor = mAdapter.getItem(position);
                        tractor.setBookmark(extras.getBoolean(UsedTractorDetailActivity.isFavourite, false));
                        mAdapter.updateItem(tractor, position);
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    private void init() {
        mAdapter = new NewTractorListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NewTractorListAdapter.Listener() {
            @Override
            public void onClick(int position,@NotNull View view) {
                position1 = position;
                Intent intent = new Intent(activity,NewTractorDetails.class);
                intent.putExtra("position",position);
                intent.putExtra(NewTractorDetails.newTractorId,mAdapter.getItem(position).getId());
                tractorDetailsActivityResultLauncher.launch(intent);
            }
            @Override
            public void onFavoriteClick(int position,@NotNull View view) {
                view.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.anim_scale));
                setMyFavorites(mAdapter.getItem(position),position);
            }
            @Override
            public void onGetRoadPriceClick(int position,@NotNull View view) {
                Intent intent = new Intent(activity, NewTrGetOnRoadPriceActivity.class);
                intent.putExtra("id", mAdapter.getItem(position).getId());
                intent.putExtra("name", mAdapter.getItem(position).getName());
                if (mAdapter.getItem(position).getImage() != null)
                    intent.putExtra("image", mAdapter.getOthers().getTractorImagePath() + mAdapter.getItem(position).getImage().getTractor_img_name());
                intent.putExtra("priceType", PriceType.TRACTOR);
                startActivity(intent);
            }
            @Override
            public void onGetDemoClick(int position,@NotNull View view) {
                Intent intent = new Intent(activity, NewTractorRequestForDemo.class);
                intent.putExtra("id", mAdapter.getItem(position).getId());
                intent.putExtra("name", mAdapter.getItem(position).getName());
                intent.putExtra("image", mAdapter.getOthers().getTractorImagePath() + mAdapter.getItem(position).getImage().getTractor_img_name());
                intent.putExtra("demoType", PriceType.TRACTOR);
                startActivity(intent);
            }
        });
        mAdapter.setOnLoadMoreListener(this::getNewTractorList);
    }
    private void getNewTractorList(int page) {
        viewModel.setFilter(filters);
        viewModel.getNewTractors(page).observe(getViewLifecycleOwner(),newTractorRespStateData -> {
           Log.d("vcbcbcvbvcb====",""+newTractorRespStateData.getStatus()+"=="+viewModel.getFilter());
            switch(newTractorRespStateData.getStatus()) {
               case LOADING:
                    if(page == 1) {
                        binding.llProgress.setVisibility(View.VISIBLE);
                        binding.filterIcon.setVisibility(View.GONE);
                    }
                    break;
               case SUCCESS:
                    NewTractorResp newTractorResp = newTractorRespStateData.getData();
                    Log.d("vcbcvbcvbccxvcx===",""+newTractorResp);
                    binding.llProgress.setVisibility(View.GONE);
                    binding.filterIcon.setVisibility(View.VISIBLE);
                    if(newTractorResp != null) {
                        mAdapter.setTotPages(newTractorResp.getData().getLastPage());
                        mAdapter.addItems(addSponsoredItems(newTractorResp), page, newTractorResp.getOthers());
                        Log.d("vcbcvbcvbc11===",""+newTractorResp.getData().getLastPage());
                        Log.d("vcbcvbcvbc22===",""+addSponsoredItems(newTractorResp).size()+"=="+page+"=="+newTractorResp.getOthers());
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity,newTractorRespStateData.getError(),Toast.LENGTH_SHORT).show();
                    mAdapter.addItems(new ArrayList<>(), page);
                    binding.llProgress.setVisibility(View.GONE);
                    binding.filterIcon.setVisibility(View.VISIBLE);
                    Log.d("cvbcvbcvbvbcbfgfg===",""+newTractorRespStateData.getError());
                    break;
            }
        });
    }
    private List<NewTractor> addSponsoredItems(NewTractorResp newTractorResp) {
        try {
            if (newTractorResp.getData().getData().size() > 0 && newTractorResp.getData().getData().size() > 0 && newTractorResp.getOthers().getNewTractorSponsored() != null && newTractorResp.getOthers().getNewTractorSponsored().size() > 0) {
                if (newTractorResp.getData().getData().size() > 5 && newTractorResp.getOthers().getNewTractorSponsored().size() > 0) {
                    newTractorResp.getOthers().getNewTractorSponsored().get(0).getNewTractor().setSponsor(true);
                    newTractorResp.getData().getData().add(4, newTractorResp.getOthers().getNewTractorSponsored().get(0).getNewTractor());
                    if (newTractorResp.getData().getData().size() > 8 && newTractorResp.getOthers().getNewTractorSponsored().size() > 1) {
                        newTractorResp.getOthers().getNewTractorSponsored().get(1).getNewTractor().setSponsor(true);
                        newTractorResp.getData().getData().add(9, newTractorResp.getOthers().getNewTractorSponsored().get(1).getNewTractor());
                    }
                }

            }
        } catch (Exception e) {
            return newTractorResp.getData().getData();
        }
        return newTractorResp.getData().getData();
    }
    private void setMyFavorites(final NewTractor tractor,final int position) {
        viewModel.setMyFavorites(tractor).observe(getViewLifecycleOwner(), myResponse -> {
            switch (myResponse.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (myResponse.getData() != null) {
                        if (!myResponse.getData().isError()) {
                            mAdapter.getItem(position).setBookmark(!myResponse.getData().getMsg().get(0).equalsIgnoreCase("Bookmark Removed Successfully"));
                        } else {
                            Toast.makeText(activity, myResponse.getData().getMsg().get(0), Toast.LENGTH_LONG).show();
                        }
                    }
                    mAdapter.notifyItemChanged(position);
                    break;
                case ERROR:
                    mAdapter.notifyItemChanged(position);
                    break;
            }
        });
    }
}
