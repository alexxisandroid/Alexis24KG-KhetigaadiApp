package com.khetigaadi.user.profile
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.khetigaadi.R
import com.khetigaadi.commons.activities.BaseActivity
import com.khetigaadi.databinding.ActivitySocialMediaBinding
import com.khetigaadi.social.SocialMediaFragment
import com.khetigaadi.util.Utils
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class SocialMedia:BaseActivity() {
    private var mFragmentManager:FragmentManager? = null
    private lateinit var mFragmentTransaction:FragmentTransaction
    private lateinit var facebookFragment:Fragment
    private lateinit var twitterFragment:Fragment
    private var linkedInFragment: SocialMediaFragment? = null
    private var binding:ActivitySocialMediaBinding? = null
    protected override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialMediaBinding.inflate(layoutInflater)
        val view:View? = binding?.getRoot()
        setContentView(view)
        initToolbar()
        socialMediaUrl = ArrayList<String?>()
        socialMediaUrl!!.add("https://www.facebook.com/khetigaadi.India/")
        socialMediaUrl!!.add("https://www.instagram.com/khetigaadi/")
        socialMediaUrl!!.add("https://www.linkedin.com/in/khetigaadi")
        mFragmentManager = supportFragmentManager
        //        callFacebookPage();
        if(intent.extras != null) {
            val page: Int? = intent.extras?.getInt("page",1)
            when (page) {
                1 -> {
                    callFacebookPage()
                }
                2 -> {
                    callInstagramPage()
                }
                3 -> {
                    callLinkedInPage()
                }
                else -> {
                    callFacebookPage()
                }
            }
        }
        binding?.imgBtnFacebook?.setOnClickListener({ v -> callFacebookPage() })
        binding?.imgBtnInstagram?.setOnClickListener({ v -> callInstagramPage() })
        binding?.imgBtnLinkedIn?.setOnClickListener({ v -> callLinkedInPage() })
        if (!Utils.isConnectionAvailable(this)) {
            noInternetConnection(binding?.parent)
        }
    }
    private fun initToolbar() {
        setSupportActionBar(binding?.toolbars?.toolbar)
        binding?.toolbars?.toolbar?.setNavigationIcon(R.drawable.abc_ic_ab_back_material)
        binding?.toolbars?.toolbar?.setNavigationOnClickListener({ v -> onBackPressed() })
        binding?.toolbars?.toolbar?.setTitle(getString(R.string.social_media))
        supportActionBar?.title = getString(R.string.social_media)
    }
    private fun callLinkedInPage() {
        if(linkedInFragment == null) {
            linkedInFragment = SocialMediaFragment.newInstance(socialMediaUrl!![2])
        }
        mFragmentTransaction = mFragmentManager!!.beginTransaction()
        mFragmentTransaction!!.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit)
        mFragmentTransaction!!.replace(binding?.clPageViewer!!.id,linkedInFragment!!).commit()
    }
    private fun callInstagramPage() {
        if(twitterFragment == null) {
            twitterFragment = SocialMediaFragment.newInstance(socialMediaUrl!![1])
        }
        mFragmentTransaction = mFragmentManager!!.beginTransaction()
        mFragmentTransaction!!.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit)
        mFragmentTransaction!!.replace(binding?.clPageViewer!!.id,twitterFragment).commit()
    }
    private fun callFacebookPage() {
        if(facebookFragment == null) {
            facebookFragment = SocialMediaFragment.newInstance(socialMediaUrl!![0])
        }
        mFragmentTransaction = mFragmentManager!!.beginTransaction()
        mFragmentTransaction!!.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
        mFragmentTransaction!!.replace(binding?.clPageViewer!!.id,facebookFragment).commit()
    }
    companion object {
        private var socialMediaUrl: ArrayList<String?>? = null
    }
}