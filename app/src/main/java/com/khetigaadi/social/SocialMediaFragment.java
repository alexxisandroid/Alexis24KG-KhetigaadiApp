package com.khetigaadi.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.khetigaadi.databinding.SocialMediaViewBinding;

/**
 * Created by sarads8 on 10/7/2016.
 */
public class SocialMediaFragment extends Fragment {
    private static String url1;
    private static boolean first = true;
    private SocialMediaViewBinding binding;

    public static SocialMediaFragment newInstance(String url) {
        SocialMediaFragment fragment = new SocialMediaFragment();
        Bundle args = new Bundle();
        args.putString(url1, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SocialMediaViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        binding.webView.setWebViewClient(new MyBrowser());
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (getArguments() != null) {
            binding.webView.loadUrl(getArguments().getString(url1));
        }
        return view;
    }

    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            if (binding.llProgress.llProgress.getVisibility() == View.GONE && first) {
                binding.llProgress.llProgress.setVisibility(View.VISIBLE);
                first = false;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                if (binding.llProgress.llProgress.getVisibility() == View.VISIBLE) {
                    binding.llProgress.llProgress.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
