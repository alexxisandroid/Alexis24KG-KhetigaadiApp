package com.khetigaadi.news.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.khetigaadi.MainActivity;
import com.khetigaadi.R;
import com.khetigaadi.commons.utils.Constants;
import com.khetigaadi.databinding.RecycleListBinding;
import com.khetigaadi.news.activity.NewsDetailsActivity;
import com.khetigaadi.news.adapter.NewsListAdapter;
import com.khetigaadi.retrofit.KhetiAPIs;
import com.khetigaadi.room.repository.NewsRepository;

import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;
@AndroidEntryPoint
public class NewsListFragment extends Fragment {
    private static final String TAG = NewsListFragment.class.getSimpleName();
    private NewsListAdapter mAdapter;
    private FragmentActivity activity;
    @Inject
    Tracker mTracker;
    @Inject
    KhetiAPIs khetiAPIs;
    @Inject
    NewsRepository newsRepository;
    private RecycleListBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = RecycleListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        ((MainActivity) activity).setToolbarTitle(getString(R.string.news));
        Glide.with(activity).load(R.drawable.loader_white).into(binding.ivLoader);
        binding. filterIcon.setVisibility(View.GONE);
        initAdapter();
        if (Constants.isConnectionAvailable(activity)) {
            getNews(1);
        }
        else {
            ((MainActivity) activity).noInternetConnection();
        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        //Google analytic code
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
    private void getNews(int page) {
        newsRepository.getNews(page).observe(getViewLifecycleOwner(),newsListRespStateData -> {
            switch (newsListRespStateData.getStatus()) {
                case LOADING:
                    if (page == 1)
                      binding.  llProgress.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    if (page == 1) {
                        if (newsListRespStateData.getData() != null) {
                            mAdapter.setTotPages(newsListRespStateData.getData().getData().getLastPage());
                            mAdapter.addItems(newsListRespStateData.getData().getData().getData().stream().filter(news -> news.getImage() != null && news.getImage().getImage() != null).collect(Collectors.toList()), page);
                            binding.  llProgress.setVisibility(View.GONE);
                            if (mAdapter.getItemCount() < 5 && newsListRespStateData.getData().getData().getLastPage() != page)
                                getNews(page + 1);
                        }
                    } else {
                        if (newsListRespStateData.getData() != null && mAdapter != null) {
                            mAdapter.addItems(newsListRespStateData.getData().getData().getData().stream().filter(news -> news.getImage() != null && news.getImage().getImage() != null).collect(Collectors.toList()), page);
                        }
                    }
                    break;
                case ERROR:
                    Timber.e(TAG, newsListRespStateData.getError());
                    break;
            }
        });
    }
    private void initAdapter() {
        mAdapter = new NewsListAdapter(activity, binding.myRecyclerView);
        binding.myRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, v) -> {
            Intent intent = new Intent(activity, NewsDetailsActivity.class);
            intent.putExtra("news_id", mAdapter.getList().get(position).getId());
            startActivity(intent);
        });
        mAdapter.setOnLoadMoreListener(this::getNews);
    }
}
