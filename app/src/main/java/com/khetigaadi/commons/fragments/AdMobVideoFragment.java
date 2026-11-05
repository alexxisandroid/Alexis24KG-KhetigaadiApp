package com.khetigaadi.commons.fragments;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.khetigaadi.MainActivity;
import com.khetigaadi.base.model.Banner;
import com.khetigaadi.databinding.FragmentAdMobVideoBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.List;
public class AdMobVideoFragment extends DialogFragment {
    public static final String TAG = "AdMobVideoFragment";
    private FragmentAdMobVideoBinding binding;
    private FragmentActivity activity;

    public AdMobVideoFragment() {
        // Required empty public constructor
    }
    public static AdMobVideoFragment newInstance() {
        return new AdMobVideoFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentAdMobVideoBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.ibClose.setOnClickListener(v -> dismiss());
        initializePlayer();
       /* if (isAdded()) {
            if (youTubePlayer1 != null && !youTubePlayer1.isPlaying()) {
                dismiss();
            }
        }*/
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
    private void initializePlayer() {
        try {
            List<Banner> videoList = ((MainActivity) activity).allBanner.getBanners().getHomePageVideo();
            if (videoList != null && videoList.size() > 0) {
                getLifecycle().addObserver(binding.youtubePlayerView);
                binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(videoList.get(0).getImageUrl().replaceAll("https:\\/\\/khetigaadi.com\\/images\\/banners\\/MobileApp\\/", ""), 0F);
                    }
                });
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            dismiss();
        }
    }
}