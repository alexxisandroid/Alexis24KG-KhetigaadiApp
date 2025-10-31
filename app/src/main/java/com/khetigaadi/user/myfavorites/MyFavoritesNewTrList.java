package com.khetigaadi.user.myfavorites;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.def.PriceType;
import com.khetigaadi.tractor.newTractor.NewTrGetOnRoadPriceActivity;
import com.khetigaadi.tractor.newTractor.NewTractorDetails;
import com.khetigaadi.tractor.newTractor.NewTractorListAdapter;
import com.khetigaadi.tractor.newTractor.NewTractorRequestForDemo;
import com.khetigaadi.tractor.newTractor.model.NewTractor;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.user.myfavorites.model.MyFavNewTractor;
//import com.khetigaadi.user.myfavorites.model.MyFavNewTractor;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class MyFavoritesNewTrList extends Fragment {
    private static final String TAG = MyFavoritesNewTrList.class.getSimpleName();
    private MyFavNewTractorListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private MyFavoritesViewModel viewModel;
    private RecycleListBinding binding;
    public static MyFavoritesNewTrList getInstance() {
        return new MyFavoritesNewTrList();
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
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container,Bundle savedInstanceState)
    {
        binding = RecycleListBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this.getParentFragment()).get(MyFavoritesViewModel.class);
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.filterIcon.setVisibility(View.GONE);
        init();
        if (Constants.isConnectionAvailable(activity)) {
            getNewTractorList();
        } else {
            ((MainActivity) activity).noInternetConnection();
        }
        return binding.getRoot();
    }
    ActivityResultLauncher<Intent> tractorDetailsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    try {
                        int position = result.getData().getIntExtra("position",-1);
                        MyFavNewTractor tractor = mAdapter.getItem(position);
                        tractor.getNewTractor().setBookmark(result.getData().getBooleanExtra(UsedTractorDetailActivity.isFavourite,false));
                        if (tractor.getNewTractor().isBookmark())
                            mAdapter.updateItem(tractor, position);
                        else
                            mAdapter.itemRemove(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    private void init() {
        mAdapter = new MyFavNewTractorListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setLoaded();
        mAdapter.setOnItemClickListener(new NewTractorListAdapter.Listener() {
            @Override
            public void onClick(int position, @NotNull View view) {
                Intent intent = new Intent(activity, NewTractorDetails.class);
                intent.putExtra(NewTractorDetails.newTractorId,mAdapter.getItem(position).getNewTractor().getId());
                intent.putExtra("position", position);
                tractorDetailsActivityResultLauncher.launch(intent);
            }
            @Override
            public void onFavoriteClick(int position,@NotNull View view) {
                if (mAdapter.getList() != null) {
                    view.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.anim_scale));
                    setMyFavorites(mAdapter.getItem(position).getNewTractor());
                }
            }
            @Override
            public void onGetRoadPriceClick(int position,@NotNull View view) {
                Intent intent = new Intent(activity, NewTrGetOnRoadPriceActivity.class);
                intent.putExtra("id", mAdapter.getItem(position).getNewTractor().getId());
                intent.putExtra("name", mAdapter.getItem(position).getNewTractor().getName());
                intent.putExtra("image", mAdapter.getOthers().getNewTractorImagePath() + mAdapter.getItem(position).getNewTractor().getImage().getTractor_img_name());
                intent.putExtra("priceType", PriceType.TRACTOR);
                startActivity(intent);
            }
            @Override
            public void onGetDemoClick(int position,@NotNull View view) {
                Intent intent = new Intent(activity, NewTractorRequestForDemo.class);
                intent.putExtra("id",mAdapter.getItem(position).getNewTractor().getId());
                intent.putExtra("name",mAdapter.getItem(position).getNewTractor().getName());
                intent.putExtra("image",mAdapter.getOthers().getNewTractorImagePath() + mAdapter.getItem(position).getNewTractor().getImage().getTractor_img_name());
                intent.putExtra("demoType",PriceType.TRACTOR);
                startActivity(intent);
            }
        });
        viewModel.getNewTractors().observe(getViewLifecycleOwner(),myFavNewTractors->mAdapter.updateItems(myFavNewTractors));
    }
    private void getNewTractorList() {
        //Set layout manager to position the items
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
    private void setMyFavorites(final NewTractor tractor) {
        viewModel.setNewTractorFavorites(tractor).observe(this,response -> {
            switch (response.getStatus()) {
                case LOADING:
                    break;
                case SUCCESS:
                    if (response.getData() != null) {
                        Toast.makeText(activity,response.getData().getMsg().get(0),Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ERROR:
                    Toast.makeText(activity,response.getError(),Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}