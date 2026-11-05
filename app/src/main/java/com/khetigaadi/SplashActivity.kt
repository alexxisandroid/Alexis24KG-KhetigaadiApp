package com.khetigaadi
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
//import androidx.core.graphics.Insets
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowCompat
//import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.gson.Gson
import com.khetigaadi.base.BaseActivity
import com.khetigaadi.base.model.AllBannerResp
import com.khetigaadi.base.model.Banner
import com.khetigaadi.commons.SharedPreferenceUtils
import com.khetigaadi.commons.utils.Constants
import com.khetigaadi.databinding.ActivitySplashBinding
import com.khetigaadi.language.SelectLanguage
import com.khetigaadi.notification.model.Notification
import com.khetigaadi.retrofit.KhetiAPIs
import com.khetigaadi.user.LoginActivity
import com.khetigaadi.user.SessionManager
import com.khetigaadi.user.retrofit.UserAPIs
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import kotlin.concurrent.schedule
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity:BaseActivity() {
    private val nextBanner = "NextBanner"
    @Suppress("PrivatePropertyName")
    private val TAG = javaClass.simpleName
    @Inject
    lateinit var session:SessionManager
    @Inject
    lateinit var khetiAPIs:KhetiAPIs
    @JvmField
    @Inject
    var userAPIs:UserAPIs? = null
    @Inject
    lateinit var disposable:CompositeDisposable
    @Inject
    lateinit var pref:SharedPreferenceUtils
    private lateinit var binding:ActivitySplashBinding
    private var isNavigated = false
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(window,false)

        enableEdgeToEdge()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        @Suppress("DEPRECATION")
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view:View = binding.root
        setContentView(view)

        val isInternetAvailable = isConnectionAvailable(this)
        if(isInternetAvailable) {
            getAllBanner()
            if(pref.getStringValue(Constants.APP_BANNER_KEY,"") != "") {
                try {
                    val allBanner = Gson().fromJson(pref.getStringValue(Constants.APP_BANNER_KEY,"{}"),AllBannerResp::class.java)
                    if(allBanner.banners != null) {
                        if(allBanner.banners.splash != null) {
                            Glide.with(this@SplashActivity)
                                .load(getBanner(allBanner.banners.splash))
                                .error(R.drawable.splash_banner)
                                .placeholder(R.drawable.splash_banner)
                                .into(binding.adImage)
                        }
                    }
                }
                catch(e:Exception) {
                    e.printStackTrace()
                }
            }
        }

        if(session.isLoggedIn) {
            if (isInternetAvailable) {
                FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(intent)
                    .addOnSuccessListener(this) { pendingDynamicLinkData ->
                        var deepLink: Uri? = null
                        if(pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.link
                        }
                        if (!isNavigated) {
                            isNavigated = true
                            callMainActivity(deepLink)
                        }
                    }
                    .addOnFailureListener(this) { e ->
                        Timber.tag(TAG).w(e,"getDynamicLink:onFailure")
                        if (!isNavigated) {
                            isNavigated = true
                            callMainActivity(null)
                        }
                    }
            }
            Handler(Looper.getMainLooper()).postDelayed({
                if (!isNavigated && !isFinishing) {
                    isNavigated = true
                    callMainActivity(intent?.data)
                }
            }, 800)
        }
        else {
            Handler(Looper.getMainLooper()).postDelayed({
                if (!isNavigated && !isFinishing) {
                    isNavigated = true
                    callLogin()
                }
            }, 800)
        }
    }
    @Suppress("DEPRECATION")
    fun isConnectionAvailable(context:Context):Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return(netInfo != null && netInfo.isConnected && netInfo.isConnectedOrConnecting && netInfo.isAvailable)
    }
    private fun getBanner(splash:List<Banner>):String {
        val currentBannerIndex = pref.getIntValue(nextBanner,0)
        return if(currentBannerIndex < splash.size) {
            pref.setValue(nextBanner,currentBannerIndex + 1)
            splash[currentBannerIndex].imageUrl
        }
        else {
            pref.setValue(nextBanner,1)
            splash[0].imageUrl
        }
    }
    private fun callLogin() {
        if(pref.getBooleanValue(Constants.IS_FIRST_TIME_LAUNCH,true)) {
            val i = Intent(this@SplashActivity,SelectLanguage::class.java)
            startActivity(i)
            pref.setValue(Constants.IS_FIRST_TIME_LAUNCH,false)
        }
        else {
            val i = Intent(this@SplashActivity,LoginActivity::class.java)
            startActivity(i)
        }
        finish()
    }
    private fun callMainActivity(deepLink:Uri?) {
        FcmApi.registerFCMToken(userAPIs,session) {
        }
        checkAndNavigateToMain(deepLink)
    }
    private fun checkAndNavigateToMain(deepLink:Uri?) {
        val i:Intent
        if(pref.getBooleanValue(Constants.IS_FIRST_TIME_LAUNCH,true)) {
            i = Intent(this@SplashActivity,SelectLanguage::class.java)
            startActivity(i)
            pref.setValue(Constants.IS_FIRST_TIME_LAUNCH,false)
        }
        else {
            i = Intent(this@SplashActivity,MainActivity::class.java)
            @Suppress("DEPRECATION")
            if(intent.hasExtra(Constants.NOTIFICATION_INTENT_KEY))
                i.putExtra(Constants.NOTIFICATION_INTENT_KEY,intent.getParcelableExtra<Parcelable>(Constants.NOTIFICATION_INTENT_KEY) as Notification)
            i.putExtras(intent)
            if(deepLink == null)
                i.data = intent.data
            else
                i.data = deepLink
            i.action = intent.action
            startActivity(i)
        }
        finish()
    }
    private fun getAllBanner() {
        disposable.add(khetiAPIs.banner
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data: AllBannerResp ->
                if(!data.isError) {
                    pref.setValue(Constants.APP_BANNER_KEY,Gson().toJson(data))
                }
            }
            ) { error:Throwable ->
            })
    }
    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}