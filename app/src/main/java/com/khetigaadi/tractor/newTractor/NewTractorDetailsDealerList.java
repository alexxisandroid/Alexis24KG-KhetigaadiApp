package com.khetigaadi.tractor.newTractor;
import android.content.Intent;
import android.net.Uri;
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
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.tractor.dealer.DealersListAdapter;
import com.khetigaadi.tractor.dealer.GetDealersDetails;
import org.jetbrains.annotations.NotNull;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
@AndroidEntryPoint
public class NewTractorDetailsDealerList extends Fragment {
    private static final String TAG = NewTractorDetailsDealerList.class.getSimpleName();
    private DealersListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private RecycleListBinding binding;
    private NewTractorViewModel viewModel;
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
    public View onCreateView(@NotNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Glide.with(activity)
                .load(R.drawable.loader_white)
                .into(binding.ivLoader);
        binding.filterIcon.setVisibility(View.GONE);
        binding.myRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.myRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider((NewTractorDetails) activity).get(NewTractorViewModel.class);
        initAdapter();
        getDealersList(1);
    }
    private void initAdapter() {
        mAdapter = new DealersListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnGetDetailsClickListener((position, v) -> {
            try {
                Intent intent = new Intent(activity, GetDealersDetails.class);
                intent.putExtra("dealerId", mAdapter.getList().get(position).getId());
                startActivity(intent);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        });
        mAdapter.setOnCallClickListener((position, v) -> {
            try {
                String[] paths = mAdapter.getList().get(position).getContact().split("/");
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + paths[0]));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        mAdapter.setOnLoadMoreListener(this::getDealersList);
    }
    private void getDealersList(int page) {
       try {
           viewModel.getDealers(page).observe(activity,dealerListRespStateData -> {
               switch (dealerListRespStateData.getStatus()) {
                   case LOADING:
                       if (page == 1)
                           binding.llProgress.setVisibility(View.VISIBLE);
                       break;
                   case SUCCESS:
                       binding.llProgress.setVisibility(View.GONE);
                       if (dealerListRespStateData.getData() != null) {
                           mAdapter.setOthers(dealerListRespStateData.getData().getOthers());
                           mAdapter.setTotPages(dealerListRespStateData.getData().getData().getLastPage());
                           mAdapter.addItems(dealerListRespStateData.getData().getData().getDealers(), page);
                       }
                       break;
                   case ERROR:
                       Toast.makeText(activity, dealerListRespStateData.getError(), Toast.LENGTH_SHORT).show();
                       break;
               }
           });
       }
       catch(Exception e) {
           e.printStackTrace();
       }
    }
}
