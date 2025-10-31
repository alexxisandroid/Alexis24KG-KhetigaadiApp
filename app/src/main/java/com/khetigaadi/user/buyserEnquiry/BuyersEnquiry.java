package com.khetigaadi.user.buyserEnquiry;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.khetigaadi.R;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.tractor.usedTractor.UsedTractorDetailActivity;
import com.khetigaadi.user.SessionManager;
import com.khetigaadi.user.buyserEnquiry.model.InterestedBuyersResp;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;
@AndroidEntryPoint
public class BuyersEnquiry extends Fragment {
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    private final String TAG = getClass().getSimpleName();
    private BuyersEnquiryListAdapter mAdapter;
    @Inject
    SessionManager session;
    @Inject
    KhetiAPIs khetiAPIs;
    private RecycleListBinding binding;
    CompositeDisposable composite = new CompositeDisposable();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Glide.with(activity).load(R.drawable.loader_white).into(binding.ivLoader);
        binding.myRecyclerView.setHasFixedSize(true);
        binding.filterIcon.setVisibility(View.GONE);
        return view;
    }
    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBuyersEnq();
    }
    private void getBuyersEnq() {
        binding.llProgress.setVisibility(View.VISIBLE);
        composite.add(khetiAPIs.getBuyEnq()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<InterestedBuyersResp>() {
                    @Override
                    public void onSuccess(@NonNull InterestedBuyersResp interestedBuyersResp) {
                        binding.llProgress.setVisibility(View.GONE);
                        try {
                            if (!interestedBuyersResp.isError()) {
                                mAdapter = new BuyersEnquiryListAdapter(activity, interestedBuyersResp.getData(), interestedBuyersResp.getOthers());
                                binding.myRecyclerView.setAdapter(mAdapter);
                                mAdapter.setOnCallClickListener((position, v) -> {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + interestedBuyersResp.getData().get(position).getUsedTractor().getUser().getMobile()));
                                    startActivity(intent);
                                });
                                mAdapter.setOnItemClickListener((position, v) -> {
                                    Intent intent = new Intent(activity, UsedTractorDetailActivity.class);
                                    intent.putExtra("tractorId", interestedBuyersResp.getData().get(position).getUsedTractor().getId());
                                    startActivity(intent);
                                });

                            } else {
                                Toast.makeText(activity, interestedBuyersResp.getMsg().get(0), Toast.LENGTH_LONG).show();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        binding.llProgress.setVisibility(View.GONE);
                    }
                }));
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        Timber.i("Setting screen name: %s", TAG);
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
