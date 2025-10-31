package com.khetigaadi.base
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.khetigaadi.base.databinding.FragmentWebViewBinding
class WebViewFragment : Fragment() {
    private var binding: FragmentWebViewBinding? = null
    private var activity: FragmentActivity? = null
    private var url: String? = null
    private var htmlContent: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity()
        if (arguments != null) {
            url = requireArguments().getString(URL)
            htmlContent = requireArguments().getString(HTML_CONTENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding!!.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.webView.getSettings().setLoadsImagesAutomatically(true)
        binding!!.webView.getSettings().setJavaScriptEnabled(true)
        binding!!.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        if (!TextUtils.isEmpty(url)) {
            binding!!.webView.setWebViewClient(MyBrowser())
            binding!!.webView.loadUrl(url!!)
        } else {
            htmlContent =
                "<html><head><style type=\"text/css\">@font-face {font-family: Museosans-Regular; src: url(\"file:///android_asset/Font/Museosans-Regular.otf\")} @font-face {font-family: Museosans-Bold; src: url(\"file:///android_asset/Font/Museosans-Bold.otf\")} body {font-family:Museosans-Regular;font-size:18px;padding: 17px 17px 0 17px;} b{font-family:Museosans-Bold;}</style></head><body>$htmlContent</body></html>"
            binding!!.webView.loadDataWithBaseURL(null, htmlContent!!, "text/html", "utf-8", null)
        }
        Glide.with(this)
            .load(R.drawable.loader_white)
            .into<Target<Drawable>>(binding!!.llProgress.findViewById(R.id.ivLoader))
    }
    internal inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
        override fun onPageStarted(view:WebView,url:String,favicon:Bitmap) {
            super.onPageStarted(view, url, favicon)
            if (binding?.llProgress!!.getVisibility() === View.GONE) {
                binding?.llProgress!!.setVisibility(View.VISIBLE)
            }
        }
        override fun onPageFinished(view:WebView,url:String) {
            if (binding!!.llProgress.getVisibility() === View.VISIBLE) {
                binding!!.llProgress.setVisibility(View.GONE)
            }
        }
        override fun onReceivedError(view:WebView,request:WebResourceRequest,error:WebResourceError) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(activity, error.description.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        const val URL = "url"
        const val HTML_CONTENT = "htmlContent"
        fun newInstance(url: String?, htmlContent: String?): WebViewFragment {
            val fragment = WebViewFragment()
            val args = Bundle()
            args.putString(URL, url)
            args.putString(HTML_CONTENT, htmlContent)
            fragment.setArguments(args)
            return fragment
        }
    }
}