package com.khetigaadi.social;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.khetigaadi.databinding.SocialMediaViewBinding;
public class SocialMediaFragment extends Fragment {
    private static String url1="url1";
    private static boolean first = true;
    private SocialMediaViewBinding binding;
    public static SocialMediaFragment newInstance(String url) {
        SocialMediaFragment fragment = new SocialMediaFragment();
        Bundle args = new Bundle();
        args.putString(url1, url);
        fragment.setArguments(args);
        return fragment;
    }
    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        binding = SocialMediaViewBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        // binding.webView.setWebViewClient(new MyBrowser());
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);

        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setAllowContentAccess(true);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setBuiltInZoomControls(true);
        binding.webView.getSettings().setBlockNetworkImage(false);
        //binding.webView.getSettings().setCacheMode(CookieManager.getInstance().acceptCookie());
        binding.webView.getSettings().setGeolocationEnabled(true);
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);


// Set a browser-like User-Agent
        binding.webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 10; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Mobile Safari/537.36");


        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view,WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                binding.webView.loadUrl(request.getUrl().toString());
                Log.d("bcvbcvbcv23423===",""+getArguments()+"=="+getArguments().getString(url1)+"=="+request.getUrl().toString());
                return true;
            }
        });

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(binding.webView,true);

        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        Log.d("bcvbcvbcv===",""+getArguments()+"=="+getArguments().getString(url1));
        if(getArguments() != null) {
            try {
                // binding.webView.loadUrl(getArguments().getString(url1));
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                // intent.user
                intent.setData(Uri.parse(getArguments().getString(url1)));
                startActivity(intent);
            }
            catch(Exception e) {
                Log.d("cbvcbcvbcb====",""+e.getLocalizedMessage());
            }
        }
        return view;
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view,String url) {
            Log.d("cbvcbcvbcb22====",""+url);
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onLoadResource(WebView view,String url) {
            if (binding.llProgress.llProgress.getVisibility() == View.GONE && first) {
                binding.llProgress.llProgress.setVisibility(View.VISIBLE);
                first = false;
            }
        }
        @Override
        public void onPageFinished(WebView view,String url) {
            try {
                if(binding.llProgress.llProgress.getVisibility() == View.VISIBLE) {
                    binding.llProgress.llProgress.setVisibility(View.GONE);
                }
            }
            catch(Exception e) {
                Log.d("cvbcvbcvbcvbvc====",""+e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }
}
