package com.khetigaadi.base
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.khetigaadi.base.databinding.ActivityWebViewBinding
class WebViewActivity : AppCompatActivity() {
    var binding: ActivityWebViewBinding? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
     //   WindowCompat.setDecorFitsSystemWindows(getWindow(), false)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        val view: View = binding!!.getRoot()
        setContentView(view)
//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(
//            view,
//            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
//                val systemBarsInsets = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
//                v!!.setPadding(
//                    systemBarsInsets.left,
//                    systemBarsInsets.top,
//                    systemBarsInsets.right,
//                    systemBarsInsets.bottom)
//                insets
//            })

        initToolbar()
        binding!!.webView.setWebViewClient(MyBrowser())
        binding!!.webView.getSettings().loadsImagesAutomatically = true
        binding!!.webView.getSettings().javaScriptEnabled = true
        binding!!.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)

        println("cxvcxvcxvcxvcx====="+intent.extras!!.getString(URL)!!)
        binding!!.webView.loadUrl(intent.extras!!.getString(URL)!!)
        Glide.with(this)
            .load(R.drawable.loader_white)
            .into(binding!!.llProgress.findViewById(R.id.ivLoader))
    }
    private fun initToolbar() {
        //setSupportActionBar(binding!!.llProgress.findViewById(R.id.llProgress))
        setSupportActionBar(binding!!.toolbar)
        binding!!.toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding!!.toolbar.setNavigationOnClickListener { v -> onBackPressed() }
        binding!!.toolbar.setTitle(intent.extras!!.getString(TITLE,getString(R.string.app_name)))
        binding!!.toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.ic_baseline_more_vert_24))
    }
    internal inner class MyBrowser:WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view:WebView,url:String):Boolean {
            view.loadUrl(url)
            return true
        }
//        override fun onPageStarted(view:WebView,url:String,favicon:Bitmap) {
//            super.onPageStarted(view,url,favicon)
//            @Suppress("DEPRECATED_IDENTITY_EQUALS")
//            if(binding!!.llProgress.visibility === View.GONE) {
//                binding!!.llProgress.visibility = View.VISIBLE
//            }
//        }
        override fun onPageFinished(view:WebView,url:String) {
            @Suppress("DEPRECATED_IDENTITY_EQUALS")
            if (binding!!.llProgress.visibility === View.VISIBLE) {
                binding!!.llProgress.visibility = View.GONE
            }
        }
        override fun onReceivedError(view:WebView,request:WebResourceRequest,error:WebResourceError) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Toast.makeText(this@WebViewActivity,error.description.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }
    companion object {
        const val TITLE = "title"
        const val URL = "url"
    }
}